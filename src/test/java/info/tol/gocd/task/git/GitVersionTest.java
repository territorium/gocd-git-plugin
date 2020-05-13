
package info.tol.gocd.task.git;

import java.io.File;

public class GitVersionTest {

  public static void main(String[] args) throws Exception {
    File location = new File("/data/smartIO/develop/remoteLogger");
    GitVersion git = GitVersion.getLatestVersion(location);

    System.out.printf("VERSION\t= %s\n", git.getVersion().toString("0.0.0"));
    System.out.printf("BRANCH\t= %s\n", git.getBranch());
    System.out.printf("NAME\t= %s\n", git.getName());
    System.out.printf("HASH\t= %s\n", git.getHash());
    System.out.printf("DATE\t= %s\n", git.getSimpleTime());
  }
}
