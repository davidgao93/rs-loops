package main;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import api.Mouse;
import api.Timer;

import java.awt.*;


@ScriptManifest(name="PJSalt", author="iMKitty", info="Digs salt", version=1.5, logo="")

public class PJSalt extends Script {

	//AREAS
    Area b = new Area(1674, 3564, 1676, 3559);
    Area west = new Area(1665, 3549, 1673, 3543);
    Area south = new Area(1683, 3533, 1691, 3527);
    Area east = new Area(1705, 3550, 1713, 3543);
    
    public static int[] DMMWorlds = {2, 3, 4, 5, 6, 9, 10, 11,
    	12, 13, 14, 18, 19, 20, 22, 27, 28, 29, 30, 33, 34, 36, 38, 41,
    	42, 43, 44, 46, 50, 51, 52, 54, 57, 58, 59, 60, 62, 67, 68, 69, 70,
    	74, 75, 76, 77, 78, 86};
    public static int worldCounter = 0;
    public static int STAMINA_ID = 12631, DSTAMINA_ID = 229;
    
    Entity 	westEntity, southEntity, eastEntity;
    
	private Timer timer;
	private Mouse m;
    private String status;
    int counter, currentWorld;
    RS2Object saltPile;
    
    @Override
    public void onStart() {
		timer = new Timer(System.currentTimeMillis());
		m = new Mouse(this);
		westEntity = getObjects().closest(west, "Saltpetre");
		southEntity = getObjects().closest(south, "Saltpetre");
		eastEntity = getObjects().closest(east, "Saltpetre");
		status = "Initializing...";
        currentWorld = worlds.getCurrentWorld() - 300;
        for (int i = 0; i < DMMWorlds.length; i++) {
        	if (currentWorld == DMMWorlds[i]) {
        		worldCounter = i+1;
        		log("Starting on world: " + DMMWorlds[i]);
        	}
        }
        counter = 0;
    }
    
    @Override
    public void onExit() {
        this.status = "Exiting...";
    }
    
    public enum State {WALK, DIG, BANK, WORLDHOP, IDLE}

    public State getState() {
	    if (westEntity == null && southEntity == null && eastEntity == null && !getInventory().isFull()) {
	    	return State.WORLDHOP;
	    } else if (getInventory().isFull()) {
            return State.BANK;
        } else if (!getInventory().isFull() && (!west.contains(myPlayer()) || !south.contains(myPlayer()) || !east.contains(myPlayer()))) {
            return State.WALK;
        } else if (west.contains(myPlayer()) || south.contains(myPlayer()) || east.contains(myPlayer())) {
            return State.DIG;
        } else {
        	return State.IDLE;
        }
    }
    
    @Override
    public int onLoop() throws InterruptedException {
		westEntity = getObjects().closest(west, "Saltpetre");
		southEntity = getObjects().closest(south, "Saltpetre");
		eastEntity = getObjects().closest(east, "Saltpetre");
		if (getCamera().getPitchAngle() < 59) {
    		getCamera().movePitch(random(59, 62) + 5);
    	}
		
        switch (getState()) {
			default:
				break;
			case IDLE:
				log("IDLE");
				break;
			case WALK:
				log("WALK");
				status = "Walking";
				if (eastEntity != null) {
					getWalking().webWalk(east);
				} else if (southEntity != null) {
					getWalking().webWalk(south);
				} else {
					getWalking().webWalk(west);
				}
				Script.sleep(Script.random(1000, 1500));;
            case DIG:
                log("DIG");
                status = "Digging";
                if (west.contains(myPlayer())) {
                	saltPile = objects.closest(west, "Saltpetre");
                } else if (east.contains(myPlayer())) {
                	saltPile = objects.closest(east, "Saltpetre");
                } else if (south.contains(myPlayer())) {
                	saltPile = objects.closest(south, "Saltpetre");
                } else {
                	saltPile = null;
                }
                getTabs().open(Tab.INVENTORY);
                if (saltPile != null && !getInventory().isFull()) {
                	saltPile.interact("Dig");
					for (int i = 0; i < Script.random(20, 35); i++) {
						if (saltPile != null && !getInventory().isFull()) {
							Script.sleep(Script.random(1000, 1500));;
						}
					}
                }
        		break;
                
		case BANK:
		    log("BANK");
		    status = "Banking";
	    	getWalking().webWalk(b);
	    	RS2Object bankBooth = objects.closest("Bank booth");
	    	Script.sleep(Script.random(1000, 1500));;
            if (!bank.isOpen()) {
                bankBooth.interact("Bank");
                new ConditionalSleep(1000, 100) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return getBank().isOpen();
                    }
                }.sleep();
            } else {
			    bank.depositAllExcept("Spade");
                new ConditionalSleep(1000, 100) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return !getInventory().isFull();
                    }
                }.sleep();
                if (settings.getRunEnergy() <= 60) {
                	if (getBank().contains(STAMINA_ID)) {
	                	getBank().withdraw(STAMINA_ID, 1);
	                	getBank().close();
	    	  	        new ConditionalSleep(1000, 100) {
	    	  	            @Override
	    	  	            public boolean condition() throws InterruptedException {
	    	  	                return !getBank().isOpen();
	    	  	            }
	    	  	        }.sleep();
		  	        	if (getInventory().contains(STAMINA_ID)) {
		  	  				getInventory().interact("Drink", STAMINA_ID);
		  	  				Script.sleep(Script.random(1200, 1500));
	  	  				}
		  	  			if (getInventory().contains(DSTAMINA_ID)) {
		  	  				getInventory().getItem(DSTAMINA_ID).interact("Drop");
		  	  			}
	                }
                }
		      	counter += 27;
            }
			break;
			
        case WORLDHOP:
        	log("WORLDHOP");
        	Script.sleep(Script.random(4000, 5500));;
        	status = "Hopping";
        	if (DMMWorlds != null && DMMWorlds[worldCounter] != -1){
				worlds.hop(DMMWorlds[worldCounter]);
				worldCounter++;
			} else {
				log("int array is null");
			}
			if (worldCounter == 46){
				worldCounter = 0;
			}
        	break;
        }
        return 300;
    }


    @Override
    public void onPaint(Graphics2D g) {
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
    	m.draw(g);
    	g.drawString("Salt collected: " + counter, 10, 300);
    	g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 315);
        g.drawString("Status: " + this.status, 10, 330);
    }

}