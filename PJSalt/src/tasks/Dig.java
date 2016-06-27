package tasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;

import framework.Node;
import data.Data;

public class Dig implements Node {
	
	private Script s;
	private Player me;
	private Data data;
	
	public Dig(Script s, Data data) {
		this.s = s;
		this.me = s.myPlayer();
		this.data = data;
	}
	
	@Override
	public boolean validate() {
		if (s.getInventory().contains(data.SPADE)) {
			RS2Object saltPile = s.getObjects().closest("Saltpetre");
			if (saltPile != null) {
				if (saltPile.isVisible()) {
					return true;
				}
			} else {
				return false;
			}
		} else {
			s.log("SPADE NOT FOUND");
		}
		return false;
	}
	
	@Override
	public void run() throws InterruptedException {
		data.setStatus("Digging");
		RS2Object saltPile = s.getObjects().closest("Saltpetre");
		if (saltPile.isVisible() && !me.isAnimating()) {
			saltPile.interact("Dig");
            new ConditionalSleep(Script.random(5000, 7000)) {
                @Override
                public boolean condition() throws InterruptedException {
                    return s.getInventory().isFull();
                }
            }.sleep();
		} else {
			Script.sleep(Script.random(1500, 3000));
		}
		
	}
}
