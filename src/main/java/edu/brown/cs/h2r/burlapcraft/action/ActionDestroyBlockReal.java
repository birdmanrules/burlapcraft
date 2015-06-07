package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class ActionDestroyBlockReal extends ActionAgentReal {

	public ActionDestroyBlockReal(String name, Domain domain) {
		super(name, domain);
	}

	public ActionDestroyBlockReal(String name, Domain domain, int sleepMS) {
		super(name, domain, sleepMS);
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Destroy Block");
		HelperActions.destroyBlock();
		

	}

}
