package tasks;

import java.awt.event.KeyEvent;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;

import framework.Node;
import data.Data;

public class Stamina implements Node {

	private Script s;
	private Player me;
	private Inventory inv;
	private Data data;
	
	public Stamina(Script s, Data data) {
		this.s = s;
		this.me = s.myPlayer();
		this.inv = s.getInventory();
		this.data = data;
	}
	
	@Override
	public boolean validate() {
		return data.b.contains(me) 
				&& data.getStaminaUsage() 
				&& s.settings.getRunEnergy() <= 60 
				&& !inv.isFull();
	}

	@Override
	public void run() throws InterruptedException {
		s.log("STAMINA OPERATION");
		RS2Object bankBooth = s.objects.closest("Bank booth");
		if (bankBooth != null) {
  			bankBooth.interact("Bank");
  	        new ConditionalSleep(10000) {
  	            @Override
  	            public boolean condition() throws InterruptedException {
  	                return s.getBank().isOpen();
  	            }
  	        }.sleep();
	  		if (s.getBank().isOpen()) {
	  			if (s.getBank().contains(data.STAM) ){
	  				s.getBank().withdraw(data.STAM, 1);
	  			} else if (s.getBank().contains(data.STAM2)){
	  				s.getBank().withdraw(data.STAM2, 1);
	  			} else if (s.getBank().contains(data.STAM3)){
	  				s.getBank().withdraw(data.STAM3, 1);
	  			} else if (s.getBank().contains(data.STAM4)){
	  				s.getBank().withdraw(data.STAM4, 1);
	  			}
	  			
	  			s.getKeyboard().typeKey((char)KeyEvent.VK_ESCAPE);
	  	        new ConditionalSleep(10000) {
	  	            @Override
	  	            public boolean condition() throws InterruptedException {
	  	                return !s.getBank().isOpen();
	  	            }
	  	        }.sleep();
	  			
	  			if (s.getInventory().contains(data.STAM)) {
	  				s.getInventory().interact("Drink", data.STAM);
	  			} else if (inv.contains(data.STAM2)) {
	  				s.getInventory().interact("Drink", data.STAM2);
	  			} else if (inv.contains(data.STAM3)) {
	  				s.getInventory().interact("Drink", data.STAM3);
	  			} else if (inv.contains(data.STAM4)) {
	  				s.getInventory().interact("Drink", data.STAM4);
	  			}
	  			Script.sleep(Script.random(1200, 1500));
	  			if (s.getInventory().contains(data.VIAL)) {
	  				inv.getItem(data.VIAL).interact("Drop");
	  			} else {
		  			bankBooth.interact("Bank");
		  	        new ConditionalSleep(10000) {
		  	            @Override
		  	            public boolean condition() throws InterruptedException {
		  	                return s.getBank().isOpen();
		  	            }
		  	        }.sleep();
		  	        if (s.getInventory().contains(data.STAM3)) {
		  	        	s.getBank().deposit(data.STAM3, 1);
		  	        } else if (s.getInventory().contains(data.STAM2)){
		  	        	s.getBank().deposit(data.STAM2, 1);
		  	        } else if (s.getInventory().contains(data.STAM)) {
		  	        	s.getBank().deposit(data.STAM, 1);
		  	        }
	  			}
	  			
	  		} else {
	  			bankBooth.interact("Bank");
	  	        new ConditionalSleep(10000) {
	  	            @Override
	  	            public boolean condition() throws InterruptedException {
	  	                return s.getBank().isOpen();
	  	            }
	  	        }.sleep();
  			}
		}
	}
}
