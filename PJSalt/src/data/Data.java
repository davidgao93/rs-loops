package data;

import org.osbot.rs07.api.map.Area;

public class Data {
	public Area b = new Area(1674, 3564, 1676, 3559);
	public Area west = new Area(1665, 3549, 1673, 3543);
	public Area south = new Area(1683, 3533, 1691, 3527);
	public Area east = new Area(1705, 3550, 1713, 3543);
	public Area seast = new Area(1713, 3522, 1720, 3516);
	public Area swest = new Area(1685, 3516, 1691, 3510);
	private int currentWorld = 0;
	
	private String status = "Initializing...";
	
	public static int[] Worlds = {2, 3, 4, 5, 6, 9, 10, 11,
		12, 14, 18, 19, 20, 22, 27, 28, 29, 30, 33, 34, 36, 38, 41,
		42, 43, 44, 46, 50, 51, 52, 54, 58, 59, 60, 62, 67, 68, 69, 70,
		74, 76, 77, 78, 86};
	public static int worldCounter;
	public int counter;
	
	public void setCurrentWorld(int c) {
		currentWorld = c;
	}
	
	public int getCurrentWorld() {
		return currentWorld;
	}
	
	public int SPADE = 952, SALT = 13421,
			STAM = 12631, STAM3 = 12627, STAM2=  12629, STAM4 = 12625,
			VIAL = 229;
	
	private boolean staminaUsage;
	private boolean worldHopUsage;
		
	
	public Data(boolean staminaUsage, boolean worldHopUsage) {
		this.staminaUsage = staminaUsage;
		this.worldHopUsage = worldHopUsage;
	}	
	
	public boolean getStaminaUsage() {
		return staminaUsage;
	}
	
	public boolean getWorldHopUsage() {
		return worldHopUsage;
	}
	
	public void setStatus(String s) {
		if (!s.equals(this.status)) { 
			this.status = s;
		}
	}
	
	public String getStatus() {
		return this.status;
	}
	
}

