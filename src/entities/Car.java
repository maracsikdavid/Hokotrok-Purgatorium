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
			case 1:
			case 2: {
				Lane current = this.getCurrentLane();
				int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
				if (vegeE == 0) {
					this.setProgress(this.getProgress() + 1);
				}else{
					Intersection i2 = new Intersection();
					Skeleton.registerObject(i2, "i2");
					i2.routeVehicles();
				}
				break;
			}
			case 3:
			case 4:
			case 5: {
				Lane current = this.getCurrentLane();
				boolean singleLaneNoNeighbors = current != null
						&& current.getLeftLane() == null
						&& current.getRightLane() == null;

				if (!singleLaneNoNeighbors) {
					int akadaly = Skeleton.getIntFromUser("Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)");
					if (akadaly == 1) {
						Lane left = (current != null) ? current.getLeftLane() : null;
						int balSzabad = Skeleton.getIntFromUser("Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)");
						if (balSzabad == 1 && left != null) {
							this.changeLane(left);
						} else if (balSzabad == 0) {
							Lane right = (current != null) ? current.getRightLane() : null;
							int jobbSzabad = Skeleton.getIntFromUser("Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)");
							if (jobbSzabad == 1 && right != null) {
								this.changeLane(right);
							} else if (jobbSzabad == 0 && current != null) {
								current.acceptVehicle(this);
								this.stuck();
							}
						}
					} else {
						int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
						if (vegeE == 0) {
							this.setProgress(this.getProgress() + 1);
						}
					}
				} else {
					int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
					if (vegeE == 0) {
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
