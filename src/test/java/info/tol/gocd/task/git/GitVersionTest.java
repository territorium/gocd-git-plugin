
package info.tol.gocd.task.git;

import java.io.File;
import java.io.PrintWriter;

public class GitVersionTest {

  public static void main(String[] args) throws Exception {
    File location = new File("/data/smartIO/develop/server");
    GitVersion git = GitVersion.getLatestVersion(location);

    try (PrintWriter writer = new PrintWriter(System.out)) {
      writer.printf("VERSION\t=%s\n", git.getVersion().toString("0.0.0-0"));
      writer.printf("BRANCH\t=%s\n", git.getBranchName());
      writer.printf("NAME\t=%s\n", git.getTagName());
      writer.printf("HASH\t=%s\n", git.getHash());
      writer.printf("DATE\t=%s\n", git.getISOTime());
      writer.printf("COUNT\t=%s\n", git.getCount());
    }
  }
}
