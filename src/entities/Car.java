	package entities;

	import core.Skeleton;
	import topology.Building;
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
			Skeleton.printCall(this, this, "tick");
			this.move();
			Skeleton.printReturn(this, "tick");
		}

		/**
		 * Az autó mozgatása. Ha nincs bénultság, a progress nö.
		 * Ha eléri a sáv végét, majd elindul a következőn
		 */
		@Override
		protected void move() {
		Skeleton.printCall(this, this, "move");

		Lane current = this.getCurrentLane();
		boolean singleLaneNoNeighbors = current != null
				&& current.getLeftLane() == null
				&& current.getRightLane() == null;

		if (!singleLaneNoNeighbors) {
			int akadaly = Skeleton.getIntFromUser("Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)");
			if (akadaly == 1) {
				if (current != null) {
					Lane left = current.getLeftLane();
					int balSzabad = Skeleton.getIntFromUser("Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)");
					if (balSzabad == 1 && left != null) {
						this.changeLane(left);
					} else if (balSzabad == 0) {
						Lane right = current.getRightLane();
						int jobbSzabad = Skeleton.getIntFromUser("Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)");
						if (jobbSzabad == 1 && right != null) {
							this.changeLane(right);
						} else if (jobbSzabad == 0) {
							current.acceptVehicle(this);
							this.stuck();
						}
					}
				}
			} else {
				int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
				if (vegeE == 0) {
					// progress növelése
					this.setProgress(this.getProgress() + 1);
				} else {
					//elérte a sáv végét -> TC_02...
				}
			}
		} else {
			int vegeE = Skeleton.getIntFromUser("Elérte az autó a sáv végét? (1: Igen, 0: Nem)");
			if (vegeE == 0) {
				// progress növelése
				this.setProgress(this.getProgress() + 1);
			} else {
				//elérte a sáv végét -> TC_02...
			}
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
