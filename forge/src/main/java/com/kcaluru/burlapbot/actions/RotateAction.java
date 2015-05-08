package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class RotateAction extends AgentAction {

	private int direction;
	
	public RotateAction(String name, Domain domain, int direction) {
		
		super(name, domain);
		this.direction = direction;
		
	}

	@Override
	void doAction(State state) {
		
		switch (this.direction) {
		case 0:
			BurlapAIHelper.faceSouth();
		case 1:
			BurlapAIHelper.faceWest();
		case 2:
			BurlapAIHelper.faceNorth();
		case 3:
			BurlapAIHelper.faceEast();
		default:
			break;
		}
		
	}

}
