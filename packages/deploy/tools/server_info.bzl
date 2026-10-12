load("@rules_java//java:defs.bzl", "JavaInfo")

# ServerInfo 导出内容
ServerInfo = provider(
    doc = """
This provider is generate `CubeServerOne` must be used information.
You should export it in your `java_library` rule.
""",
    fields = [
        # 子服务主类，示例：`com.guhecloud.cube.base.alarm.server.AlarmServer`
        "main_class",
        # 子服务数据源基础包名，示例：`com.guhecloud.cube.base.alarm`
        "datasource_package",
        "legacy_datasource_packages",
        # 子服务 `java_library` 依赖，示例：`//packages/base/alarm/server:server`
        "server",
        # 子服务 ID，nacos 注册时使用示例：`alarm`
        "server_id",
        "public_path_patterns",
        "gw_prefix",
        "gw_path_rewrite",
        "strip_prefix",
        # 不生成动态数据源
        "no_datasource",
        "database",
        # 仅供参考，子服务使用的数据库类型，示例：`mysql`，`hg`，`dm`
        "db_type",
    ],
)

def _java_server_info_impl(ctx):
    providers = []
    if ctx.attr.server != None:
        out_jar = ctx.attr.server.files.to_list()[0]
        server_java_info = ctx.attr.server[JavaInfo]
        providers.append(
            JavaInfo(
                output_jar = out_jar,
                compile_jar = out_jar,
                deps = [server_java_info],
            ),
        )
    providers.append(
        ServerInfo(
            main_class = ctx.attr.main_class + ".class",
            datasource_package = ctx.attr.datasource_package,
            server = ctx.attr.server,
            server_id = ctx.attr.server_id,
            public_path_patterns = ctx.attr.public_path_patterns,
            gw_prefix = ctx.attr.gw_prefix,
            gw_path_rewrite = ctx.attr.gw_path_rewrite,
            strip_prefix = ctx.attr.strip_prefix,
            no_datasource = ctx.attr.no_datasource,
            database = ctx.attr.database,
            db_type = ctx.attr.db_type,
            legacy_datasource_packages = ctx.attr.legacy_datasource_packages,
        ),
    )
    return providers

server_info = rule(
    implementation = _java_server_info_impl,
    provides = [ServerInfo],
    attrs = {
        "main_class": attr.string(),
        "datasource_package": attr.string(),
        "legacy_datasource_packages": attr.string_list(
            default = [],
            # 目前只有 device-center 有这毛病，一个服务暴露了两个 base-package，单独拧一个参数支持该特性
            doc = "Additional datasource packages, if not empty, will generate datasource config.",
        ),
        "public_path_patterns": attr.string(),
        "gw_prefix": attr.string(),
        "strip_prefix": attr.string(),
        "gw_path_rewrite": attr.string(),
        "server_id": attr.string(),
        "db_type": attr.string(
            default = "mysql",
            doc = "The database type, only for reference.",
        ),
        "database": attr.string(
            default = "",
            doc = "The database name, if not empty, will generate datasource config.",
        ),
        "no_datasource": attr.bool(
            default = False,
            doc = "If true, do not generate dynamic data source.",
        ),
        "server": attr.label(
            providers = [JavaInfo],
            allow_files = False,
        ),
    },
)
