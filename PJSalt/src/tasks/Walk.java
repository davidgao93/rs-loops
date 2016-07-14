package tasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;

import framework.Node;
import data.Data;

public class Walk implements Node {
	
	private Script s;
	private Data data;
	private Player me;
	
	public Walk(Script s, Data data) {
		this.s = s;
		this.data = data;
		this.me = s.myPlayer();
	}
	
	@Override
	public boolean validate() {
		NPC konoo = s.npcs.closest("Konoo");
		if (konoo != null) {
			if (konoo.isVisible()) {
				return false;
			} 
		}
		return true;
	}
	
	@Override
	public void run() throws InterruptedException {
		Entity westEntity = s.getObjects().closest(data.west, "Saltpetre");
		Entity southEntity = s.getObjects().closest(data.south, "Saltpetre");
		Entity eastEntity = s.getObjects().closest(data.east, "Saltpetre");
		Entity seastEntity = s.getObjects().closest(data.seast, "Saltpetre");
		Entity swestEntity = s.getObjects().closest(data.swest, "Saltpetre");
		
		s.log("WALK OPERATION");
		data.setStatus("Walking to next area");
		if (eastEntity != null) {
			s.getWalking().webWalk(data.east);
		} else if (southEntity != null) {
			s.getWalking().webWalk(data.south);
		} else if (westEntity != null){
			s.getWalking().webWalk(data.west);
		} else if (seastEntity != null) {
			s.getWalking().webWalk(data.seast);
		} else if (swestEntity != null) {
			s.getWalking().webWalk(data.swest);
		} else {
			if (data.getWorldHopUsage()) {
				s.getWalking().walk(data.south);
			} else {
				if (data.west.contains(me)) {
					s.getWalking().walk(data.swest);
				} else if (data.seast.contains(me)) {
					s.getWalking().walk(data.west);
				} else {
					s.getWalking().walk(data.south);
				}
			}
		}
	}
}
