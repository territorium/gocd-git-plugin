/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package info.tol.gocd.task.git;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

import java.io.File;
import java.io.PrintWriter;

import info.tol.gocd.task.util.TaskRequest;
import info.tol.gocd.task.util.TaskResponse;
import info.tol.gocd.util.request.RequestHandler;

/**
 * This message is sent by the GoCD agent to the plugin to execute the task.
 *
 * <pre>
 * {
 *   "config": {
 *     "ftp_server": {
 *       "secure": false,
 *       "value": "ftp.example.com",
 *       "required": true
 *     },
 *     "remote_dir": {
 *       "secure": false,
 *       "value": "/pub/",
 *       "required": true
 *     }
 *   },
 *   "context": {
 *     "workingDirectory": "working-dir",
 *     "environmentVariables": {
 *       "ENV1": "VAL1",
 *       "ENV2": "VAL2"
 *     }
 *   }
 * }
 * </pre>
 */
public class TaskHandler implements RequestHandler {

  private final JobConsoleLogger console;

  /**
   * Constructs an instance of {@link TaskHandler}.
   *
   * @param console
   */
  public TaskHandler(JobConsoleLogger console) {
    this.console = console;
  }

  /**
   * Handles a request and provides a response.
   *
   * @param request
   */
  @Override
  public GoPluginApiResponse handle(GoPluginApiRequest request) {
    TaskRequest task = TaskRequest.of(request);
    File workingDir = new File(task.getWorkingDirectory()).getAbsoluteFile();

    try (PrintWriter writer = new PrintWriter(new File(workingDir, ".env"))) {
      if (new File(workingDir, ".git").exists()) {
        this.console.printLine("Process GIT Repository!");
        GitVersion git = GitVersion.getLatestVersion(workingDir);
        printEnvironment(git, "GIT", writer);
      }

      for (File folder : workingDir.listFiles()) {
        if (folder.isDirectory() && new File(folder, ".git").exists()) {
          GitVersion git = GitVersion.getLatestVersion(folder);
          if (git != null) {
            this.console.printLine("Processing GIT Repository '" + folder.getName() + "'!");
            try (PrintWriter writer2 = new PrintWriter(new File(folder, ".env"))) {
              String prefix = folder.getName().toUpperCase();
              printEnvironment(git, prefix, writer2);
              printEnvironment(git, prefix, writer);
            }
          }
        }
      }

      return TaskResponse.success("GIT environment loaded").toResponse();
    } catch (Throwable e) {
      this.console.printLine("" + e);
      return TaskResponse.failure(e.getMessage()).toResponse();
    }
  }

  /**
   * Processes a GIT repository and creates the .env file.
   *
   * @param git
   * @param prefix
   * @param writer
   */
  private void printEnvironment(GitVersion git, String prefix, PrintWriter writer) throws Exception {
    writer.printf("%s_RELEASE=%s\n", prefix, git.getVersion().toString("0.0-0"));
    writer.printf("%s_VERSION=%s\n", prefix, git.getVersion().toString("0.0.0"));
    writer.printf("%s_BUILD=%s\n", prefix, git.getBuild());
    writer.printf("%s_BRANCH=%s\n", prefix, git.getBranch());
    writer.printf("%s_TAG=%s\n", prefix, git.getName());
    writer.printf("%s_HASH=%s\n", prefix, git.getHash());
    writer.printf("%s_DATE=%s\n", prefix, git.getISOTime());
    writer.println();
  }
}
