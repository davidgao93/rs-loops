package api;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.osbot.rs07.script.Script;

public class Mouse {

	private Script s;

	private final BasicStroke TWO_STROKE = new BasicStroke(2);
	private final BasicStroke FIVE_STROKE = new BasicStroke(5);
	private final Color BLACK_COLOR = new Color(255, 255, 255);
	private final Color WHITE_COLOR = new Color(255, 255, 255, 15);

	public Mouse(Script s) {
		this.s = s;
	}

	public void draw(final Graphics2D g) {
		final Point point = s.mouse.getPosition();
		final int x = point.x;
		final int y = point.y;
		g.setColor(WHITE_COLOR);
		g.setStroke(FIVE_STROKE);
		g.drawLine(0, 504 - (504 - y), 765, 504 - (504 - y));
		g.drawLine(765 - (765 - x), 0, 765 - (765 - x), 504);
		g.setColor(BLACK_COLOR);
		g.setStroke(TWO_STROKE);
		g.drawLine(0, 504 - (504 - y), 765, 504 - (504 - y));
		g.drawLine(765 - (765 - x), 0, 765 - (765 - x), 504);
		g.setColor(Color.WHITE);

	}
}