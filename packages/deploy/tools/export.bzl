load("@rules_jvm_external//:defs.bzl", "java_export")

def _export_impl(
        name,
        package_group,
        artifact_id,
        release_version,
        runtime_deps,
        test_version,
        dev_version,
        exclude_deps = [],
        **kwargs):
    """Implementation of the export rule."""

    # test rule
    java_export(
        name = name + "-test",
        deploy_env = exclude_deps,
        maven_coordinates = "%s:%s:%s" % (
            package_group,
            artifact_id,
            test_version,
        ),
        tags = ["no-javadocs"],
        runtime_deps = runtime_deps,
    )

    # dev rule
    java_export(
        name = name + "-dev",
        deploy_env = exclude_deps,
        maven_coordinates = "%s:%s:%s" % (
            package_group,
            artifact_id,
            dev_version,
        ),
        tags = ["no-javadocs"],
        runtime_deps = runtime_deps,
    )

    # default rule
    java_export(
        name = name,
        deploy_env = exclude_deps,
        maven_coordinates = "%s:%s:%s" % (
            package_group,
            artifact_id,
            release_version,
        ),
        tags = ["no-javadocs"],
        runtime_deps = runtime_deps,
    )

export = macro(
    implementation = _export_impl,
    attrs = {
        "package_group": attr.string(
            configurable = False,
        ),
        "artifact_id": attr.string(
            configurable = False,
        ),
        "release_version": attr.string(
            configurable = False,
        ),
        "test_version": attr.string(
            default = "test-SNAPSHOT",
            configurable = False,
        ),
        "dev_version": attr.string(
            default = "develop-SNAPSHOT",
            configurable = False,
        ),
        "runtime_deps": attr.label_list(
            mandatory = True,
            providers = ["java_export"],
        ),
        "exclude_deps": attr.label_list(
            default = [],
            providers = ["java_export"],
        ),
    },
)
