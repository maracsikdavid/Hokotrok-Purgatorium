package entities;

import actors.BusDriver;
import core.Skeleton;
import topology.BusStop;
import topology.Intersection;
import topology.Lane;

/**
 * A busz tömegközelkedési jármű. Tartozik hozzá egy BusDriver-t, illetve a térképen előre megadott kezdő
 *  és végállomás. A busz célja, hogy e között a 2 MapNode között minél többször megforduljon és
 * ezzel pontot szerezzen.
 */
public class Bus extends Vehicle {
	private BusStop startNode;
	private BusStop endNode;
	private BusDriver driver;


	// --- GETTEREK ÉS SETTEREK ---
	public BusStop getStartNode() {
		return startNode;
	}
	public void setStartNode(BusStop startNode) {
		this.startNode = startNode;
	}

	public BusStop getEndNode() {
		return endNode;
	}
	public void setEndNode(BusStop endNode) {
		this.endNode = endNode;
	}

	public BusDriver getDriver() {
		return driver;
	}
	public void setDriver(BusDriver driver) {
		this.driver = driver;
	}


	// --- METÓDUSOK ---
	/**
	 * A busz idő függvényében történő  állapotváltozásokat implementáló függvény
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		this.move();
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * A buszok mozgatása implementáló függvény. 
	 */
	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");
		switch (Skeleton.getActiveTestCaseId()) {
			case 10, 11, 12, 13:{
				if (getTargetLane() != null) {
					getTargetLane().acceptVehicle(this);
				}
				
				if (getCurrentLane() != null) {
					getCurrentLane().removeVehicle(this);
				}
				break;
			}
			case 24: {
                Lane nextLane = getTargetLane();
                if (nextLane != null) {
                    nextLane.acceptVehicle(this);
                }

                Lane currentLane = getCurrentLane();
                if (currentLane != null) {
                    currentLane.removeVehicle(this);
                }

                topology.Road road = (nextLane != null) ? nextLane.getRoad() : null;
                if (road != null) {
                    road.getTargetNode();
                }

                int answer = Skeleton.getIntFromUser("A jelenlegi csomópont egyenlő a busz cél csompontjával? (1: Yes, 0: No)");

                if (answer == 1 && driver != null) {
                    driver.achievePoints();
                }
                break;
            }
			case 27, 28: {
				Lane current = this.getCurrentLane();
				int vegeE = Skeleton.getIntFromUser("Elérte a busz a sáv végét? (1: Igen, 0: Nem)");
				if (vegeE == 0) {
					this.setProgress(this.getProgress() + 1);
				}else{
					if (current != null && current.getRoad() != null && current.getRoad().getTargetNode() != null) {
						current.getRoad().getTargetNode().routeVehicles();
					}
				}
				break;
			}
			case 29, 30, 31: {
				Lane current = this.getCurrentLane();
				
				int obstacle = Skeleton.getIntFromUser("Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)");
				if (obstacle == 1) {
					Lane l1 = (current != null) ? current.getLeftLane() : null;
					int l1Empty = Skeleton.getIntFromUser("Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)");
					if (l1Empty == 1 && l1 != null) {
						this.changeLane(l1);
					} else if (l1Empty == 0) {
						Lane l3 = (current != null) ? current.getRightLane() : null;
						int l3Empty = Skeleton.getIntFromUser("Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)");
						if (l3Empty == 1 && l3 != null) {
							this.changeLane(l3);
						} else if (l3Empty == 0 && current != null) {
							current.acceptVehicle(this);
							this.stuck();
						}
					}
				} else {
					int vegeE = Skeleton.getIntFromUser("Elérte a bus a sáv végét? (1: Igen, 0: Nem)");
					if (vegeE == 0) {
						this.setProgress(this.getProgress() + 1);
					}
				}
				
				break;
			}
			
			default:
				break;
		}

		Skeleton.printReturn(this, "move");
	}

	/**
	 * Ellenőrzi, hogy a busz bénulhat-e, tehát mozgásképtelenné válik.
	 *  A buszok is bénulhatnak jeges sávon vagy abban az esetben, ha összeütköznek másik járművel.
	 *
	 * @return igaz (a busz lebénul)
	 */
	@Override
	public boolean isParalizable() {
		Skeleton.printCall(null, this, "isParalizable");
		Skeleton.printReturn(this, "isParalizable", "true");
		return true;
	}

	/**
	 * Abban az esetben ha a busz elbénul, ez a függvény felelős az időtartam számon tartásáért.
	 *
	 * @param time az időtartam
	 */
	public void paralyze(int time) {
		Skeleton.printCall(null, this, "paralyze");
		Skeleton.printReturn(this, "paralyze");
	}

	/**
	 * Ellenőrzi, hogy a busz elakadt-e.
	 *
	 * @return igaz, ha elakadt
	 */
	public boolean stuck() {
		Skeleton.printCall(null, this, "stuck");
		Skeleton.printReturn(this, "stuck", "false");
		return false;
	}

	/**
	 * A busz sávváltása.
	 *
	 * @param target a cél sáv
	 * @return igaz, ha sikeres
	 */
	@Override
	public boolean changeLane(Lane target) {
		Skeleton.printCall(null, this, "changeLane");
		Lane old = this.getCurrentLane();
			target.acceptVehicle(this);
			if (old != null) {
				old.removeVehicle(this);
			}
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}
}
