package randomness;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {

		Numbers numbers = null;

		if (args.length == 0) {
			System.out.println("[Main process] Starting with default parameters");
			System.out.println("[Main process] numbers: 6 ceiling: 45 tips: " + Integer.MAX_VALUE);
			numbers = new Numbers(6, 45, Integer.MAX_VALUE);
		} else if (args.length == 1) {
			System.out.println("[Main process] Starting with given parameters");
			System.out.println("[Main process] numbers: 6 ceiling: 45 tips: " + args[0]);
			try {
				numbers = new Numbers(6, 45, Integer.parseInt(args[0]));
			} catch (Exception e) {
				System.err.println("Wrong number format!");
				System.exit(1);
			}
		}

		try {
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(numbers));
			System.out.println("[Main process] Shutdown hook added");
		} catch (Throwable t) {
			System.out.println("[Main process] Could not add Shutdown hook");
			System.exit(1);
		}

		numbers.run();

		System.exit(0);
	}
}

// The ShutdownThread is the thread we pass to the
// addShutdownHook method
class ShutdownThread extends Thread {
	private Thread shutdown = null;
	private Date start, end;

	public ShutdownThread(Thread shutdown) {
		super();
		start = new Date();
		this.shutdown = shutdown;
	}

	public void run() {
		end = new Date();
		System.out.println("[Shutdown thread] Shutting down");
		((Numbers) shutdown).stopThread();
		System.out.println("[Shutdown thread] Thread was running for "
				+ millisToShortDHMS(end.getTime() - start.getTime()));
	}

	/**
	 * converts time (in milliseconds) to human-readable format "<dd:>hh:mm:ss"
	 */
	private String millisToShortDHMS(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
		if (days == 0) {
			res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
		}
		return res;
	}
}