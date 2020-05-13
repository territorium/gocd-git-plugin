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

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import info.tol.gocd.task.util.ConfigResponse;

/**
 * The {@link GitConfig} class.
 */
public interface GitConfig {

  String FILES = "files";

  /**
   * Create a {@link ConfigResponse}.
   */
  public static ConfigResponse create() {
    ConfigResponse config = new ConfigResponse();
    config.setValue(GitConfig.FILES, null, "File Pattern", "1", false, false);
    return config;
  }

  /**
   * Handles a request and provides a response.
   *
   * @param request
   * 
   *        This message is sent by the GoCD server to the plugin to know what properties are
   *        supported by this plugin that should to be stored in the cruise-config.xml file.
   *
   *        <pre>
   * {
   *   "url": {
   *     "default-value": "",
   *     "secure": false,
   *     "required": true
   *   },
   *   "user": {
   *     "default-value": "bob",
   *     "secure": true,
   *     "required": true
   *   },
   *   "password": {}
   * }
   *        </pre>
   */
  public static GoPluginApiResponse createGoApiResponse() {
    return create().build();
  }
}
