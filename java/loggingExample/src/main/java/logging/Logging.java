package logging;

import java.util.logging.*;

/*
 * 
 * Logging example
 * 
 * https://docs.oracle.com/javase/8/docs/api/java/util/logging/package-summary.html
 * https://docs.oracle.com/javase/8/docs/technotes/guides/logging/overview.html
 * http://www.vogella.com/tutorials/Logging/article.html
 * 
 * @author Michael Borko <michael@borko.at>
 * 
 */
public class Logging {

	public final static Logger log = Logger.getAnonymousLogger();
	
	public static void main(String[] args) {
		log.setLevel(Level.FINE);

	}

	public boolean doSomething() {
		return true;
	}
}
