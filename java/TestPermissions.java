import java.net.SocketPermission;
import java.util.Properties;
import java.util.Set;

/**
 * This class is testing the java.security.AllPermission directive of the java.policy file
 *
 * Find on your System the file java.policy and add following lines for recursively
 * directory access to it:
 * 
 * grant codeBase "file:/home/user/workingspace/-" {
 *   permission java.security.AllPermission;
 * };
 *
 */
public class TestPermissions {

  public static void main(String[] args) {

    SecurityManager secManager = new SecurityManager();
    SocketPermission perm = new SocketPermission("localhost:1024-", "accept,connect,listen");
    if ( secManager != null )
      secManager.checkPermission(perm);
    System.setSecurityManager(secManager);

    Properties props = System.getProperties();
    Set<String> keys = props.stringPropertyNames();

    for( String o : keys )
      System.out.println(o+" = "+props.getProperty(o));

  }
}

