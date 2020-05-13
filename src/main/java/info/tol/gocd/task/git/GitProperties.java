/*
 * Copyright (c) 2001-2019 Territorium Online Srl / TOL GmbH. All Rights Reserved.
 *
 * This file contains Original Code and/or Modifications of Original Code as defined in and that are
 * subject to the Territorium Online License Version 1.0. You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at http://www.tol.info/license/
 * and read it before using this file.
 *
 * The Original Code and all software distributed under the License are distributed on an 'AS IS'
 * basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND TERRITORIUM ONLINE HEREBY
 * DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT. Please see the License for
 * the specific language governing rights and limitations under the License.
 */

package info.tol.gocd.task.git;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.tol.gocd.util.Environment;


/**
 * The {@link GitProperties} class.
 */
public class GitProperties {

  private final static Pattern GIT_PROPERTY = Pattern.compile("^([\\w]+)[^=]*=.+");


  private final File        file;
  private final Environment environment;

  /**
   * Constructs an instance of {@link GitProperties}.
   *
   * @param file
   * @param environment
   */
  public GitProperties(File file, Environment environment) {
    this.file = file;
    this.environment = environment;
  }

  /**
   * Gets the file match.
   */
  public final File getFile() {
    return file;
  }

  /**
   * Gets the {@link Environment}.
   */
  public final Environment getEnvironment() {
    return environment;
  }


  /**
   * Replace the {@link GitProperties} with the environment variable.
   *
   * @throws IOException
   */
  public void replace() throws IOException {
    boolean matches = false;
    StringBuilder builder = new StringBuilder();
    for (String line : Files.readAllLines(getFile().toPath(), StandardCharsets.UTF_8)) {
      Matcher matcher = GIT_PROPERTY.matcher(line);
      if (matcher.find() && getEnvironment().isSet(matcher.group(1))) {
        matches = true;
        String name = matcher.group(1);
        builder.append(name);
        builder.append("=");
        builder.append(getEnvironment().get(name));
      } else {
        builder.append(line);
      }
      builder.append("\n");
    }
    if (matches) {
      try (PrintWriter writer = new PrintWriter(getFile())) {
        writer.write(builder.toString());
      }
    }
  }

  /**
   * The {@link FilePatternFilter} class.
   */
  private static class FilePatternFilter extends SimpleFileVisitor<Path> {

    private final Pattern             pattern;
    private final List<GitProperties> files        = new ArrayList<>();
    private final Stack<Environment>  environments = new Stack<Environment>();

    /**
     * Constructs an instance of {@link FilePatternFilter}.
     *
     * @param pattern
     */
    public FilePatternFilter(Pattern pattern) {
      this.pattern = pattern;
      this.environments.add(Environment.empty());
    }

    /**
     * Invoked for a directory before entries in the directory, and all of their descendants, have
     * been visited.
     *
     * @param path
     * @param attrs
     */
    @Override
    public final FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
      File file = new File(path.toFile(), ".env");
      if (file.exists()) {
        Environment env = environments.peek().clone();
        environments.push(env.load(file));
      }
      return FileVisitResult.CONTINUE;
    }

    /**
     * Invoked for a directory after entries in the directory, and all of their descendants, have
     * been visited.
     */
    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
      if (new File(path.toFile(), ".env").exists()) {
        environments.pop();
      }
      return FileVisitResult.CONTINUE;
    }

    /**
     * Visit a file.
     *
     * @param path
     * @param attrs
     */
    @Override
    public final FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
      if (pattern.matcher(path.getFileName().toString()).find()) {
        files.add(new GitProperties(path.toFile(), environments.peek()));
      }
      return FileVisitResult.CONTINUE;
    }
  }


  /**
   * Get a list of files, matching the provided pattern.
   *
   * @param location
   * @param pattern
   * @throws IOException
   */
  public static List<GitProperties> list(File location, String pattern) throws IOException {
    String text = pattern.replace(".", "\\.").replace("*", ".+").replace(',', '|').replace('{', '(').replace('}', ')');
    Pattern regexp = Pattern.compile("^" + text + "$", Pattern.CASE_INSENSITIVE);

    FilePatternFilter filter = new FilePatternFilter(regexp);
    Files.walkFileTree(location.toPath(), filter);
    return filter.files;
  }
}
