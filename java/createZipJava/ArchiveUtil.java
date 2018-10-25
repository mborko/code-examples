import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import sun.security.krb5.Checksum;
//import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class ArchiveUtil {

  // INPUT PARAMS:
  // List<String[]>
  // String[]:  [0] => filepath in zip file;  [1] => filepath on file system
  // e.q.:      [0] => embide/Directory/Hallowelt.txt [1] => /var/embide/working_copies/1/Directory[...]

  public static Object createZip(List<String[]> filepaths) throws IOException{

    String result="";
    byte[] buf = new byte[4096];
    File x = File.createTempFile("prefix", "zip");

    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(x));

    for(int i=0; i<filepaths.size(); i++){
      System.out.println("##### FILE: " + filepaths.get(i)[0] + "####### AT: " +filepaths.get(i)[1] +" #########");

      FileInputStream in = new FileInputStream(filepaths.get(i)[1]); // Add ZIP entry to output stream. out.putNextEntry(new ZipEntry(filenames[i]))
      // Add ZIP entry to output stream.

      out.putNextEntry(new ZipEntry(filepaths.get(i)[0])); // Transfer bytes from the file to the ZIP file

      int len;

      while ((len = in.read(buf)) > 0) { out.write(buf, 0, len); }
      // Complete the entry
      out.closeEntry();
      in.close();
    }

    out.close();

    System.out.println("##### FINAL FILE LENGTH: "+x.length()+" ########");

    ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);

    FileInputStream in1 = new FileInputStream(x);

    int len;

    while ((len = in1.read(buf)) > 0) { bout.write(buf, 0, len); }

    System.out.println("###### ByteArr " + bout.toByteArray()+ " ##########");

    //System.out.println("###### ByteArrStr " + new String(bout.toByteArray())+ " ##########");

    result = new String(bout.toByteArray());
    byte[] resultBytes = bout.toByteArray();

    bout.close();

    System.out.println("##### FINAL FILE CONTENT: "+result+" #######");
    System.out.println("##### FINAL FILE LENGTH: "+x.length()+" ########");
    System.out.println("##### FINAL CONTENT LENGTH: "+result.length()+" ########");
    System.out.println("##### BYTE[] LENGTH: "+resultBytes.length+" ########");

    in1.close();

    return resultBytes;
  }
}
