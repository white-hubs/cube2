load("//packages/deploy/template/gateway/docker:generate_docker.bzl", "generate_docker")
load("//packages/deploy/tools:server_info.bzl", "ServerInfo")
load("//tools/springboot:springboot.bzl", "springboot")

GATEWAY_MAIN_CLASS = "com.guhecloud.cube.gateway.Gateway"

def _generate_route_info_single(gw_prefix):
    gw_prefix = gw_prefix.endswith("/") and (gw_prefix + "**") or (gw_prefix + "/**")
    return """{gw_prefix}""".format(
        gw_prefix = gw_prefix,
    )

def _generate_route_info(server_id, gw_prefix, gw_path_rewrite, strip_prefix = "1"):
    paths = []
    for prefix in gw_prefix.split(","):
        if prefix == "":
            continue
        paths.append(_generate_route_info_single(prefix))
    strip_prefix = strip_prefix != "" and strip_prefix or "1"
    return """
          - id: {server_id}
            uri: lb://{server_id}
            predicates:
             - Path={paths}
            filters:
             {gw_path_rewrite}
             - StripPrefix={strip_prefix}""".format(
        server_id = server_id,
        paths = ", ".join(paths),
        strip_prefix = strip_prefix,
        gw_path_rewrite = gw_path_rewrite != "" and ("- RewritePath=%s" % gw_path_rewrite) or "",
    )

def _get_public_path_patterns(gw_prefix, public_path_patterns):
    if not public_path_patterns:
        return []
    prefixs = gw_prefix.split(",")
    patterns = []
    for pattern in public_path_patterns.split(","):
        if pattern.startswith("//"):
            patterns.append(pattern.replace("//", "/"))
        else:
            for prefix in prefixs:
                if prefix != "":
                    patterns.append((prefix + "/" + pattern).replace("///", "/").replace("//", "/"))

    return patterns

def _generate_template_java_file_impl(ctx):
    public_path_patterns = ["/*/public/**", "/*/actuator/**"]
    routes = []
    for dep in ctx.attr.deps:
        public_path_patterns.extend(
            _get_public_path_patterns(dep[ServerInfo].gw_prefix, dep[ServerInfo].public_path_patterns),
        )
        if not dep[ServerInfo].gw_prefix:
            continue
        routes.append(_generate_route_info(
            server_id = dep[ServerInfo].server_id,
            gw_prefix = dep[ServerInfo].gw_prefix,
            gw_path_rewrite = dep[ServerInfo].gw_path_rewrite,
            strip_prefix = dep[ServerInfo].strip_prefix,
        ))
    ctx.actions.expand_template(
        output = ctx.outputs.output_file,
        template = ctx.file.template,
        substitutions = {
            "{NAME}": ctx.attr.name,
            "{DEPS_SERVER_PUBLIC}": "".join(
                [
                    "				.pathMatchers(\"%s\").permitAll()\n" % dep
                    for dep in public_path_patterns
                ],
            ),
            "{DEPS_SERVER_ROUTES}": "".join(
                [
                    dep
                    for dep in routes
                ],
            ),
        },
    )

_generate_template_java_file = rule(
    implementation = _generate_template_java_file_impl,
    attrs = {
        "template": attr.label(
            allow_single_file = True,
        ),
        "deps": attr.label_list(
            allow_files = False,
            allow_empty = True,
            default = [],
        ),
        "output_file": attr.output(),
    },
)

_DEPS = [
    "//third-party/java/projectreactor:reactor_core",
    "//third-party/java/bitwalker:user_agent_utils",
    "//third-party/java/nacos:nacos_spring_cloud_discovery",
    "//third-party/java/nacos:nacos_spring_cloud_config",
    "//third-party/java/spring:spring_cloud_starter_gateway",
    "//third-party/java/spring:spring_cloud_gateway_server",
    "//third-party/java/spring:spring_cloud_loadbalancer",
    "//third-party/java/spring:spring_cloud_commons",
    "//third-party/java/spring:spring_cloud_starter_bootstrap",
    "//third-party/java/spring:spring_boot",
    "//third-party/java/spring:spring_boot_autoconfigure",
    "//third-party/java/spring:spring_boot_loader",
    "//third-party/java/spring:spring_boot_starter_webflux",
    "//third-party/java/spring:spring_boot_starter_data_redis",
    "//third-party/java/spring:spring_beans",
    "//third-party/java/spring:spring_context",
    "//third-party/java/spring:spring_core",
    "//third-party/java/spring:spring_web",
    "//third-party/java/spring:spring_security_config",
    "//third-party/java/spring:spring_security_core",
    "//third-party/java/spring:spring_security_web",
    "//third-party/java/spring:spring_security_oauth2_client",
    "//third-party/java/spring:spring_security_oauth2_jose",
    "//third-party/java/spring:spring_security_oauth2_resource_server",
    "//third-party/java/spring:spring_boot_starter_security",
    "//third-party/java/spring:spring_data_redis",
    "//third-party/java/projectreactor:reactor_netty_core",
    "//third-party/java/projectreactor:reactor_netty_http",
    "//third-party/java/jackson:annotations",
    "//third-party/java/jackson:core",
    "//third-party/java/jackson:databind",
    "//packages/platform:generic-deps",
    "//packages/platform/base/utils:string",
    "//packages/platform/base/utils:collection",
]

def _generate_gateway_impl(
        name,
        servers,
        **kwargs):
    _template_web_security_dep_name = name + ".gen_web_security"
    _template_application_yaml_dep_name = name + ".gen_application_yaml"
    _generate_docker_name = name + ".gen_docker_build"

    _generate_template_java_file(
        name = _template_web_security_dep_name,
        template = "//packages/deploy/template/gateway/templates:WebSecurityConfig.tpl.java",
        output_file = "generate-java/WebSecurityConfig.java",
        deps = servers,
    )

    _generate_template_java_file(
        name = _template_application_yaml_dep_name,
        template = "//packages/deploy/template/gateway/templates:application.tpl.yaml",
        output_file = "src/main/resources/application.yaml",
        deps = servers,
    )

    generate_docker(
        name = _generate_docker_name,
        output_jar_name = name + "-spring-exec.jar",
        servers = servers,
    )

    # default rule
    native.java_library(
        name = name,
        srcs = ["//packages/deploy/template/gateway:basic_java"] +
               [
                   ":" + _template_web_security_dep_name,
               ],
        resources = ["//packages/deploy/template/gateway:basic_resources"] + [":" + _template_application_yaml_dep_name],
        deps = _DEPS,
        visibility = ["//packages:__subpackages__"],
    )

    # TODO(danke): 当前 2.6.3 存在 bug，基于 bazel 8.x 实现的新版 macro 会强制校验 name，rules_spring 还未发布修复补丁，先注释掉 springboot 规则
    # [参考](https://github.com/salesforce/rules_spring/issues/271)
    #     springboot(
    #         name = _exec_dep_name,
    #         boot_app_class = kwargs["main_class"],
    #         java_library = ":" + _lib_dep_name,
    #     )

generate_gateway = macro(
    implementation = _generate_gateway_impl,
    attrs = {
        "servers": attr.label_list(
            allow_files = False,
            allow_empty = True,
            default = [],
        ),
    },
)
