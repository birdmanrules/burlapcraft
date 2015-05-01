package com.kcaluru.burlapbot.items;

import com.kcaluru.burlapbot.BurlapMod;
import com.kcaluru.burlapbot.BurlapWorldGenHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemBridgeWand extends Item {

	// name of item
	private String name = "bridgewand";
//	public static int actualDestX;
//	public static int actualDestZ;
	
	
	// indicate whether agent is in dungeon or not
	public static boolean bridgeInside;
	
	public ItemBridgeWand() {
		
		// set brideInside to false
		bridgeInside = false;
		
		// give the item a name
		setUnlocalizedName(BurlapMod.MODID + "_" + name);
		
		// add the item to misc tab
		setCreativeTab(CreativeTabs.tabCombat);
		
		// set texture
		setTextureName(BurlapMod.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		
		if (bridgeInside) {

		}
		else {
			if (!world.isRemote) {
				ItemFinderWand.finderInside = false;
				player.setPositionAndUpdate((double) BurlapWorldGenHandler.posX + 42, (double) BurlapWorldGenHandler.posY + 42, (double) BurlapWorldGenHandler.posZ + 4);
				bridgeInside = true;
			}
		}
        return itemStack;
    }
}
