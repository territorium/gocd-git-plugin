
package info.tol.gocd.task.git;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalkUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import info.tol.gocd.task.git.old.GitBranch;
import info.tol.gocd.task.git.old.GitRepository;
import info.tol.gocd.util.Version;

/**
 * Simple snippet which shows how to open an existing repository
 *
 * @author dominik.stadler at gmx.at
 */
public class GitTest {

  private final static Pattern VERSION = Pattern.compile(
      "(?<major>\\d+)[\\./](?<minor>\\d+)(?:[\\./](?<patch>\\d+))?(?:[\\-](?<name>[a-z].+))?(?:\\+(?<build>.+))?$");

  private static TagInfo from(Ref ref, GitBranch branch) {
    TagInfo info = null;
    Version version = null;
    try {
      RevCommit c = branch.getRevWalk().lookupCommit(ref.getObjectId());
      branch.getRevWalk().parseBody(c);
      Date time = new Date(c.getCommitTime() * 1000L);
      LocalDateTime local = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      version = Version.parse(ref.getName(), VERSION);
      int count = RevWalkUtils.count(branch.getRevWalk(), branch.getCommit(), c);
      info = new TagInfo(c.getName(), local, version, count);
    } catch (Throwable e) {
      System.out.println("***" + version);
      e.printStackTrace();
    }
    return info;
  }

  public static void main(String[] args) throws IOException, GitAPIException {
    File location = new File("/var/lib/go-agent/pipelines/smartIO-Web-Develop/.git");
    location = new File("/home/brigl/Downloads/smartIO-Web-Develop");

    // "/data/smartIO/develop/.git"
    try (GitRepository repoRoot = GitRepository.of(location.getAbsolutePath())) {
      // try (GitRepository repoNode = repoRoot.subModule("server")) {
      try (GitBranch branch = repoRoot.openBranch("HEAD")) {
        List<ObjectId> commits = branch.getCommits();
        branch.getRevWalk().parseBody(branch.getCommit());
        System.out.println(branch.getCommit().getShortMessage());
        Stream<Ref> tags = repoRoot.filterTags(t -> commits.contains(t.getObjectId()));
        tags.map(t -> from(t, branch)).filter(i -> (i != null)).sorted().forEach(
            i -> System.out.printf("%s (%s) at %s\n\t%s\n", i.getVersion(), i.getCommits(), i.getTime(), i.getHash()));
      }
    }
  }
  // }
}