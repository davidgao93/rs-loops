package main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Locale;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Option;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;

import api.Mouse;
import api.Timer;

@ScriptManifest(author = "iMKitty", info = "Makes Glass", name ="Alice", version = 1.0, logo = "http://i2.cdn.turner.com/cnn/dam/assets/140311114125-joska-glassblowing-horizontal-gallery.png")
public class Glass extends Script {
    String state;
	private Timer timer;
	private Mouse m;
	int magicXP, craftXP, mxpPH, cxpPH;
	RS2Object bankBooth;
	public String status = "Getting to work";
	int CLAY = 434, SOFTCLAY = 1761, SAND = 1783, SEAWEED = 401, GLASS = 1775,
			SMOKE = 11998, STEAM = 11787, ASTRAL = 9075;
	
	public void onStart() {
		experienceTracker.startAll();
		m = new Mouse(this);
		timer = new Timer(System.currentTimeMillis());
		bankBooth = objects.closest("Bank chest");
    }
	
	public void onPaint(Graphics2D g) {
		m.draw(g);
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		g.drawString("Alice", 10, 290);
		g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 305);
		magicXP = experienceTracker.getGainedXP(Skill.MAGIC);
		craftXP = experienceTracker.getGainedXP(Skill.CRAFTING);
		mxpPH =  experienceTracker.getGainedXPPerHour(Skill.MAGIC);
		cxpPH = experienceTracker.getGainedXPPerHour(Skill.CRAFTING);
		g.drawString("Magic Experience Gained: " + magicXP + " (" + NumberFormat.getNumberInstance(Locale.US).format(mxpPH) + "xp/h)", 10, 320);
		g.drawString("Crafting Experience Gained: " + craftXP + " (" + NumberFormat.getNumberInstance(Locale.US).format(cxpPH) + "xp/h)", 10, 335);
	}
	private enum State {
    	GLASSMAKE, CLAY, BANK, BANKCLAY, IDLE
    }
    
    private State getState() {
    	if (getInventory().contains(SAND) && getInventory().contains(SEAWEED)) {
    		return State.GLASSMAKE;
    	} else if (getInventory().contains(GLASS)){
    		return State.BANK;
    	} else {
    		return State.IDLE;
    	}
    }
    
    private boolean mouseToGlassArea() throws InterruptedException {
		mouse.move(random(709, 724), random(273, 293));
        return isGlassOption();
    }
    
    private boolean mouseToHumidifyArea() throws InterruptedException {
    	mouse.move(random(556, 577), random(243, 264));
    	return isHumidifyOption();
    }
    
    private boolean isGlassOption() {
        for (Option option : menu.getMenu()) {
            if (option != null && option.action.equals("Cast") && option.name.contains("Superglass Make")) {
        		return true;
            }
        }
        return false;
    }
    
    private boolean isHumidifyOption() {
    	for (Option option : menu.getMenu()) {
    		if (option != null && option.action.equals("Cast") && option.name.contains("Humidify")) {
					return true;
			}
		}
    	return false;
    }
    
    private void leftClick() {
        mouse.click(false);
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
    	getBank().depositAllExcept(SMOKE, STEAM, ASTRAL);
    }
    
    private void glassBank() throws InterruptedException {
    	depositItem(GLASS);
      	sleep(random(500, 1000));
		if (getBank().contains(SEAWEED) && getBank().contains(SAND)) {
	      	if (random(0, 100) < 50) {
	      		getBank().withdraw(SAND, 13);
	      		if (random(0, 100) < 90) {
	      			getBank().withdraw(SEAWEED, 13);
	      		} else {
	      			getBank().withdrawAll(SEAWEED);
	      		}
	      	} else {
	      		getBank().withdraw(SEAWEED, 13);
	      		if (random(0, 100) < 90) {
	      			getBank().withdraw(SAND, 13);
	      		} else {
	      			getBank().withdrawAll(SAND);
	      		}
	      	}
		}
    }
    
    private void clayBank() throws InterruptedException {
    	depositItem(SOFTCLAY);
    	sleep(random(500, 1000));
    	if (getBank().contains(CLAY)) {
			if (random(0, 50) == 1) {
	      		glassBank();
			} else {
				if (random(0, 50) == 1) {
					getBank().withdraw(CLAY, random(26, 33));
				} else {
					getBank().withdrawAll(CLAY);
				}
			}
    	} else {
    		glassBank();
    	}
    }

	public int onLoop() throws InterruptedException {
		switch(getState()) {
		case GLASSMAKE:
			log("GLASSMAKE");
			status = "Making glass";
			if (getBank().isOpen()) {
				//getBank().close();
				getKeyboard().typeKey((char)KeyEvent.VK_ESCAPE);
			}
			if (getInventory().contains(11998)) {
				getInventory().interact("Wield", 11998);
			}
			if (!tabs.getOpen().equals(Tab.MAGIC)) {
				tabs.open(Tab.MAGIC);
			}
			if (mouseToGlassArea()) {
                leftClick();
                sleep(random(2500, 4000));
			}
			if (random(0,128) == 44) {
				log("Anti-ban hit, waiting for up to 2 minutes");
				sleep(random(25000, 120000));
			}
			break;
		case BANK:
			log("BANK");
	    	status = "Banking";
	    	if (bankBooth != null && getBank().isOpen()) {
	    		glassBank();
	    	} else {
	    		bankBooth.interact("Use");
				sleep(random(500,1000));
	    	}
			break;
		case CLAY:
			log("CLAY");
			status = "Humidifying clay";
			if (getBank().isOpen()) {
				getBank().close();
			}
			if (getInventory().contains(11787)) {
				getInventory().interact("Wield", 11787);
			}
			if (!tabs.getOpen().equals(Tab.MAGIC)) {
				tabs.open(Tab.MAGIC);
			}
			if (mouseToHumidifyArea()) {
                leftClick();
                sleep(random(2500, 4000));
			}
			if (random(0, 128) == 44) {
				log("Anti-ban hit, waiting for up to 2 minutes");
				sleep(random(25000, 120000));
			}
			break;
		case BANKCLAY:
	    	status = "Banking";
	    	log("BANKCLAY");
	    	if (bankBooth != null && getBank().isOpen()) {
	    		clayBank();
	    	} else {
	    		bankBooth.interact("Use");
				sleep(random(500,1000));
	    	}
			break;
		case IDLE:
			log("IDLE");
			break;
		}
		return 300;
	}
	public void onExit() {
	}
}