package com.kcaluru.burlapbot.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kcaluru.burlapbot.BurlapWorldGenHandler;
import com.kcaluru.burlapbot.domaingenerator.DungeonWorldDomain;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.items.ItemFinderWand;
import com.kcaluru.burlapbot.stategenerator.StateGeneratorFinderDungeon;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.Policy.PolicyUndefinedException;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldStateParser;
import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.SinglePFTF;
import burlap.oomdp.singleagent.common.UniformCostRF;

public class SolverBridgeDungeon {

	
	
	DungeonWorldDomain 			dwdg;
	Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int index = 0;
	private int actionSize = 0;
	private int curX = (int) Math.ceil(Minecraft.getMinecraft().thePlayer.posX);
	private final int curY = (int) Math.ceil(Minecraft.getMinecraft().thePlayer.posY);
	private int curZ = (int) Math.ceil(Minecraft.getMinecraft().thePlayer.posZ);
	private int destX;
	private int destZ;
	
	public SolverBridgeDungeon(int [][][] map, int startX, int startZ, int destX, int destZ) {
		
		System.out.println(startX);
		System.out.println(startZ);
		System.out.println(destX);
		System.out.println(destZ);
		
		//create the domain
		dwdg = new DungeonWorldDomain(10, 15, 15);

		domain = dwdg.generateDomain();
		
		this.destX = destX;
		this.destZ = destZ;
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
//		rf = new UniformCostRF();
		rf = new MovementRF(this.destX, this.destZ);
//		tf = new SinglePFTF(domain.getPropFunction(NameSpace.PFATGOAL));
		tf = new MovementTF(this.destX, this.destZ);
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGeneratorFinderDungeon.getCurrentState(domain);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
		hashingFactory.setAttributesForClass(GridWorldDomain.CLASSAGENT, 
		domain.getObjectClass(GridWorldDomain.CLASSAGENT).attributeList); 
	}
	
	public void BFS() {
		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
		planner.planFromState(initialState);
		
		Policy p = new SDPlannerPolicy(planner);
//		
//		State cur = new State();
//		State next = new State();
//		
//		AbstractGroundedAction aga = p.getAction(initialState);
//		System.out.println(aga);
//		next = aga.executeIn(cur);
		
//		System.out.println(startX);
		
//		State next = new State();		
//		AbstractGroundedAction action = p.getAction(initialState);
//		System.out.println(action);
//		next = action.executeIn(initialState);
//		System.out.println(next);
//		
//		while (next != null) {
//			action = p.getAction(next);
//			System.out.println(action);
//			next = action.executeIn(next);
//			System.out.println(next);
//		}	
//		
//		System.out.println(p.getAction(initialState));
		
		
		System.out.println(p.evaluateBehavior(initialState, rf, tf).getActionSequenceString("\n"));
		
		executeActions(p.evaluateBehavior(initialState, rf, tf).getActionSequenceString("\n"), this.destX, this.destZ);
	
	}
	
	public void executeActions(String actionString, final int destX, final int destZ) {
		final String[] lines = actionString.split("\\r?\\n");
		final Timer timer = new Timer();
		actionSize = lines.length;
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  if (index < actionSize) {
					  if (lines[index].equals("northAction")) {
						  BurlapAIHelper.walkNorth(false, curX - 1, curY - 2, curZ - 1);
						  curZ -= 1;
					  }
					  else if (lines[index].equals("southAction")) {
						  BurlapAIHelper.walkSouth(false, curX - 1, curY - 2, curZ - 1);
						  curZ += 1;
					  }
					  else if (lines[index].equals("eastAction")) {
						  BurlapAIHelper.walkEast(false, curX - 1, curY - 2, curZ - 1);
						  curX += 1;
					  }
					  else if (lines[index].equals("westAction")) {
						  BurlapAIHelper.walkWest(false, curX - 1, curY - 2, curZ - 1);
						  curX -= 1;
					  }
					  index += 1;
				  }
				  else {
					  BurlapAIHelper.faceBlock(BurlapWorldGenHandler.bridgeX + 2, curY - 1, BurlapWorldGenHandler.bridgeZ + 1);
					  timer.cancel();
				  }
			  }
		}, 0, 1000);
	}
	
	public static class MovementRF implements RewardFunction{

		int goalX;
		int goalZ;
		
		public MovementRF(int goalX, int goalZ) {
			this.goalX = goalX;
			this.goalZ = goalZ;
		}
		
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			//get location of agent in next state
			ObjectInstance agent = sprime.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			
			// are they at goal location?
			if(ax == (this.goalX - 1) && az == (this.goalZ - 1) || ax == (this.goalX + 1) && az == (this.goalZ + 1) || ax == (this.goalX + 1) && az == (this.goalZ - 1)
					|| ax == (this.goalX - 1) && az == (this.goalZ + 1) || ax == (this.goalX) && az == (this.goalZ - 1)
					|| ax == (this.goalX) && az == (this.goalZ + 1) || ax == (this.goalX - 1) && az == (this.goalZ) || ax == (this.goalX + 1) && az == (this.goalZ)) {
				return 100.;
			}
			// are they anywhere else?
//			else if(ax) {
//				
//			}
			// are they on lava
			else {
				return -1;
			}
			
			
		}
		
		
	}
	
	public static class MovementTF implements TerminalFunction{

		int goalX;
		int goalZ;
		
		public MovementTF(int goalX, int goalZ) {
			this.goalX = goalX;
			this.goalZ = goalZ;
		}
		
		@Override
		public boolean isTerminal(State s) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			
			//are they at goal location?
			if(ax == (this.goalX - 1) && az == (this.goalZ - 1) || ax == (this.goalX + 1) && az == (this.goalZ + 1) || ax == (this.goalX + 1) && az == (this.goalZ - 1)
					|| ax == (this.goalX - 1) && az == (this.goalZ + 1) || ax == (this.goalX) && az == (this.goalZ - 1)
					|| ax == (this.goalX) && az == (this.goalZ + 1) || ax == (this.goalX - 1) && az == (this.goalZ) || ax == (this.goalX + 1) && az == (this.goalZ)) {
				return true;
			}
			
			return false;
		}
		
	}
}