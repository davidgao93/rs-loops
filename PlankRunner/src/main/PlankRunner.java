package main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;



import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;

import api.Mouse;
import api.Timer;

@ScriptManifest(author = "iMKitty", info = "Makes Planks", name ="PlankRunner", version = 1.0, logo = "")
public class PlankRunner extends Script {
    String state;
	private Timer timer;
	private Mouse m;
	public String status = "Getting to work";
	int LOG = 1521, OAKPLANK = 8778, 
			STAM = 12631, STAM3 = 12627, STAM2=  12629, STAM4 = 12625,
			VIAL = 229, 
			counter = 0, lapcounter = 0;
	Area b = new Area(1589, 3482, 1591, 3481);
	Area bb = new Area(1589, 3480, 1593, 3476);
	Area saw = new Area(1624, 3501, 1625, 3499);
	
	public void onStart() {
		m = new Mouse(this);
		timer = new Timer(System.currentTimeMillis());
    }
	
	public void onPaint(Graphics2D g) {
		m.draw(g);
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		g.drawString("Plank Runner", 10, 290);
		g.drawString("Planks made: " + counter, 10, 305);
		g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 320);
	}
	private enum State {
    	PLANK, BANK, IDLE
    }
    
    private State getState() {
    	if (getInventory().contains(LOG)) {
    		return State.PLANK;
    	} else if (getInventory().contains(OAKPLANK)){
    		return State.BANK;
    	} else {
    		return State.IDLE;
    	}
    }
   
    private void depositItem(int itemid) throws InterruptedException {
    	while (getInventory().contains(itemid) && getBank().isOpen()) {
	    	switch (random(0, 50)) {
	    	case 10:
	    		getInventory().getItem(itemid).interact("Deposit-1");
	    		break;
	    	case 20:
	    		getBank().deposit(itemid, random(30, 33));
	    		break;
	    	case 30:
	    		getInventory().getItem(itemid).interact("Deposit-10");
	    		break;
	    	case 40:
	    		getInventory().getItem(itemid).interact("Deposit-5");
	    		break;
	    	default:
	    		getInventory().getItem(itemid).interact("Deposit-All");
	    		break;
	    	}
	    	sleep(random(500, 1500));
    	}
    	//getBank().depositAllExcept("Coins");
    }
    
    private boolean mouseToBuyArea() throws InterruptedException {
		mouse.move(random(295, 364), random(155, 171));
        return true;
    }
    
    private void leftClick() {
        mouse.click(false);
    }

	public int onLoop() throws InterruptedException {
		if (getCamera().getPitchAngle() < 59) {
    		getCamera().movePitch(random(59, 62) + 5);
    	}
		switch(getState()) {
		case PLANK:
			log("PLANK");
			status = "Making planks";
			if (random(0,128) == 44) {
				log("Anti-ban hit, waiting for up to 2 minutes");
				sleep(random(25000, 120000));
			}
			if (saw.contains(myPlayer())) {
				NPC operator = npcs.closest("Sawmill operator");
				if (operator != null) {
					operator.interact("Buy-plank");
					sleep(random(800, 2000));
				}
				
				if (mouseToBuyArea()) {
	                leftClick();
	                sleep(random(500, 1500));
				}
				
			} else {
				getWalking().webWalk(saw);
			}
			break;
		case BANK:
			log("BANK");
	    	status = "Banking";
	    	if (b.contains(myPlayer()) || bb.contains(myPlayer())) {
				RS2Object bankBooth = objects.closest("Bank chest");
				Script.sleep(Script.random(1000, 1500));;
				if (!bank.isOpen()) {
					if (bankBooth != null) {
						bankBooth.interact("Use");
						new ConditionalSleep(10000) {
							@Override
							public boolean condition() throws InterruptedException {
								return getBank().isOpen();
							}
						}.sleep();
					}
				} else {
					depositItem(OAKPLANK);
					lapcounter++;
					counter += 27;
					if (settings.getRunEnergy() <= 50 
							&& lapcounter >= 2
							&& (getBank().contains(STAM) 
									|| getBank().contains(STAM2)
									|| getBank().contains(STAM3)
									|| getBank().contains(STAM4))) {
			  			bankBooth.interact("Use");
						new ConditionalSleep(10000) {
							@Override
							public boolean condition() throws InterruptedException {
								return getBank().isOpen();
							}
						}.sleep();
				  		if (getBank().isOpen()) {
				  			if (getBank().contains(STAM) ){
				  				getBank().withdraw(STAM, 1);
				  			} else if (getBank().contains(STAM2)){
				  				getBank().withdraw(STAM2, 1);
				  			} else if (getBank().contains(STAM3)){
				  				getBank().withdraw(STAM3, 1);
				  			} else if (getBank().contains(STAM4)){
				  				getBank().withdraw(STAM4, 1);
				  			}
				  			
				  			getKeyboard().typeKey((char)KeyEvent.VK_ESCAPE);
				  	        new ConditionalSleep(10000) {
				  	            @Override
				  	            public boolean condition() throws InterruptedException {
				  	                return !getBank().isOpen();
				  	            }
				  	        }.sleep();
				  			
				  			if (getInventory().contains(STAM)) {
				  				getInventory().interact("Drink", STAM);
				  			} else if (getInventory().contains(STAM2)) {
				  				getInventory().interact("Drink", STAM2);
				  			} else if (getInventory().contains(STAM3)) {
				  				getInventory().interact("Drink", STAM3);
				  			} else if (getInventory().contains(STAM4)) {
				  				getInventory().interact("Drink", STAM4);
				  			}
				  			Script.sleep(Script.random(1200, 1500));
				  			if (getInventory().contains(VIAL)) {
				  				getInventory().getItem(VIAL).interact("Drop");
				  			} else {
					  			bankBooth.interact("Use");
					  	        new ConditionalSleep(10000) {
					  	            @Override
					  	            public boolean condition() throws InterruptedException {
					  	                return getBank().isOpen();
					  	            }
					  	        }.sleep();
					  	        if (getInventory().contains(STAM3)) {
					  	        	getBank().deposit(STAM3, 1);
					  	        } else if (getInventory().contains(STAM2)){
					  	        	getBank().deposit(STAM2, 1);
					  	        } else if (getInventory().contains(STAM)) {
					  	        	getBank().deposit(STAM, 1);
					  	        }
				  			}
				  		}
					}
				}
	    	} else {
	    		getWalking().webWalk(b);
	    	}
			break;
		case IDLE:
			log("IDLE");
			if (b.contains(myPlayer()) || bb.contains(myPlayer())) {
				if (!bank.isOpen()) {
					RS2Object bankBooth = objects.closest("Bank chest");
					if (bankBooth != null) {
						bankBooth.interact("Use");
						new ConditionalSleep(3000, 100) {
							@Override
							public boolean condition() throws InterruptedException {
								return getBank().isOpen();
							}
						}.sleep();
					}
				} else {
					if (random(0, 100) <= 50) {
						bank.withdraw(LOG, 30);
					} else {
						bank.withdrawAll(LOG);
					}
				}
			} else {
				getWalking().webWalk(b);
			}
			break;
		}
		return 300;
	}
	public void onExit() {
		log("Planks made: " + counter + " in " + timer.parse(timer.getElapsed()));
	}
}