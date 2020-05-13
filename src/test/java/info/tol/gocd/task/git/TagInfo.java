package info.tol.gocd.task.git;

import java.time.LocalDateTime;

import info.tol.gocd.util.Version;

public class TagInfo implements Comparable<TagInfo> {

	private final String hash;
	private final LocalDateTime time;
	private final Version version;
	private final int commits;

	/**
	 * 
	 * @param hash
	 * @param time
	 * @param version
	 * @param commits
	 */
	public TagInfo(String hash, LocalDateTime time, Version version, int commits) {
		this.hash = hash;
		this.time = time;
		this.version = version;
		this.commits = commits;
	}

	public final String getHash() {
		return hash;
	}

	public final LocalDateTime getTime() {
		return time;
	}

	public final Version getVersion() {
		return version;
	}

	public final int getCommits() {
		return commits;
	}

	@Override
	public int compareTo(TagInfo o) {
		return (commits == o.commits) ? version.compareTo(o.version) : Integer.valueOf(commits).compareTo(o.commits);
	}
}
