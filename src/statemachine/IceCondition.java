package statemachine;
import cli.ConsoleOutput;
import core.GameRules;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import topology.Road;
import topology.Lane;

/**
 * A sávok (Lane) jégpáncéllal borított állapotát reprezentáló osztály.
 * Ez a legveszélyesebb közlekedési körülmény a szimulációban.
 * A normál járművek (Autó, Busz) ezen a sávon 20% eséllyel
 * megcsúszhatnak és balesetet szenvedhetnek (lebénulhatnak). 
 * A Hókotrók (Snowplow) speciális felépítésük miatt immunisak a jégre.
 * Ezt az állapotot a Sószóró (SaltPlow), Tűzhányó (DragonPlow) vagy a Jégtörő (IcebreakerPlow) 
 * segítségével lehet megszüntetni.
 */
public class IceCondition implements LaneCondition {
    private static final Random RNG = new Random(42);
    private int saltTimer = -1;


    // --- KONSTRUKTOROK ---
    /**
     * Alapértelmezett konstruktor.
     */
    public IceCondition() {
    }

    /**
     * Paraméteres konstruktor a só hatásának aktivációs idejével.
     *
     * @param saltTimer a só hatásának aktivációs ideje tickekben
     */
    public IceCondition(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a sózás időzítőjét.
     *
     * @return a hátralévő tickek száma
     */
    public int getSaltTimer() {
        return saltTimer;
    }

    /**
     * Beállítja a sózás időzítőjét.
     *
     * @param saltTimer a beállítandó tickek száma
     */
    public void setSaltTimer(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- METÓDUSOK ---

    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. A szimuláció során a sáv ezen a metóduson keresztül számolhatja 
     * például a kiszórt só hatásának aktivációs idejét.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük.
     */
    @Override
    public void tick(Lane lane) {
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // No change inside tunnel
        }
        if (saltTimer > 0) {
            saltTimer--;
            if (saltTimer == 0) {
                lane.setState(new CleanCondition());
            }
        } else if (saltTimer == 0) {
            lane.setState(new CleanCondition());
        }
    }

    /**
     * Havazás (csapadék) éri a jéggel borított sávot. Intenzív hóesés 
     * hatására a jégpáncél felett vastag hótakaró alakulhat ki, így a sáv 
     * állapota ThickSnowCondition-re módosulhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a hó esik.
     */
    @Override
    public void addSnow(Lane lane) {
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // No snow inside tunnel
        }
        lane.setState(new ThickSnowCondition());
    }

    /**
     * Sót juttat a jeges sávra. A só megolvasztja a jég.
     */
    @Override
    public void applySalt(Lane lane) {
        saltTimer = GameRules.SALT_ACTIVATION_TICKS;
    }

    /**
     * Kavics szórása a jeges sávra, amely növeli a tapadást. 
     * Ennek hatására az állapot GraveledIceCondition-re változhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {
        lane.setState(new GraveledIceCondition());
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a jeges sávra. 
     * Ha a jármű bénítható (Autó vagy Busz), a rendszer megcsúszást 
     * generálhat, ami időleges mozgásképtelenséget okoz. 
     * A Hókotrók immunisak a jégre.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a jármű rálép.
     * @param vehicle Az a jármű, amelyik a sávra érkezik.
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle vehicle) {
        if (vehicle == null || !vehicle.isParalizable()) {
            return;
        }

        boolean slip = shouldSlip(lane, vehicle);
        if (!slip) {
            return;
        }

        Vehicle target = pickCollisionTarget(lane, vehicle);
        vehicle.paralyze(GameRules.COLLISION_PARALYZE_TICKS);
        if (target != null) {
            target.paralyze(GameRules.COLLISION_PARALYZE_TICKS);
        }
    }

    private boolean shouldSlip(Lane lane, Vehicle vehicle) {
        if (ConsoleOutput.isTestMode()) {
            if (lane.getVehicles() != null && lane.getVehicles().size() > 1) {
                return true;
            }
            return vehicle instanceof entities.Bus;
        }
        return RNG.nextDouble() < GameRules.ICE_SLIP_PROBABILITY;
    }

    private Vehicle pickCollisionTarget(Lane lane, Vehicle vehicle) {
        List<Vehicle> candidates = new ArrayList<>();
        if (lane != null && lane.getVehicles() != null) {
            for (Vehicle other : lane.getVehicles()) {
                if (other != null && other != vehicle) {
                    candidates.add(other);
                }
            }
        }

        if (!candidates.isEmpty()) {
            if (candidates.size() == 1) {
                return candidates.get(0);
            }
            if (ConsoleOutput.isTestMode()) {
                return candidates.get(0);
            }
        }

        Road road = lane != null ? lane.getRoad() : null;
        if (road == null || road.getLanes() == null) {
            return candidates.isEmpty() ? null : candidates.get(0);
        }

        for (Lane l : road.getLanes()) {
            if (l == null || l.getVehicles() == null) {
                continue;
            }
            for (Vehicle other : l.getVehicles()) {
                if (other != null && other != vehicle) {
                    candidates.add(other);
                }
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        double sum = 0.0;
        double[] weights = new double[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            Vehicle c = candidates.get(i);
            int distance = laneDistance(vehicle.getCurrentLane(), c.getCurrentLane());
            double w = 1.0 / Math.max(1, distance);
            weights[i] = w;
            sum += w;
        }

        if (ConsoleOutput.isTestMode()) {
            int bestIndex = 0;
            for (int i = 1; i < candidates.size(); i++) {
                if (weights[i] > weights[bestIndex]) {
                    bestIndex = i;
                }
            }
            return candidates.get(bestIndex);
        }

        double pick = RNG.nextDouble() * sum;
        double acc = 0.0;
        for (int i = 0; i < candidates.size(); i++) {
            acc += weights[i];
            if (pick <= acc) {
                return candidates.get(i);
            }
        }
        return candidates.get(candidates.size() - 1);
    }

    private int laneDistance(Lane a, Lane b) {
        if (a == null || b == null) {
            return 1;
        }
        if (a == b) {
            return 1;
        }
        if (a.getRoad() == null || b.getRoad() == null || a.getRoad() != b.getRoad()) {
            return 1;
        }
        List<Lane> lanes = a.getRoad().getLanes();
        if (lanes == null) {
            return 1;
        }
        int ia = lanes.indexOf(a);
        int ib = lanes.indexOf(b);
        if (ia < 0 || ib < 0) {
            return 1;
        }
        return Math.abs(ia - ib) + 1;
    }

    /**
     * Az objektum állapotának és adatainak kiírása.
     *
     * @param id Az objektum azonosítója.
     * @param registry Az objektumtár.
     */
    public void printData(String id, cli.ObjectRegistry registry) {
        System.out.println(this.getClass().getSimpleName());
    }
}