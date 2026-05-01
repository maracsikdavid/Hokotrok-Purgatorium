package topology;

import cli.Actionable;
import cli.ObjectRegistry;

/**
 * Épület csomópont. A személyautók kiindulási (otthon) vagy célállomásaként (munkahely) szolgál
 */
public class Building extends MapNode implements Actionable {
		
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Building() {
		super();
	}


	// --- METÓDUSOK ---

	@Override
	public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
		switch (actionName) {
			default:
				throw new Exception();
		}
	}

}
