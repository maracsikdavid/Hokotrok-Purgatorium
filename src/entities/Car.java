package entities;

import core.Skeleton;
import topology.Building;
import topology.Intersection;
import topology.Lane;

	/**
	 * Az autó {jarmű egy személykocsi. Munkahelye és otthone között mozog, akadályokat kikerülhet,
	 * és jeges sávon megcsúszhat. Az autós lakik egy épületben (otthon) és másikéban dolgozik (munka).
	 */
	public class Car extends Vehicle {
		private Building homeNode;
		private Building workplaceNode;


		// --- GETTEREK ÉS SETTEREK ---
		public Building getHomeNode() {
			return homeNode;
		}
		public void setHomeNode(Building homeNode) {
			this.homeNode = homeNode;
		}

		public Building getWorkplaceNode() {
			return workplaceNode;
		}
		public void setWorkplaceNode(Building workplaceNode) {
			this.workplaceNode = workplaceNode;
		}


	// --- METÓDUSOK ---
	/**
	 * Az autó időzítés lépése, idő függvényében történő változást valósítja meg
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		this.move();
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * Az autó mozgatása. Ha nincs bénultság, a progress nö.
	 * Ha eléri a sáv végét, majd elindul a következőn
	 */
	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");
		switch (Skeleton.getActiveTestCaseId()) {
			case 1, 2: {
				Lane current = this.getCurrentLane();
				int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
				if (vegeE == 0) {
					this.setProgress(this.getProgress() + 1);
				}else{
					if (current != null && current.getRoad() != null && current.getRoad().getTargetNode() != null) {
						current.getRoad().getTargetNode().routeVehicles();
					}
				}
				break;
			}
			case 3, 4, 5: {
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
					int isEnd = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
					if (isEnd == 0) {
						this.setProgress(this.getProgress() + 1);
					}
				}
			
				break;
			}
			case 10, 11, 12, 13:{
				if (getTargetLane() != null) {
					getTargetLane().acceptVehicle(this);
				}
				
				if (getCurrentLane() != null) {
					getCurrentLane().removeVehicle(this);
				}
				break;	
			}
			default:
				break;
		}

		Skeleton.printReturn(this, "move");
	}

		/**
		 * Ellenőrzi, hogy az autó megbénulhat-e. Az autós bénulhatnak jeges sávon az ütközések miatt.
		 *
		 * @return igaz (az autós megbánult)
		 */
		@Override
		public boolean isParalizable() {
			Skeleton.printCall(null, this, "isParalizable");
			Skeleton.printReturn(this, "isParalizable", "true");
			return true;
		}

		/**
		 * Az autót bénulásából eltelt idő. Ez idő alatt nem mozoghat
		 * @param time az időtartam, amig az autó mozgasképtelen
		 */
		public void paralyze(int time) {
			Skeleton.printCall(null, this, "paralyze");
			Skeleton.printReturn(this, "paralyze");
		}

		/**
		 * Ellenőrzi, hogy az autó elakadt-e vagy sem. 
		 * Ha egyéb jarűvel ütközık, akkor igaz.
		 *
		 * @return igaz, ha az autó elakadt
		 */
		public boolean stuck() {
			Skeleton.printCall(null, this, "stuck");
			Skeleton.printReturn(this, "stuck", "false");
			return false;
		}

		/**
		 * Az autó sávváltást kísérel meg.
		 *
		 * @param target a cél sáv
		 * @return igaz, ha a váltás sikeres
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
