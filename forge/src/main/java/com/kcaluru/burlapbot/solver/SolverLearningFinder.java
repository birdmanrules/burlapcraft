package com.kcaluru.burlapbot.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kcaluru.burlapbot.BurlapWorldGenHandler;
import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.items.ItemFinderWand;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.Policy.PolicyUndefinedException;
import burlap.behavior.singleagent.learning.modellearning.models.TabularModel;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
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

public class SolverLearningFinder {

	DomainGeneratorReal 			dwdg;
	public Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int length;
	private int height;
	private int width;
	final int nConfident = 1;
	final double maxVIDelta = 0.1;
	final int maxVIPasses = 20;
	final double maxReward = 0;
	
	public SolverLearningFinder(int length, int width, int height) {
		
		// set the length, width and height of the dungeon
		this.length = length;
		this.width = width;
		this.height = height;
		
		//create the domain
		dwdg = new DomainGeneratorReal(length, width, height);

		domain = dwdg.generateDomain();
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
		rf = new UniformCostRF();
		tf = new MovementTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, 1);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
	}
	
	public void RMAX() {
		
		PotentialShapedRMax planner = new PotentialShapedRMax(domain, rf, tf, 0.95, hashingFactory, maxReward, nConfident, maxVIDelta, maxVIPasses);
		
		int maxSteps = 1;
		for (int i = 0; i < maxSteps; i++) {
			EpisodeAnalysis ea = planner.runLearningEpisodeFrom(initialState, 500);
		}
	
	}
	
	public static class MovementTF implements TerminalFunction{

		int goalX;
		int goalY;
		int goalZ;
		
		@Override
		public boolean isTerminal(State s) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int ay = agent.getDiscValForAttribute(NameSpace.ATY);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			int rotDir = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
			int vertDir = agent.getDiscValForAttribute(NameSpace.ATVERTDIR);
			
			List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (block.getDiscValForAttribute(NameSpace.ATBTYPE) == 41) {
					goalX = block.getDiscValForAttribute(NameSpace.ATX);
					goalY = block.getDiscValForAttribute(NameSpace.ATY);
					goalZ = block.getDiscValForAttribute(NameSpace.ATZ);
				} 
			}
			
			//are they at goal location or dead
			if(((ax == (this.goalX) && az == (this.goalZ - 1) && rotDir == 0 && vertDir == 1) || (ax == (this.goalX) && az == (this.goalZ + 1) && rotDir == 2 && vertDir == 1)
					|| (ax == (this.goalX - 1) && az == (this.goalZ) && rotDir == 3 && vertDir == 1) || (ax == (this.goalX + 1) && az == (this.goalZ) && rotDir == 1 && vertDir == 1)) 
					|| (BurlapAIHelper.getMinecraft().thePlayer.getHealth() == 0) || (BurlapAIHelper.getMinecraft().thePlayer.isBurning())) {
				return true;
			}
			
			return false;
		}
		
	}
	
	
//	public void BFS() {
//		
//		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
//		planner.planFromState(initialState);
//		
//		Policy p = new SDPlannerPolicy(planner);
//		
//		State cur = new State();
//		State next = new State();
//		
//		AbstractGroundedAction aga = p.getAction(initialState);
//		System.out.println(aga);
//		next = aga.executeIn(cur);
//		
//		System.out.println(startX);
//		
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
//		
//		
//		System.out.println(p.evaluateBehavior(initialState, rf, tf).getActionSequenceString("\n"));
//		
//		executeActions(p.evaluateBehavior(initialState, rf, tf).getActionSequenceString("\n"), this.destX, this.destZ);
//	}
	
//	public void executeActions(String actionString, final int destX, final int destZ) {
//		final String[] lines = actionString.split("\\r?\\n");
//		final Timer timer = new Timer();
//		actionSize = lines.length;
//		timer.scheduleAtFixedRate(new TimerTask() {
//			  @Override
//			  public void run() {
//				  if (index < actionSize) {
//					  if (lines[index].equals("northAction")) {
//						  BurlapAIHelper.walkNorth(false, curX, curY - 1, curZ);
//						  curZ -= 1;
//					  }
//					  else if (lines[index].equals("southAction")) {
//						  BurlapAIHelper.walkSouth(false, curX, curY - 1, curZ);
//						  curZ += 1;
//					  }
//					  else if (lines[index].equals("eastAction")) {
//						  BurlapAIHelper.walkEast(false, curX, curY - 1, curZ);
//						  curX += 1;
//					  }
//					  else if (lines[index].equals("westAction")) {
//						  BurlapAIHelper.walkWest(false, curX, curY - 1, curZ);
//						  curX -= 1;
//					  }
//					  index += 1;
//				  }
//				  else {
//					  BurlapAIHelper.faceBlock(BurlapWorldGenHandler.finderX + destX, curY - 1, BurlapWorldGenHandler.finderZ + destZ);
//					  timer.cancel();
//				  }
//			  }
//		}, 0, 1000);
//	}
}