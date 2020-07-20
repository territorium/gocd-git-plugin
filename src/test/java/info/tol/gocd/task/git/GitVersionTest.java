
package info.tol.gocd.task.git;

import java.io.File;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class GitVersionTest {

  public static void main(String[] args) throws Exception {
    File location = new File("/data/smartIO/release2004/app");
    GitVersion git = GitVersion.getLatestVersion(location);

    try (PrintWriter writer = new PrintWriter(System.out)) {
      writer.printf("VERSION\t=%s\n", git.getVersion().toString("0.0.0-0"));
      writer.printf("BRANCH\t=%s\n", git.getBranchName());
      writer.printf("NAME\t=%s\n", git.getTagName());
      writer.printf("HASH\t=%s\n", git.getHash());
      writer.printf("DATE\t=%s\n", git.getISOTime());
      writer.printf("COUNT\t=%s\n", git.getCount());


      Instant instant =
          OffsetDateTime.of(LocalDate.of(2016, 1, 1), LocalTime.of(0, 0), ZoneOffset.ofHours(0)).toInstant();
      long time = System.currentTimeMillis() - instant.toEpochMilli();


      System.out.println(time / 1000 / 3600);
    }
  }
}
