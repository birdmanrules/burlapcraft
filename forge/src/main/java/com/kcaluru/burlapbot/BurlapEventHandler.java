package com.kcaluru.burlapbot;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BurlapEventHandler {
	
	@SubscribeEvent
	public void LivingUpdateEvent(LivingEvent event) {
		
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (!player.inventory.hasItem(BurlapMod.finderWand)) {
				player.inventory.addItemStackToInventory(new ItemStack(BurlapMod.finderWand));
			} 
			if (!player.inventory.hasItem(BurlapMod.bridgeWand)) {
				player.inventory.addItemStackToInventory(new ItemStack(BurlapMod.bridgeWand));
			}
			if (!player.inventory.hasItem(BurlapMod.escapeWand)) {
				player.inventory.addItemStackToInventory(new ItemStack(BurlapMod.escapeWand));
			}
			if (!player.inventory.hasItem(Items.diamond_pickaxe)) {
				player.inventory.addItemStackToInventory(new ItemStack(Items.diamond_pickaxe));
			}
		}
		
	}
}
