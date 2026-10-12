load("//packages/deploy/tools:server_info.bzl", "ServerInfo")

def _generate_docker_impl(ctx):
    print("Generating Docker files for", ctx.attr.name)

    ctx.actions.expand_template(
        output = ctx.outputs.entrypoint,
        template = ctx.file.entrypoint,
        substitutions = {
            "{OUTPUT_JAR}": ctx.attr.output_jar_name,
        },
    )
    ctx.actions.expand_template(
        output = ctx.outputs.dockerfile,
        template = ctx.file.dockerfile,
        substitutions = {
            "{OUTPUT_JAR}": ctx.attr.output_jar_name,
        },
    )
    ctx.actions.expand_template(
        output = ctx.outputs.bash_tools,
        template = ctx.file.bash_tools,
    )
    ctx.actions.expand_template(
        output = ctx.outputs.docker_build_script,
        template = ctx.file.docker_build_script,
    )
    ctx.actions.expand_template(
        output = ctx.outputs.wait_for_it,
        template = ctx.file.wait_for_it,
    )

generate_docker = rule(
    implementation = _generate_docker_impl,
    attrs = {
        "output_jar_name": attr.string(
            mandatory = True,
        ),
        "entrypoint": attr.label(
            allow_single_file = True,
            default = "//packages/deploy/template/server-one/docker:entrypoint.sh",
        ),
        "servers": attr.label_list(
            allow_files = False,
            allow_empty = True,
            default = [],
        ),
        "dockerfile": attr.label(
            allow_single_file = True,
            default = "//packages/deploy/template/server-one/docker:Dockerfile",
        ),
        "bash_tools": attr.label(
            allow_single_file = True,
            default = "//packages/deploy/template/server-one/docker:bash-tools.sh",
        ),
        "docker_build_script": attr.label(
            allow_single_file = True,
            default = "//packages/deploy/template/server-one/docker:docker-build.sh",
        ),
        "wait_for_it": attr.label(
            allow_single_file = True,
            default = "//packages/deploy/template/server-one/docker:wait-for-it.sh",
        ),
    },
    outputs = {
        "dockerfile": "Dockerfile",
        "bash_tools": "bash-tools.sh",
        "docker_build_script": "docker-build.sh",
        "wait_for_it": "wait-for-it.sh",
        "entrypoint": "entrypoint.sh",
    },
)
