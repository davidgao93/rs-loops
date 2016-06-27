package tasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;

import framework.Node;
import data.Data;

public class Bank implements Node {

	private Script s;
	private Player me;
	private Inventory inv;
	private Data data;
	
	public Bank(Script s, Data data) {
		this.s = s;
		this.me = s.myPlayer();
		this.inv = s.getInventory();
		this.data = data;
	}
	
	@Override
	public boolean validate() {
		return inv.isFull();
	}

	@Override
	public void run() throws InterruptedException {
		s.log("BANK OPERATION");
        s.getWalking().webWalk(data.b);
		new ConditionalSleep(10000) {
			@Override
			public boolean condition() {
				return !me.isMoving();
			}
		}.sleep();
		RS2Object bankBooth = s.objects.closest("Bank booth");
		bankBooth.interact("Bank");
        new ConditionalSleep(10000) {
            @Override
            public boolean condition() throws InterruptedException {
                return s.getBank().isOpen();
            }
        }.sleep();
    	if (bankBooth != null) {
			if (s.getBank().isOpen()) {
		      	depositItem(data.SALT);
                new ConditionalSleep(10000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return !s.getInventory().isFull();
                    }
                }.sleep();
		      	data.counter += 27;
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
	
    private void depositItem(int itemid) throws InterruptedException {
    	while (inv.contains(itemid) && s.getBank().isOpen()) {
	    	switch (Script.random(0, 50)) {
	    	case 10:
	    		inv.getItem(itemid).interact("Deposit-1");
	    		break;
	    	case 20:
	    		s.getBank().deposit(itemid, Script.random(30, 33));
	    		break;
	    	case 30:
	    		inv.getItem(itemid).interact("Deposit-10");
	    		break;
	    	case 40:
	    		inv.getItem(itemid).interact("Deposit-5");
	    		break;
	    	default:
	    		inv.getItem(itemid).interact("Deposit-All");
	    		break;
	    	}
	    	Script.sleep(Script.random(600, 1100));
    	}
    	if(s.getBank().isOpen()) {
    		s.getBank().close();
    	}
    }
}
