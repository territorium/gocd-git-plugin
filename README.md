# GoCD task plugin for GIT Info

The GIT Info plugin implements a GoCD task to fetch informations from the git repositories in the pipeline. The plugin at least an *.env* file on the project directory, containing informations of all repositories (prefixed with the module name).
The *.env* file will contain a build number as well, that is calculate by the sum of all *GIT_COMMITS* in the project.

In each repository another *.env* file will be created, where all variables are prefixed with *GIT_*.

```plain
GIT_HASH=193ab095a                      # The short hash id in GIT
GIT_DATE=2020-05-12T11:08:19+02:00      # The date/time as ISO
GIT_BRANCH=develop
GIT_TAG=refs/tags/version/1.0/1
GIT_COMMITS=52                          # Is the number of commits
GIT_RELEASE=1.0                         # Release number as MAJOR.MINOR
GIT_VERSION=1.0.1                       # Version number as MAJOR.MINOR.PATCH

BUILD_NUMBER=123
```

## Replacing property files

Optionally the plugin allows to define a file pattern. In that case the plugin replaces all properties defined in the file with the environment variables. The plugin will consider the local defined environment variables and provides as fallback the global environment.

e.g. will catch all files with extension *.pri* or *.pro* and replace all definitions ```GIT_VERSION=[SOMETHING]``` with the environment variable.

```plain
*.{pri,pro}
```


## Environment structure

Consumers of the *.env* files are inherited. Means that a *.env* definition of a *GIT module* will inherit the definition of the global environment. This is important, as the *BUILD_NUMBER* will be defined only on the global *.env* file.


## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2018 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## About the license and releasing your plugin under a different license

The skeleton code in this repository is licensed under the Apache 2.0 license. The license itself specifies the terms
under which derivative works may be distributed (the license also defines derivative works). The Apache 2.0 license is a
permissive open source license that has minimal requirements for downstream licensors/licensees to comply with.

This does not prevent your plugin from being licensed under a different license as long as you comply with the relevant
clauses of the Apache 2.0 license (especially section 4). Typically, you clone this repository and keep the existing
copyright notices. You are free to add your own license and copyright notice to any modifications.

This is not legal advice. Please contact your lawyers if needed.
