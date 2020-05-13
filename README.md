# GoCD task plugin for git info

This is merely a skeleton plugin that plugin developers can fork to get quickly 
started with writing task plugins for GoCD. It works out of the box, but you should change 
it to do something besides executing `curl`.
 
All the documentation is hosted at https://plugin-api.gocd.io/current/tasks.



## Getting started

The git plugin supports to fetch informations from the GIT repository. Once included as task, it will look for GIT repositories and provide informations into a local file *.env*.

The *.env* file will be generated for every repository and once globaly providing informations for all repository. Each environment variable will be prefixed with the name of the repository in upper cases.

```plain
GIT_RELEASE=1.0
GIT_VERSION=1.0.1
GIT_BUILD=52
GIT_BRANCH=develop
GIT_TAG=refs/tags/version/1.0/1
GIT_HASH=193ab095a
GIT_DATE=2020-05-12T11:08:19+02:00
```



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
