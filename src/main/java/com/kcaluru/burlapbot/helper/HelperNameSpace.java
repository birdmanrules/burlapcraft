package com.kcaluru.burlapbot.helper;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.RegistryNamespaced;

public class HelperNameSpace {
	
	//-------------ATTRIBUTE STRINGS---------------------
	public static final String							ATX = "x";
	public static final String							ATY = "y";
	public static final String							ATZ = "z";
	public static final String							ATBTYPE = "blockType";
	public static final String 							ATROTDIR = "rotationalDirection";
	public static final String							ATVERTDIR = "verticalDirection";
	public static final String							ATIBQUANT = "inventoryBlockQuantity";
	
	//-------------BURLAP OBJECTCLASS STRINGS-------------
	public static final String							CLASSAGENT = "agent";
	public static final String							CLASSBLOCK = "block";
	public static final String							CLASSINVENTORYBLOCK = "inventoryBlock";
	
	//-------------ACTIONS STRINGS-------------
	public static final String						ACTIONMOVE = "moveFowardAction";
	public static final String						ACTIONROTATERIGHT = "rotateRightAction";
	public static final String 						ACTIONROTATELEFT = "rotateLeftAction";
	public static final String 						ACTIONDESTBLOCK = "destroyBlockAction";
	public static final String						ACTIONPLACEBLOCK = "placeBlockAction";
	public static final String						ACTIONAHEAD = "lookAheadAction";
	public static final String						ACTIONDOWNONE = "lookDownOneAction";
	
	//-------------ENUMS-------------
	public enum RotDirection {
		NORTH(2), EAST(3), SOUTH(0), WEST(1);
		public static final int size = RotDirection.values().length;
		private final int intVal;
		
		RotDirection(int i) {
			this.intVal = i;
		}
		
		public int toInt() {
			return this.intVal;
		}
		
		public static RotDirection fromInt(int i) {
			switch (i) {
			case 2:
				return NORTH;
			case 0:
				return SOUTH;
			case 3:
				return EAST;
			case 1:
				return WEST;
			
			}
			return null;
		}
		
	}
	
	public enum VertDirection {
		AHEAD(0), DOWNONE(1);
		public static final int size = VertDirection.values().length;
		private final int intVal;
		
		VertDirection(int i) {
			this.intVal = i;
		}
		
		public int toInt() {
			return this.intVal;
		}
		
		public static VertDirection fromInt(int i) {
			switch (i) {
			case 1:
				return DOWNONE;
			case 0:
				return AHEAD;
			
			}
			return null;
		}
	}
	
	
}