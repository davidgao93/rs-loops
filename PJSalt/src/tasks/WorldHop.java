package tasks;
import java.util.Random;

import org.osbot.rs07.script.Script;
import framework.Node;
import data.Data;

public class WorldHop implements Node {
	
	private Script s;
	private Data data;
	
	public static int[] Worlds = {2, 3, 4, 5, 6, 9, 10, 11,
		12, 14, 18, 19, 20, 22, 27, 28, 29, 30, 33, 34, 36, 38, 41,
		42, 43, 44, 46, 50, 51, 52, 54, 58, 59, 60, 62, 67, 68, 69, 70,
		74, 76, 77, 78, 86};
	public static int worldCounter;
	Random rnd = new Random();
	public int hopToWorld; 
	
	public WorldHop(Script s, Data data) {
		this.s = s;
		this.data = data;
	}
	
	@Override
	public boolean validate() {
		return s.npcs.closest("Konoo") == null 
				&& data.getWorldHopUsage()
				&& data.south.contains(s.myPlayer());
	}

	@Override
	public void run() throws InterruptedException {
		s.log("WORLDHOP OPERATION");
       
        if (Worlds != null) {
        	while(s.worlds.getCurrentWorld() == data.getCurrentWorld()) {
        		hopToWorld = getRandom(Worlds);
				s.log("Trying to hop to " + hopToWorld);
				if (s.worlds != null) {
					s.worlds.hop(hopToWorld);
				}
				Script.sleep(Script.random(3200, 3500));
        	}
        	
        	if (s.worlds.getCurrentWorld() != data.getCurrentWorld()) {
    			s.log("Hop successful, old world: " + data.getCurrentWorld() 
    					+ " New World: " + s.worlds.getCurrentWorld());
    			Script.sleep(Script.random(3200, 3500));
    			data.setCurrentWorld(s.worlds.getCurrentWorld());
    		}
        }
	}
	
	public static int getRandom(int[] array) {
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
}
