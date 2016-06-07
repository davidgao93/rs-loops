package main;

import java.awt.Font;
import java.awt.Graphics2D;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.api.ui.Spells;

import api.Mouse;
import api.Timer;

@ScriptManifest(author = "iMKitty", info = "Makes Glass", name ="Alice", version = 1.0, logo = "http://i2.cdn.turner.com/cnn/dam/assets/140311114125-joska-glassblowing-horizontal-gallery.png")
public class Glass extends Script {
    String state;
    int counter;
	private Timer timer;
	private Mouse m;
	
	public void onStart() {
		m = new Mouse(this);
		timer = new Timer(System.currentTimeMillis());
    }
	
	public void onPaint(Graphics2D g) {
		m.draw(g);
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		g.drawString("Alice", 10, 300);
		g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 315);
		g.drawString("Glass made: " + counter, 10, 330);
	}
	private enum State {
    	MAKE, BANK, IDLE
    }
    
    private State getState() {
    	if (getInventory().contains("Bucket of sand")) {
    		return State.MAKE;
    	} else if (getInventory().contains("Molten glass")){
    		return State.BANK;
    	} else {
    		return State.IDLE;
    	}
    }

    public String status = "Getting to work";

	public int onLoop() throws InterruptedException {
		switch(getState()) {
		case MAKE:
			status = "Making glass";
            getTabs().open(Tab.MAGIC);
            sleep(random(50, 100));
            getMagic().castSpell(Spells.LunarSpells.SUPERGLASS_MAKE);
            sleep(random(1000, 1500));
		case BANK:
			RS2Object bankBooth = objects.closest("Bank booth");
	    	status = "Banking";
	    	getWalking().walk(bankBooth.getPosition());
	    	if (bankBooth != null) {
				if (getBank().isOpen()) {
			      	getInventory().getItem("Molten glass").interact("Deposit-All");
			      	counter += 13;
			      	sleep(random(1000, 1500));
			      	getBank().withdraw("Bucket of sand", 13);
			      	sleep(random(500, 1000));
			      	getBank().withdraw("Seaweed", 13);
			      	getBank().close();
				} else {
					bankBooth.interact("Bank");
				}
	    	}
			break;
			
		case IDLE:
			break;
		}
		return 300;
	}
	public void onExit() {
	}
}