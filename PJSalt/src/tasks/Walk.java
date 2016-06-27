package tasks;

import org.osbot.rs07.script.Script;
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
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	@Override
	public void run() throws InterruptedException {
		s.log("WALK OPERATION");
		NPC konoo = s.npcs.closest("Konoo");
		if (konoo != null) {
			if (!konoo.isVisible()) {
				s.getWalking().webWalk(konoo.getArea(2));
			}
		} else {
			s.getWalking().webWalk(data.south);
		}
	}
}
