package actors;

import cli.Actionable;
import cli.ObjectRegistry;
import core.Shop;
import core.ShopItem;
import core.Wallet;
import entities.Snowplow;
import equipments.Biokerosene;
import equipments.Consumable;
import equipments.DragonPlow;
import equipments.DumpPlow;
import equipments.Gravel;
import equipments.GravelPlow;
import equipments.IcebreakerPlow;
import equipments.Plow;
import equipments.Salt;
import equipments.SaltPlow;
import equipments.SweeperPlow;

import java.util.ArrayList;
import java.util.List;
import topology.Lane;
import topology.Road;

import cli.Linkable;

/**
 * A takarító operátor a szimulációban. Feladata a hókotrók irányítása, felszerelések vásárlása,
 * a gépek felszerelésének kezelése, nyersanyagok utántöltése a saját raktárából, 
 * valamint a sikeres hótakarítás után érmék gyűjtése.
 */
public class Cleaner extends Player implements Actionable, Linkable {
    private Wallet wallet;
    private List<Snowplow> fleet = new ArrayList<>();
    private List<Consumable> inventory = new ArrayList<>();


    // --- KONSTRUKTOROK ---
	
    /**
     * Alapértelmezett konstruktor.
     */
    public Cleaner() {
        super();
    }

    /**
     * Paraméteres konstruktor a név megadásához.
     *
     * @param name a takarító neve
     */
    public Cleaner(String name) {
        super(name);
    }

    /**
     * Paraméteres konstruktor minden fő attribútummal.
     *
     * @param name a takarító neve
     * @param wallet a takarító pénztárcája
     * @param fleet a takarító hókotró flottája
     */
    public Cleaner(String name, Wallet wallet, List<Snowplow> fleet) {
        super(name);
        this.wallet = wallet;
        this.fleet = fleet;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a takarító pénztárcáját.
     *
     * @return a pénztárca
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Beállítja a takarító pénztárcáját.
     *
     * @param wallet a beállítandó pénztárca
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Visszaadja a takarító hókotró flottáját.
     *
     * @return a hókotrók listája
     */
    public List<Snowplow> getFleet() {
        return fleet;
    }

    /**
     * Beállítja a takarító hókotró flottáját.
     *
     * @param fleet a beállítandó flotta
     */
    public void setFleet(List<Snowplow> fleet) {
        this.fleet = fleet;
    }

    /**
     * Visszaadja a takarító raktárát (fogyóeszközök).
     *
     * @return a raktár tartalma
     */
    public List<Consumable> getInventory() {
        return inventory;
    }

    /**
     * Beállítja a takarító raktárát.
     *
     * @param inventory a beállítandó raktár
     */
    public void setInventory(List<Consumable> inventory) {
        this.inventory = inventory;
    }


    // --- METÓDUSOK ---

    /**
     * Végrehajtja a megnevezett akciót a takarító kontextusában.
     *
     * @param actionName az akció neve (pl. "buyItem", "commandSnowplow")
     * @param args       a parancssor további paraméterei
     * @param registry   a központi objektumtár
     * @throws Exception ha az akció sikertelen
     */
    @Override
    public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
        switch (actionName) {
            case "buyItem":
                buyItemAction(args, registry);
                break;
            case "commandSnowplow":
                commandSnowplowAction(args, registry);
                break;
            case "refillPlow":
                refillPlowAction(args, registry);
                break;
            case "equipPlowToSnowplow":
                equipPlowToSnowplowAction(args, registry);
                break;
            default:
                throw new Exception();
        }
    }

    /**
     * Összekapcsolja az objektumot más objektumokkal.
     *
     * @param property A beállítandó tulajdonság neve (pl. "wallet", "addInventory").
     * @param args     Az összekapcsoláshoz szükséges argumentumok.
     * @param registry Az objektumtár.
     * @throws Exception Ha az összekapcsolás sikertelen.
     */
    @Override
    public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
        switch (property) {
            case "wallet":
            case "setWallet":
                Wallet w = (Wallet) registry.getObject(args[0]);
                setWallet(w);
                break;
            case "addInventory":
                inventory.add((Consumable) registry.getObject(args[0]));
                break;
            default:
                throw new Exception("Action failed: Unknown link property '" + property + "' for Cleaner");
        }
    }

    /**
     * A "buyItem" akció feloldása és végrehajtása.
     *
     * Pszeudokód:
     * 1. Ellenőrzi az argumentumok számát.
     * 2. Kikeresi a boltot és a vásárolni kívánt tételt.
     * 3. Opcionálisan kikeresi a cél hókotrót.
     * 4. Meghívja a buyItem(...) üzleti metódust.
     * 
     * @param args A parancs argumentumai (Bolt ID, Tárgy neve, opcionális Snowplow ID).
     * @param registry Az objektumtár.
     * @throws Exception Ha a vásárlás vagy regisztráció sikertelen.
     */
    private void buyItemAction(String[] args, ObjectRegistry registry) throws Exception {
        if (args.length < 2) {
            throw new Exception("Action failed: buyItem requires at least a shop ID and item name");
        }
        try {
            Shop shop = (Shop) registry.getObject(args[0]);
            ShopItem item = ShopItem.valueOf(args[1]);

            Snowplow targetSp = null;
            if (args.length >= 3) {
                targetSp = (Snowplow) registry.getObject(args[2]);
            }

            Object purchased = buyItemInternal(shop, item, targetSp);
            if (purchased != null) {
                registerPurchasedObject(item, purchased, registry);
            }
        } catch (ClassCastException e) {
            throw new Exception("Action failed: Invalid parameter type for buyItem");
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid argument type: " + args[1]);
        }
    }

    /**
     * A "commandSnowplow" akció feloldása.
     * args[0] = Snowplow ID, args[1] = Road ID, args[2] = Lane ID
        *
        * Pszeudokód:
        * 1. Ellenőrzi az argumentumok számát.
        * 2. Feloldja az ID-kat objektumokra.
        * 3. Meghívja a commandSnowplow(...) metódust.
     */
    private void commandSnowplowAction(String[] args, ObjectRegistry registry) throws Exception {
        if (args.length < 3) {
            throw new Exception("Action failed: commandSnowplow requires snowplow, road, and lane IDs");
        }
        try {
            Snowplow sp = (Snowplow) registry.getObject(args[0]);
            Road toRoad = (Road) registry.getObject(args[1]);
            Lane toLane = (Lane) registry.getObject(args[2]);
            commandSnowplow(sp, toRoad, toLane);
        } catch (ClassCastException e) {
            throw new Exception("Action failed: Invalid parameter type for commandSnowplow");
        }
    }

    /**
     * A "refillPlow" akció feloldása.
     * args[0] = Snowplow ID, args[1] = Consumable ID
        *
        * Pszeudokód:
        * 1. Ellenőrzi az argumentumok számát.
        * 2. Feloldja a hókotrót és a nyersanyagot.
        * 3. Meghívja a refillPlow(...) metódust.
     */
    private void refillPlowAction(String[] args, ObjectRegistry registry) throws Exception {
        if (args.length < 2) {
            throw new Exception("Action failed: refillPlow requires snowplow and consumable IDs");
        }
        try {
            Snowplow sp = (Snowplow) registry.getObject(args[0]);
            Consumable resource = (Consumable) registry.getObject(args[1]);
            refillPlow(sp, resource);
        } catch (ClassCastException e) {
            throw new Exception("Action failed: Invalid parameter type for refillPlow");
        }
    }

    /**
     * Az "equipPlowToSnowplow" akció feloldása.
     * args[0] = Snowplow ID, args[1] = Plow ID
     *
     * Pszeudokód:
     * 1. Ellenőrzi az argumentumok számát.
     * 2. Feloldja az ID-kat objektumokra.
     * 3. Meghívja az equipPlowToSnowplow(...) metódust.
     */
    private void equipPlowToSnowplowAction(String[] args, ObjectRegistry registry) throws Exception {
        if (args.length < 2) {
            throw new Exception("Action failed: equipPlowToSnowplow requires snowplow and plow IDs");
        }
        try {
            Snowplow sp = (Snowplow) registry.getObject(args[0]);
            Plow plow = (Plow) registry.getObject(args[1]);
            equipPlowToSnowplow(sp, plow.getClass());
        } catch (ClassCastException e) {
            throw new Exception("Action failed: Invalid parameter type for equipPlowToSnowplow");
        }
    }

    /**
     * A hókotrót irányítja a megadott útra és sávra (sávváltás).
     *
     * @param sp az irányítandó hókotró
     * @param toRoad a cél úthálózat
     * @param toLane a cél sáv az útban
     *
     * Pszeudokód:
     * 1. Jogosultság ellenőrzése (fleet.contains(sp)).
     * 2. Sávváltási kísérlet: sp.changeLane(toLane).
     * 3. Sikertelenség esetén hibajelzés.
     */
    public void commandSnowplow(Snowplow sp, Road toRoad, Lane toLane) {
        if (!ownsSnowplow(sp)) {
            throw new IllegalStateException("Action failed: You are not allowed to control this snowplow.");
        }
        if (toRoad == null || toLane == null || toLane.getRoad() != toRoad) {
            throw new IllegalStateException("Action failed: Failed lane change or topological blockage.");
        }
        if (!sp.changeLane(toLane)) {
            throw new IllegalStateException("Action failed: Failed lane change or topological blockage.");
        }
    }

    /**
     * A takarító egy felszerelést vásárol a boltból (pl. sót, biokerozint vagy kotrófejeket).
     * Kotrófej vásárlás esetén meg kell adni a cél hókotrót, nyersanyagnál ez null lehet.
     *
     * @param shop a bolt, ahonnan vásárol
     * @param item a megvásárolni kívánt felszerelés azonosítója
     * @param targetSp a cél hókotró (csak kotrófej vásárlásakor kötelező)
     *
     * Pszeudokód:
     * 1. Meghívja a shop.tryPurchase(...)-t.
     * 2. Sikertelenség esetén kivételt dob.
     * 3. Siker esetén a tárgy típusától függő további műveletek futnak.
     */
    public void buyItem(Shop shop, ShopItem item, Snowplow targetSp) throws Exception {
        buyItemInternal(shop, item, targetSp);
    }

    /**
     * Utasítja a hókotrót, hogy szerelje fel az adott típusú fejet a saját készletéből.
     *
     * @param sp a módosítandó hókotró
     * @param plowClass a felszerelni kívánt kotrófej típusa
     *
     * Pszeudokód:
     * 1. Jogosultság ellenőrzése.
     * 2. A megfelelő fej kiválasztása a saját készletből.
     * 3. Felszerelés delegálása a Snowplow felé.
     */
    public void equipPlowToSnowplow(Snowplow sp, Class<? extends Plow> plowClass) {
        if (!ownsSnowplow(sp)) {
            throw new IllegalStateException("Action failed: You are not allowed to control this snowplow.");
        }
        if (plowClass == null) {
            throw new IllegalStateException("Action failed: Invalid plow type.");
        }

        try {
            Plow plow = plowClass.getDeclaredConstructor().newInstance();
            sp.equipPlow(plow);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Action failed: Cannot equip the requested plow type.");
        }
    }

    /**
     * Feltölti a hókotróra szerelt kotrófej tartályát a megadott nyersanyaggal, 
     * kivéve azt a takarító raktárából.
     *
     * @param sp a hókotró, amelynek a tartályát tölteni kell
     * @param resource a betöltendő nyersanyag példány a raktárból
     *
     * Pszeudokód:
     * 1. Ellenőrzi, hogy a resource szerepel-e az inventory listában.
     * 2. Lekéri az aktuális fejet.
     * 3. Típushelyes refill és inventory-ból törlés.
     */
    public void refillPlow(Snowplow sp, Consumable resource) throws Exception {
        if (!ownsSnowplow(sp)) {
            throw new Exception("Action failed: You are not allowed to control this snowplow.");
        }
        if (resource == null || !inventory.contains(resource)) {
            throw new Exception("Action failed: No such resource in your inventory.");
        }

        Plow equipped = sp.getEquippedPlow();
        if (equipped == null) {
            throw new Exception("Action failed: Snowplow has no equipped plow.");
        }

        String plowType = equipped.getConsumableType();
        if (plowType == null || !plowType.equals(resource.getConsumableType())) {
            throw new Exception("Action failed: Incompatible resource for the equipped plow.");
        }
        equipped.refill(resource);
        inventory.remove(resource);
    }

    /**
     * Új nyersanyagot ad a takarító raktárához (vásárlás után).
     *
     * @param c a megvásárolt nyersanyag
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi a paraméter érvényességét.
	 * 2. Hozzáadja a nyersanyagot az inventory listához.
     */
    public void addConsumable(Consumable c) {
        if (c == null) {
            return;
        }
        inventory.add(c);
    }

    /**
     * A takarító érméket szerez a sikeres hótakarítás után. Ez a metódus a Hókotró
     * (Snowplow) osztály által hívódik meg a sikeres tisztítás után.
     *
     * Pszeudokód:
     * 1. Lekéri a wallet objektumot.
     * 2. Meghívja a wallet.add(...) metódust.
     */
    public void achieveCoin() {
        if (wallet != null) {
            wallet.add(1);
        }
    }

    private boolean ownsSnowplow(Snowplow sp) {
        if (sp == null) {
            return false;
        }
        return fleet.contains(sp) || sp.getOwner() == this;
    }

    private Object buyItemInternal(Shop shop, ShopItem item, Snowplow targetSp) throws Exception {
        if (shop == null || item == null) {
            throw new Exception("Action failed: Invalid purchase request.");
        }
        if (!shop.tryPurchase(this, item)) {
            throw new Exception("Action failed: Insufficient funds in Wallet.");
        }

        switch (item) {
            case Biokerosene: {
                Biokerosene bio = new Biokerosene();
                addConsumable(bio);
                return bio;
            }
            case Salt: {
                Salt salt = new Salt();
                addConsumable(salt);
                return salt;
            }
            case Gravel: {
                Gravel gravel = new Gravel();
                addConsumable(gravel);
                return gravel;
            }
            case DragonPlow:
            case SaltPlow:
            case DumpPlow:
            case SweeperPlow:
            case IcebreakerPlow:
            case GravelPlow: {
                Plow plow = createPlow(item);
                if (targetSp != null) {
                    if (!ownsSnowplow(targetSp)) {
                        throw new Exception("Action failed: You are not allowed to control this snowplow.");
                    }
                    targetSp.equipPlow(plow);
                }
                return plow;
            }
            case Snowplow: {
                Snowplow sp = new Snowplow();
                sp.setOwner(this);
                sp.equipPlow(new SweeperPlow());
                return sp;
            }
            default:
                throw new Exception("Action failed: Unknown shop item.");
        }
    }

    private Plow createPlow(ShopItem item) {
        switch (item) {
            case DragonPlow:
                return new DragonPlow();
            case SaltPlow:
                return new SaltPlow();
            case DumpPlow:
                return new DumpPlow();
            case SweeperPlow:
                return new SweeperPlow();
            case IcebreakerPlow:
                return new IcebreakerPlow();
            case GravelPlow:
                return new GravelPlow();
            default:
                throw new IllegalArgumentException("Invalid plow item: " + item);
        }
    }

    private void registerPurchasedObject(ShopItem item, Object purchased, ObjectRegistry registry) throws Exception {
        String prefix = item.name().toLowerCase();
        int index = 1;
        String candidate = prefix + index;

        while (registry.getObjects().containsKey(candidate)) {
            index++;
            candidate = prefix + index;
        }

        registry.register(candidate, purchased);
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     * @param registry A központi regiszter.
     */
    @Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println("Cleaner," + id);
        System.out.println("name," + this.getName());
        System.out.println("wallet," + registry.findId(wallet));

        StringBuilder fleetStr = new StringBuilder("[");
        for (int i = 0; i < fleet.size(); i++) {
            if (i > 0) fleetStr.append(",");
            fleetStr.append(registry.findId(fleet.get(i)));
        }
        fleetStr.append("]");
        System.out.println("fleet," + fleetStr.toString());

        StringBuilder invStr = new StringBuilder("[");
        for (int i = 0; i < inventory.size(); i++) {
            if (i > 0) invStr.append(",");
            invStr.append(registry.findId(inventory.get(i)));
        }
        invStr.append("]");
        System.out.println("inventory," + invStr.toString());
    }
}
