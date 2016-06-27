package main;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import api.Mouse;
import api.Timer;

import java.awt.*;

import data.Data;
import framework.Node;
import tasks.*;
import gui.GUI;

@ScriptManifest(name="PJSalt", author="iMKitty", info="Digs salt", version=1.5, logo="")

public class PJSalt extends Script {
	//AREAS

	public static Data data;
	private GUI gui;

	Entity westEntity, southEntity, eastEntity;
	private Timer timer;
	private Mouse m;
	private String status;

	@Override
	public void onStart() throws InterruptedException {
		timer = new Timer(System.currentTimeMillis());
		m = new Mouse(this);
		gui = new GUI();
		status = "Initializing...";
		while (gui.running) {
			sleep(10);
		}
		log("Starting on world + " + worlds.getCurrentWorld());
		data.setCurrentWorld(worlds.getCurrentWorld());
	}
	
	@Override
	public void onExit() {
		this.status = "Exiting...";
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		runTasks(buildTasks());
		return 100;
	}

	private Node[] buildTasks() {
		Node[] tasks = {
				new Bank(this, data), 
				new Stamina(this, data),
				new Walk(this, data),
				new Dig(this, data),
				new WorldHop(this, data),
				};
		return tasks;
	}

	private void runTasks(Node[] tasks) throws InterruptedException {
		if (getCamera().getPitchAngle() < 59) getCamera().movePitch(random(59, 62) + 5);
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].validate()) {
				tasks[i].run();
			}
		}
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		g.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		m.draw(g);
		g.drawString("Salt collected: " + data.counter, 10, 300);
		g.drawString("Timer: " + timer.parse(timer.getElapsed()), 10, 315);
		g.drawString("Status: " + this.status, 10, 330);
	}

}
