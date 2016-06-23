package main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.text.NumberFormat;
import java.util.Locale;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;

import api.Mouse;
import api.Timer;

@ScriptManifest(author = "iMKitty", info = "Kills Monkeys", name ="KingKong", version = 1.0, logo = "https://i.gyazo.com/22b0a50a34de9bc1135c1f7427b959a0.png")
public class KingKong extends Script {
    String state;
	private Timer timer;
	private Mouse m;
	
	public static int[] Worlds = {2, 3, 4, 5, 6, 9, 10, 11,
		12, 13, 14, 18, 19, 20, 22, 27, 28, 29, 30, 33, 34, 36, 38, 41,
		42, 43, 44, 46, 50, 51, 52, 54, 57, 58, 59, 60, 62, 67, 68, 69, 70,
		74, 75, 76, 77, 78, 86};
	public static int worldCounter = 0;
			
	int hunterXP, hxph, currentWorld;
	RS2Object boulder;
	public String status = "Getting to work";
	int TAIL = 19665, BONE = 526, BANANA = 1963, TABS = 8014;
	Area boulderArea = new Area(2912, 9128, 2912, 9127);
	public void onStart() {
		experienceTracker.start(Skill.HUNTER);
		m = new Mouse(this);
		timer = new Timer(System.currentTimeMillis());
		currentWorld = worlds.getCurrentWorld() - 300;
		for (int i = 0; i < Worlds.length; i++) {
			if (currentWorld == Worlds[i]) {
				worldCounter = i+1;
				log("Starting on world: " + Worlds[i]);
			}
		}
    }
	
	public void onPaint(Graphics2D g) {
		m.draw(g);
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		g.drawString("KingKong", 10, 290);
		g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 305);
		hunterXP = experienceTracker.getGainedXP(Skill.HUNTER);
		hxph =  experienceTracker.getGainedXPPerHour(Skill.HUNTER);
		g.drawString("Hunter Experience Gained: " + hunterXP + " (" + NumberFormat.getNumberInstance(Locale.US).format(hxph) + "xp/h)", 10, 320);
	}
	
	boolean onlyMe() {
		for (Player p : getPlayers().getAll()) {
			if (p != null && !p.equals(myPlayer())) {
				return false;
			}
		}
		return true;
	}
	
	private enum State {
    	HUNT, BONES, DROP, LOOT, WORLDHOP, IDLE
    }
    
    private State getState() {
    	boulder = objects.get(2910, 9127).get(0);
    	if (getInventory().contains(19665) && boulder.getId() == 28827) {
    		return State.DROP;
    	} else if (!onlyMe()) {
    		return State.WORLDHOP;
    	} else if (boulder.getId() == 28827) {
    		return State.IDLE;
    	} else if (boulder.getId() == 28824 && getInventory().contains(BANANA)) {
    		return State.HUNT;
    	} else if (boulder.getId() >= 28830) {
    		return State.LOOT;
    	} else if (!getInventory().contains(BANANA) && boulder.getId() != 28827) {
    		return State.BONES;
    	} else {
    		return State.IDLE;
    	}
    }

	public int onLoop() throws InterruptedException {
		if (getCamera().getPitchAngle() < 59) {
    		getCamera().movePitch(random(59, 62) + 5);
    	}
		switch(getState()) {
		case HUNT: // Set-up a boulder trap
			log("HUNT");
			if (boulderArea.contains(myPlayer())) {
				RS2Object b = objects.closest(28824);
				if (b != null) {
					b.interact("Set-trap");
				}
				sleep(random(2500, 4500));
			} else {
				getWalking().webWalk(boulderArea);
			}
			break;
		case BONES:
			log("BONES");
			while (!getInventory().isFull()) {
				GroundItem bone = getGroundItems().closest(BONE);
				if (bone != null) {
					if (bone.isVisible()) {
						bone.interact("Take");
						sleep(random(1000, 3000));
						new ConditionalSleep(10000) {
							@Override
							public boolean condition() {
								return !myPlayer().isMoving();
							}
						}.sleep();
					} else {
						getWalking().walk(bone.getPosition());
						new ConditionalSleep(10000) {
							@Override
							public boolean condition() {
								return !myPlayer().isMoving();
							}
						}.sleep();
					}
				} else {
					getWalking().webWalk(boulderArea);
				}
			}
	    	if (getInventory().isFull()) {
	    		getInventory().interact("Break", TABS);
	    		sleep(random(1500, 3000));
	    	}
			break;
		case DROP:
			log("DROP");
			if (getInventory().contains(TAIL)) {
				getInventory().interact("Drop", TAIL);
			}
			sleep(random(300, 800));
			break;
		case LOOT:
	    	log("LOOT");
	    	RS2Object s = objects.closest("Large boulder");
	    	if (s != null && s.hasAction("Check")) {
	    		s.interact("Check");
	    	}
	    	sleep(random(2000, 3500));
			break;
		case WORLDHOP:
			log("WORLDHOP");
			if (Worlds != null && Worlds[worldCounter] != -1){
				worlds.hop(Worlds[worldCounter]);
				worldCounter++;
			} else {
				log("int array is null");
			}
			if (worldCounter == 46) {
				worldCounter = 0;
			}
			break;
		case IDLE:
			log("IDLE");
			sleep(random(1000, 1500));
			new ConditionalSleep(30000) {
				@Override
				public boolean condition() {
					return boulder.getId() != 28827;
				}
			}.sleep();
			break;
		}
		return 300;
	}
	public void onExit() {
	}
}