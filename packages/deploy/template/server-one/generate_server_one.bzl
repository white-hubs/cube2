load("//packages/deploy/template/server-one/docker:generate_docker.bzl", "generate_docker")
load("//packages/deploy/tools:server_info.bzl", "ServerInfo")
load("//tools/springboot:springboot.bzl", "springboot")
load(":db_type.bzl", "DB_TYPE", "assert_is_supported_db_type", "generate_jdbc_url")

SERVER_ONE_MAIN_CLASS = "com.guhecloud.cube.servone.ServoneServer"

def _generate_template_java_file_impl(ctx):
    assert_is_supported_db_type(ctx.attr.db_type)

    datasource_packages = [
        {
            "database": dep[ServerInfo].database,
            "datasource_package": dep[ServerInfo].datasource_package,
            "db_type": ctx.attr.db_type or dep[ServerInfo].db_type,
        }
        for dep in ctx.attr.deps
        if dep[ServerInfo].datasource_package and not dep[ServerInfo].no_datasource
    ]

    for dep in ctx.attr.deps:
        if dep[ServerInfo].legacy_datasource_packages:
            datasource_packages.extend([
                {
                    "database": dep[ServerInfo].database,
                    "datasource_package": pkg,
                    "db_type": ctx.attr.db_type or dep[ServerInfo].db_type,
                }
                for pkg in dep[ServerInfo].legacy_datasource_packages
            ])

    ctx.actions.expand_template(
        output = ctx.outputs.output_file,
        template = ctx.file.template,
        substitutions = {
            "{NAME}": ctx.attr.name,
            "{PRIMARY_DATABASE}": datasource_packages[0]["datasource_package"],
            "{DEP_MAIN_CLASS_IMPORTS}": "".join(
                [
                    "%s,\n" % dep[ServerInfo].main_class
                    for dep in ctx.attr.deps
                    if dep[ServerInfo].main_class
                ],
            ),
            "{DEP_DATA_SOURCE_PACKAGES}": "".join(
                [
                    "\"%s\",\n" % datasource["datasource_package"]
                    for datasource in datasource_packages
                ],
            ),
            "{DB_DRIVER_CLASS_NAME}": DB_TYPE[ctx.attr.db_type]["driver"],
            "{DEP_DATA_SOURCE_YAML_CONFIG}": "".join(
                [
                    """        "[%s]":
          url: %s
          username: %s
          password: ${common-env.db.password}
          driver-class-name: %s
""" % (
                        datasource["datasource_package"],
                        "jdbc:shardingsphere:nacos:${NACOS_CONFIG_SHARDING_JDBC:sharding-db.yaml}?serverAddr=${spring.cloud.nacos.config.server-addr}&namespace=${spring.cloud.nacos.config.namespace}&group=${NACOS_CONFIG_GROUP:DEFAULT_GROUP}" if datasource["datasource_package"] == "com.guhecloud.cube.base.file" else generate_jdbc_url(datasource["db_type"], "${common-env.db.ip}:${common-env.db.port}", datasource["database"], "cube"),
                        datasource["database"] if datasource["db_type"] == "dm" else "${common-env.db.username}",
                        "org.apache.shardingsphere.driver.ShardingSphereDriver" if datasource["datasource_package"] == "com.guhecloud.cube.base.file" else DB_TYPE[datasource["db_type"]]["driver"],
                    )
                    for datasource in datasource_packages
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
        "db_type": attr.string(
            # e.g: mysql | dm | hg
            default = "mysql",
        ),
    },
)

_DEPS = [
    "//packages/platform/database",
    "//packages/platform/job",
    "//packages/platform/rocketmq",
    "//packages/platform/server",
    "//packages/platform/event",
    "//packages/platform/web-extra",
    "//packages/platform/base/utils:regex",
    "//packages/platform/base/utils:string",
    "//third-party/java/nacos:nacos_spring_cloud_discovery",
    "//third-party/java/github:mouzt_bizlog_sdk",
    "//third-party/java/aspectj:aspectjweaver",
    "//third-party/java/jakarta:annotation_api",
    "//third-party/java/mybatis-plus:dynamic_datasource_spring_boot_starter",
    "//third-party/java/mybatis-plus:dynamic_datasource_spring",
    "//third-party/java/mybatis-plus:dynamic_datasource_spring_boot_common",
    "//third-party/java/mybatis-plus:dynamic_datasource_creator",
]

_supported_db_types = ["mysql", "dm", "hg"]

def _generate_server_one_impl(
        name,
        servers,
        db_type,
        **kwargs):
    _tpl_server_one = name + ".gen_server_one"
    _tpl_dynamic_datasource = name + ".gen_dynamic_datasource"
    _tpl_application_yaml = name + ".gen_application_yaml"
    _generate_docker_name = name + ".gen_docker_build"

    assert_is_supported_db_type(db_type)

    _generate_template_java_file(
        name = _tpl_server_one,
        template = "//packages/deploy/template/server-one/templates:ServoneServer.tpl.java",
        output_file = "generate-src/ServoneServer.java",
        db_type = db_type,
        deps = servers,
    )

    _generate_template_java_file(
        name = _tpl_dynamic_datasource,
        template = "//packages/deploy/template/server-one/templates:OneDynamicDataSourceConfig.tpl.java",
        output_file = "generate-src/config/OneDynamicDataSourceConfig.java",
        db_type = db_type,
        deps = servers,
    )

    _generate_template_java_file(
        name = _tpl_application_yaml,
        template = "//packages/deploy/template/server-one/templates:application.tpl.yaml",
        output_file = "src/main/resources/application.yaml",
        db_type = db_type,
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
        srcs = ["//packages/deploy/template/server-one:basic_java"] +
               [
                   ":" + _tpl_server_one,
                   ":" + _tpl_dynamic_datasource,
               ],
        resources = ["//packages/deploy/template/server-one:basic_resources"] + [":" + _tpl_application_yaml],
        deps = _DEPS + servers,
        visibility = ["//packages:__subpackages__"],
    )

    # TODO(danke): 当前 2.6.3 存在 bug，基于 bazel 8.x 实现的新版 macro 会强制校验 name，rules_spring 还未发布修复补丁，先注释掉 springboot 规则
    # [参考](https://github.com/salesforce/rules_spring/issues/271)
    #     springboot(
    #         name = _exec_dep_name,
    #         boot_app_class = kwargs["main_class"],
    #         java_library = ":" + _lib_dep_name,
    #     )

generate_server_one = macro(
    implementation = _generate_server_one_impl,
    attrs = {
        "servers": attr.label_list(
            allow_files = False,
            allow_empty = True,
            default = [],
        ),
        "db_type": attr.string(
            # e.g: mysql | dm | hg
            # see db_type.bzl assert_is_supported_db_type
            default = "mysql",
            configurable = False,
        ),
    },
)
