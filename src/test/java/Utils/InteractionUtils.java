package Utils;

public class InteractionUtils {

	private InteractionUtils() {
		// cannot initialize
	}

	public static void explicitWait(long wait_millis) {
		try {
			Thread.sleep(wait_millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
