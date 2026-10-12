load("@rules_spring//springboot:springboot.bzl", _springboot = "springboot")

# 运行版本大于 jdk8 时需要添加 --add-opens
# If version > jdk8 need `--add-opens`
# run example:
#              java --add-opens=java.base/java.base=ALL-UNNAMED
#                   --add-opens=java.base/java.io=ALL-UNNAMED
#                   --add-opens=java.base/java.nio=ALL-UNNAMED
#                   --add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED
#                   --add-opens=java.base/java.lang.reflect=ALL-UNNAMED
#                   --add-opens=java.base/java.lang=ALL-UNNAMED
#                   -jar server-exec.jar
OVERRIDE_DEFAULT_BAZEL_RUN_ADDOPENS = [
    "java.base/java.base=ALL-UNNAMED",
    "java.base/java.io=ALL-UNNAMED",
    "java.base/java.nio=ALL-UNNAMED",
    "java.base/sun.reflect.annotation=ALL-UNNAMED",
    "java.base/java.lang.reflect=ALL-UNNAMED",
    "java.base/java.lang=ALL-UNNAMED",
]

OVERRIDE_DEFAULT_DUPECLASSESCHECK_ENABLE = False

def springboot(
        name,
        java_library,
        boot_app_class,
        boot_launcher_class = "org.springframework.boot.loader.launch.JarLauncher",
        deps = None,
        deps_banned = [],  # detects common mistake of test dep pollution
        deps_exclude_labels = None,
        deps_exclude = None,
        deps_exclude_paths = None,
        deps_index_file = None,
        deps_use_starlark_order = None,
        dupeclassescheck_enable = OVERRIDE_DEFAULT_DUPECLASSESCHECK_ENABLE,
        dupeclassescheck_ignorelist = None,
        javaxdetect_enable = None,
        javaxdetect_ignorelist = None,
        include_git_properties_file = True,
        bazelrun_java_toolchain = None,
        bazelrun_script = None,
        bazelrun_jvm_flags = None,
        bazelrun_jvm_flag_list = None,
        bazelrun_env_flag_list = None,
        bazelrun_data = None,
        bazelrun_background = False,
        addins = [],
        tags = [],
        testonly = False,
        visibility = None,
        bazelrun_addopens = OVERRIDE_DEFAULT_BAZEL_RUN_ADDOPENS,
        bazelrun_addexports = [],
        jartools_toolchains = ["@bazel_tools//tools/jdk:current_host_java_runtime"],  # Issue 250
        exclude = [],  # deprecated
        classpath_index = "@rules_spring//springboot:empty.txt",  # deprecated
        use_build_dependency_order = True,  # deprecated
        fail_on_duplicate_classes = False,  # deprecated
        duplicate_class_allowlist = None,  # deprecated
        jvm_flags = "",  # deprecated
        data = [],  # deprecated
        restricted_to = None,
        target_compatible_with = []):
    """Bazel rule for packaging an executable Spring Boot application.

    Note that the rule README has more detailed usage instructions for each attribute.

    Args:
      name: **Required**. The name of the Spring Boot application. Typically this is set the same as the package name.
        Ex: *helloworld*.
      java_library: **Required**. The built jar, identified by the name of the java_library rule, that contains the
        Spring Boot application.
      boot_app_class: **Required**. The fully qualified name of the class annotated with @SpringBootApplication.
        Ex: *com.sample.SampleMain*
      deps: Optional. An additional set of Java dependencies to add to the executable.
        Normally all dependencies are set on the *java_library*.
      deps_banned: Optional. A list of strings to match against the jar filenames in the transitive graph of
        dependencies for this springboot app. If any of these strings is found within any jar name, the rule will fail.
        This is useful for detecting jars that should never go to production. The list of dependencies is
        obtained after the deps_exclude_labels and deps_exclude_paths processing has run. Default: [ "junit", "mockito" ]
      deps_exclude_labels: Optional. A list of jar labels that will be omitted from the final packaging step.
        This is a manual option for eliminating a problematic dependency that cannot be eliminated upstream.
        Ex: *["@maven//:commons_cli_commons_cli"]*.
      deps_exclude: Deprecated. Use deps_exclude_labels instead. Functions the same as deps_exclude_labels but retained for backward compatibility.
      deps_exclude_paths: Optional. This attribute provides a list of partial paths that will be omitted
        from the final packaging step if the string is contained within the dep filename. This is a more raw method
        than deps_exclude_labels for eliminating a problematic dependency/file that cannot be eliminated upstream.
        Ex: [*jackson-databind-*].
      deps_index_file: Optional. Uses Spring Boot's
        [classpath index feature](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html#executable-jar-war-index-files-classpath)
        to define classpath order. This feature is not commonly used, as the application must be extracted from the jar
        file for it to work. Ex: *my_classpath_index.idx*
      deps_use_starlark_order: When running the Spring Boot application from the executable jar file, setting this attribute to
        *True* will use the classpath order as expressed by the order of deps in the BUILD file. Otherwise it is random order.
      dupeclassescheck_enable: If *True*, will analyze the list of dependencies looking for any class that appears more than
        once, but with a different hash. This indicates that your dependency tree has conflicting libraries.
      dupeclassescheck_ignorelist: Optional. When using the duplicate class check, this attribute provides a file
        that contains a list of libraries excluded from the analysis. Ex: *dupeclass_libs.txt*
      javaxdetect_enable: If *True*, will analyze the list of dependencies looking for any class from javax.*
        package. This is a candidate for migration to jakarta.
      javaxdetect_ignorelist: Optional. When using the javax detect check, this attribute provides a file
        that contains a list of libraries excluded from the analysis. Ex: *javaxdetect_ignorelist.txt*
      include_git_properties_file: If *True*, will include a git.properties file in the resulting jar.
      bazelrun_java_toolchain: Optional. Label to the Java toolchain to use when launching the application using 'bazel run'
      bazelrun_script: Optional. When launching the application using 'bazel run', a default launcher script is used.
        This attribute can be used to provide a customized launcher script. Ex: *my_custom_script.sh*
      bazelrun_jvm_flags: Optional. Deprecated form of bazelrun_jvm_flag_list. Ex: *-Dcustomprop=gold -DcustomProp2=silver*
      bazelrun_jvm_flag_list: Optional. When launching the application using 'bazel run', an optional set of JVM flags
        to pass to the JVM at startup. Ex: *['-Dcustomprop=gold', '-DcustomProp2=silver']*
      bazelrun_env_flag_list: Optional. When launching the application using 'bazel run', an optional set of environment
        variables to pass to the launcher script at startup. Ex: *['PROP1=gold', 'PROP2=silver']*
      bazelrun_data: Uncommon option to add data files to runfiles. Behaves like the *data* attribute defined for *java_binary*.
      bazelrun_background: Optional. If True, the *bazel run* launcher will not block. The run command will return and process will remain running.
      addins: Uncommon option to add additional files to the root of the springboot jar. For example a license file. Pass an array of files from the package.
      tags: Optional. Bazel standard attribute.
      testonly: Optional. Bazel standard attribute. Defaults to False.
      visibility: Optional. Bazel standard attribute.
      jartools_toolchains: Optional. Toolchains for running build tools like singlejar, override for obscure use cases. Default: ["@bazel_tools//tools/jdk:current_java_runtime"]
      restricted_to: Optional. Bazel standard attribute.
      target_compatible_with: Optional. Bazel standard attribute.
      exclude: Deprecated synonym of *deps_exclude_labels* and *deps_exclude*.
      classpath_index: Deprecated synonym of *deps_index_file*
      use_build_dependency_order: Deprecated synonym of *deps_use_starlark_order*
      fail_on_duplicate_classes: Deprecated synonym of *dupeclassescheck_enable*
      duplicate_class_allowlist: Deprecated synonym of *dupeclassescheck_ignorelist*
      jvm_flags: Deprecated synonym of *bazelrun_jvm_flags*
      data: Deprecated synonym of *bazelrun_data*
    """
    _springboot(
        name,
        java_library,
        boot_app_class,
        boot_launcher_class,
        deps,
        deps_banned,
        deps_exclude_labels,
        deps_exclude,
        deps_exclude_paths,
        deps_index_file,
        deps_use_starlark_order,
        dupeclassescheck_enable,
        dupeclassescheck_ignorelist,
        javaxdetect_enable,
        javaxdetect_ignorelist,
        include_git_properties_file,
        bazelrun_java_toolchain,
        bazelrun_script,
        bazelrun_jvm_flags,
        bazelrun_jvm_flag_list,
        bazelrun_env_flag_list,
        bazelrun_data,
        bazelrun_background,
        addins,
        tags,
        testonly,
        visibility,
        bazelrun_addopens,
        bazelrun_addexports,
        jartools_toolchains,
        exclude,
        classpath_index,
        use_build_dependency_order,
        fail_on_duplicate_classes,
        duplicate_class_allowlist,
        jvm_flags,
        data,
        restricted_to,
        target_compatible_with,
    )
