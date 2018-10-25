import java.util.*;
import java.io.*;

public class Test {

  public static void main(String[] args) {
    List<String[]> files = new ArrayList<String[]>();
    String[] erstes = {"/test/Test.java","Test.java"};
    String[] zweites = {"/test/ArchiveUtil.java","ArchiveUtil.java"};
    String[] drittes = {"/test/jdom.jar","./bin/lib/jdom.jar"};
    files.add(erstes);
    files.add(zweites);
    files.add(drittes);
    try {
      File out = new File("output.zip");
      new FileOutputStream(out).write((byte[])ArchiveUtil.createZip(files));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
