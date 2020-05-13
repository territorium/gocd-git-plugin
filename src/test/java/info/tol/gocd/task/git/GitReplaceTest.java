
package info.tol.gocd.task.git;

import java.io.File;

public class GitReplaceTest {

  public static void main(String[] args) throws Exception {
    File location = new File("/home/brigl/Downloads/smartIO-Android-Develop");
    for (GitProperties file : GitProperties.list(location, "*.{pri,pro}")) {
      file.replace();
    }
  }
}
