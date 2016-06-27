package tasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;

import framework.Node;
import data.Data;

public class Walk implements Node {
	
	private Script s;
	private Data data;
	
	public Walk(Script s, Data data) {
		this.s = s;
		this.data = data;
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
		} 
	}
}
