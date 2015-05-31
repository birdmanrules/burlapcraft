package com.kcaluru.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import com.kcaluru.burlapcraft.BurlapCraft;
import com.kcaluru.burlapcraft.handler.HandlerDungeonGeneration;
import com.kcaluru.burlapcraft.handler.HandlerEvents;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.item.ItemFinderWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandTeleport implements ICommand {
	
	private final List aliases;
	
	public CommandTeleport() {
		aliases = new ArrayList();
		aliases.add("teleport");
		aliases.add("tele");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "teleport";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "teleport <dungeon name>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length == 0) {
				sender.addChatMessage(new ChatComponentText("Invalid argument"));
				return;
			}
			
			if (args.length > 1) {
				sender.addChatMessage(new ChatComponentText("This command takes just one argument"));
				return;
			}
			
			EntityPlayer player = HandlerEvents.player;
			String dungeonName = args[0];
			int dungeonID = 0;
			
			if (dungeonName.equals("out")) {
				player.setPositionAndUpdate(HandlerDungeonGeneration.playerSpawnPos.x, HandlerDungeonGeneration.playerSpawnPos.y, HandlerDungeonGeneration.playerSpawnPos.z);
				return;
			}
			
			for (int i = 0; i < HelperNameSpace.dungeonNameList.length; i++) {
				if (dungeonName.equals(HelperNameSpace.dungeonNameList[i])) {
					dungeonID = i;
				}
			}
			
			if (dungeonID > 0) {
				switch (dungeonID) {
				case 1:
					// start x, y and z of player within dungeon
					double playerFinderX = 1.5; 
					double playerFinderY = 1;
					double playerFinderZ = 3;
					
					// teleport the player to finder dungeon
					player.setPositionAndUpdate(HandlerDungeonGeneration.finderX + playerFinderX, HandlerDungeonGeneration.finderY + playerFinderY, HandlerDungeonGeneration.finderZ + playerFinderZ);
					
					// update the dugeonLocID
					BurlapCraft.dungeonLocID = 1;
					
					break;
					
				case 2:
					// start x, y and z of agent within dungeon
					double playerBridgeX = 1.5; 
					double playerBridgeY = 2;
					double playerBridgeZ = 4;
					
					// teleport the player to bridge dungeon
					player.setPositionAndUpdate(HandlerDungeonGeneration.bridgeX + playerBridgeX, HandlerDungeonGeneration.bridgeY + playerBridgeY, HandlerDungeonGeneration.bridgeZ + playerBridgeZ);
					
					// update the dungeonLocID
					BurlapCraft.dungeonLocID = 2;
					
					break;
					
				}
			}
			else {
				sender.addChatMessage(new ChatComponentText("Dungeon not found"));
			}
		}
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
