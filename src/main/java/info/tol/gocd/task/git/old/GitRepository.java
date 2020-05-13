
package info.tol.gocd.task.git.old;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GitRepository implements Closeable {

  private final Repository repo;

  /**
   * Create an instance of {@link GitRepository}.
   * 
   * @param repo
   */
  private GitRepository(Repository repo) {
    this.repo = repo;
  }

  /**
   * Get the {@link Repository}.
   */
  public final Repository getRepository() {
    return this.repo;
  }

  /**
   * Open a {@link GitRepository} from a module
   * 
   * @param repo
   */
  public final GitRepository subModule(String module) throws IOException {
    return new GitRepository(SubmoduleWalk.getSubmoduleRepository(this.repo, module));
  }

  /**
   * Open a {@link GitBranch}.
   * 
   * @param name
   */
  public final GitBranch openBranch(String name) throws IOException {
    return new GitBranch(name, this.repo);
  }

  /**
   * Open a {@link GitBranch}.
   * 
   * @param name
   */
  public final Stream<Ref> filterTags(Predicate<Ref> p) throws GitAPIException {
    try (Git git = new Git(this.repo)) {
      return git.tagList().call().stream().filter(r -> p.test(r));
    }
  }

  @Override
  public void close() {
    this.repo.close();
  }

  /**
   * Open a {@link Repository} from path.
   * 
   * @param path
   */
  public static GitRepository of(String path) throws IOException {
    RepositoryBuilder builder = new RepositoryBuilder();
    builder.setGitDir(new File(path));
    return new GitRepository(builder.build());
  }
}
