package api;

public class Timer {

	private long start;
	private long end;
	private long period;

	public Timer(final long period) {
		start = System.currentTimeMillis();
		this.period = period;
		end = start + period;
	}

	public long getElapsed() {
		return System.currentTimeMillis() - start;
	}

	public long getRemaining() {
		return end - System.currentTimeMillis();
	}

	public boolean isRunning() {
		return System.currentTimeMillis() < end;
	}

	public void reset() {
		start = System.currentTimeMillis();
		end = start + period;
	}
	
	public String parse(long millis) {
		String time = "n/a";
		if (millis > 0) {
			int seconds = (int) (millis / 1000) % 60;
			int minutes = (int) ((millis / (1000 * 60)) % 60);
			int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
			time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		return time;
	}

}
