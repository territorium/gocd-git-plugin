
package info.tol.gocd.task.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GitBranch implements Closeable {

  private final RevWalk   revWalk;
  private final RevCommit commit;

  public GitBranch(String name, Repository repo) throws IOException {
    Ref ref = repo.findRef(name);
    this.revWalk = new RevWalk(repo);
    this.commit = this.revWalk.lookupCommit(ref.getObjectId());
    this.revWalk.markStart(this.commit);
  }

  /**
   * Get the {@link RevWalk}.
   */
  public final RevWalk getRevWalk() {
    return this.revWalk;
  }

  /**
   * Get the {@link RevCommit}.
   */
  public final RevCommit getCommit() {
    return this.commit;
  }

  /**
   * Get the list of commit merged into branch..
   */
  public final List<ObjectId> getCommits() {
    return StreamSupport.stream(this.revWalk.spliterator(), false).map(c -> c.toObjectId())
        .collect(Collectors.toList());
  }

  @Override
  public void close() throws IOException {
    this.revWalk.close();
  }
}
