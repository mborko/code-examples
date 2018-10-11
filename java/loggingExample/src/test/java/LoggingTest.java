import logging.Logging;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoggingTest {
    @Test public void testSomeLibraryMethod() {
        Logging classUnderTest = new Logging();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.doSomething());
    }
}
