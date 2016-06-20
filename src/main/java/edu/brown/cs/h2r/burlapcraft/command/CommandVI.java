package edu.brown.cs.h2r.burlapcraft.command;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.solver.MinecraftSolver;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CommandVI implements ICommand {
	
	private final List aliases;
	
	public CommandVI() {
		aliases = new ArrayList();
		aliases.add("vi");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "vi";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "vi [gamma] \nIf gamma is no specified 0.99 is used";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length > 1) {
				sender.addChatMessage(new ChatComponentText("This command takes only 1 optional argument: the gamma factor"));
				return;
			}
			
			Dungeon dungeon = BurlapCraft.currentDungeon;

			
			if (dungeon == null) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}

			
			
			double gamma = 0.99;
			if(args.length == 1){
				gamma = new Double(args[0]);
			}
			
			final double fgamma = gamma;

			Thread bthread = new Thread(new Runnable() {
				@Override
				public void run() {
					MinecraftSolver.stocasticPlan(fgamma);
				}
			});

			bthread.start();

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