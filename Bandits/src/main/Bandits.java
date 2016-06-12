package main;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import api.Mouse;
import api.Timer;

import java.awt.*;

@ScriptManifest(name="Bandits", author="iMKitty", info="AFKs bandits", version=1.5, logo="")

public class Bandits extends Script {
	private Timer timer;
	private Mouse m;
	private NPC tiles;
    private int foodid, noteid, nextHP;
    Position kappa = new Position(3185, 2987, 0);
    Position kappaHD = new Position(3182, 2984, 0);
    Area keepo = new Area(3175, 2988, 3178, 2986);
    
    @Override
    public void onStart() {
		timer = new Timer(System.currentTimeMillis());
		m = new Mouse(this);
		foodid = 7946;
		noteid = 7947;
		nextHP = random(20, 55);
		log("Eating next at : " + nextHP);
    }
    
    @Override
    public void onExit() {
    }
    
    public enum State {EAT, EXCHANGE, IDLE}

    public State getState() {
	    if (skills.getDynamic(Skill.HITPOINTS) <= nextHP) {
	    	return State.EAT;
        } else if (!getInventory().contains(foodid)) {
            return State.EXCHANGE;
        } else {
        	return State.IDLE;
        }
    }
    
    @Override
    public int onLoop() throws InterruptedException {
		
        switch (getState()) {
			default:
				break;
			case IDLE:
				if (!myPlayer().getPosition().equals(kappa)) {
					kappa.interact(getBot(), "Walk here");
				}
				Script.sleep(Script.random(8000, 15000));;
				break;
			case EAT:
				log("EAT");
				while (skills.getDynamic(Skill.HITPOINTS) <= skills.getStatic(Skill.HITPOINTS) - 15) {
					getInventory().interact("Eat", foodid);
					Script.sleep(Script.random(1500, 3000));;
				}
				nextHP = random(20, 55);
				log("Eating next at : " + nextHP);
				break;
			case EXCHANGE:
			    log("EXCHANGE");
			    RS2Object curtain = objects.closest("Curtain");
			    if (curtain != null && curtain.hasAction("Open")) {
			    	curtain.interact("Open");
			    }
				if (!myPlayer().getPosition().equals(kappaHD)) {
					kappaHD.interact(getBot(), "Walk here");
				}
			    if (curtain != null && curtain.hasAction("Close")) {
			    	curtain.interact("Close");
			    }
		    	getWalking().walk(keepo);
		    	tiles = npcs.closest("Tiles");
		    	Script.sleep(Script.random(1000, 1500));;
		    	if (tiles != null) {
		    		if (getInventory().isItemSelected()) {
		    			tiles.interact("Use");
		    			dialogues.selectOption(2);
		    		} else {
		    			getInventory().interact("Use", noteid);
		    		}
		    	}
		    	
		    	if (getInventory().contains(foodid)) {
					if (!myPlayer().getPosition().equals(kappaHD)) {
						kappaHD.interact(getBot(), "Walk here");
					}
				    if (curtain != null && curtain.hasAction("Open")) {
				    	curtain.interact("Open");
				    }
					getWalking().walk(kappa);
		    	}
			break;
        }
        return 300;
    }

    @Override
    public void onPaint(Graphics2D g) {
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
    	m.draw(g);
    	g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 315);
    	Skill[] skills = { Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.RANGED, Skill.MAGIC };
		int totalXP = experienceTracker.getGainedXP(skills[0]) + experienceTracker.getGainedXP(skills[1]) + experienceTracker.getGainedXP(skills[2]) + experienceTracker.getGainedXP(skills[3]) + experienceTracker.getGainedXP(skills[4]);
		int xpPH =  experienceTracker.getGainedXPPerHour(skills[0]) + experienceTracker.getGainedXPPerHour(skills[1]) + experienceTracker.getGainedXPPerHour(skills[2]) + experienceTracker.getGainedXPPerHour(skills[3]) + experienceTracker.getGainedXPPerHour(skills[4]);
		g.drawString("Experience Gained: " + totalXP + " (" + xpPH + "k/ph)", 10, 330);
    }

}