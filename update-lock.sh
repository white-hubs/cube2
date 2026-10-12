#!/bin/bash

# This script updates the lock file for the project.
# [ref](https://github.com/bazel-contrib/rules_jvm_external/blob/master/docs/bzlmod.md#installation)
REPIN=1 bazel run @maven//:pin