---
name: cube2-base-video
description: >
  Cube2 base 视频三件套：adapter 厂商适配、videogw 统一网关、vmonitor 安防监控；查 RTSP/HLS/预览/云台/巡逻时触发。
  Keywords: base/video, adapterVideo, videoGateWay, vmonitor, video gateway.
---

# base/video

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：
  - **adapter（视频适配器）**：按设备/平台厂商（海康 ISC、大华 ICC、谷和云、H5S、宇视等）提供 HTTP 接口，将预览、抓拍、录像、云台等能力对齐为 Cube 侧统一语义；供子系统请求 SDK（如 `VideoClient`）路由调用。**无业务库**（`server_info` 中 `no_datasource = True`）。
  - **gateway（视频网关）**：对上层暴露统一 REST（预览 URL、抓拍、定时录像、云台、认证信息、录像文件查询）；内部通过 `subsystem-requester-sdk` 的 `VideoClient` 结合设备中心解析第三方设备 ID，再调用 adapter。**库 `cube_base_videogw`**。
  - **monitor（视频监控业务）**：安防侧多路视频、收藏夹、巡逻路线/记录、统计、与安全管理/设备授权联动；消费 RocketMQ 同步授权范围；依赖 videogw **client**、device-center、alarm、security-management 等。**库 `cube-biz-vmonitor`**。
- **与 fusion-communicate 的区别**：`fusion-communicate` 负责**融合通讯**（腾讯 TRTC、会议房间、迅一、GPS 等），网关前缀 `/fusion-communicate/`；本模块负责**安防/设备视频流与网关编排**（预览/抓拍/云台/文件），与实时会议 SDK 无直接重叠。二者均为 base 层独立微服务，勿混淆领域。
- **不负责**：网关全局路由表与租户级路由细节（以部署/Nacos 为准）；各厂商云平台账号密钥注入；客户端播放器具体解码与拉流实现。

## 子模块分流

| 子服务 | 职责摘要 | Bazel 运行/发布主标签 | `spring.application.name`（`bootstrap.yaml`） |
|--------|----------|------------------------|-----------------------------------------------|
| **adapter** | 厂商适配 HTTP，路径按厂商前缀分流（如 `/hikvision-isc/...`、`/dahua/...`） | `//packages/base/video/adapter:server-exec`（可执行）、`:package`（`adapter-video-server`） | `adapterVideo` |
| **gateway** | 统一视频 API，`/video/...`；Facade 经 `VideoClient` 调 adapter | `//packages/base/video/gateway/server:server-exec`、`:package`（`videogw-server`）；Feign 类库：`//packages/base/video/gateway/client:export` | `videoGateWay` |
| **monitor** | 监控业务 REST、授权、巡逻与统计；MQ 消费 | `//packages/base/video/monitor/server:server-exec`、`:package`（`vmonitor-server`） | `vmonitor` |

根目录 `packages/base/video/BUILD.bazel` 当前为空，**无**聚合 target；构建入口分别在 `adapter/`、`gateway/server/`、`gateway/client/`、`monitor/server/`。

## 快速定位清单

1. **网关 HTTP 前缀**（`server_info`）：adapter `/video-adapter/`，gateway `/videogw/`，monitor `/vmonitor/`。
2. **应用名与端口**：各子服务 `resources/bootstrap.yaml`（`spring.application.name`）；`application.yaml` 中 `server.port` 引用 `common-env.video-adapter|video-gateway|video-monitor.port`，默认分别为 **10221 / 10225 / 10208**（以 Nacos `common-env` 为准）。
3. **流媒体协议枚举（网关域模型）**：`gateway/server/src/common/domain/enumeration/VideoProtocolEnum.java`（含 NATIVE、RTSP、WS/WSS、RTC、HLS/HLS_S、RTMP 等）。
4. **网关调适配器链路**：`SubsystemVideoSdkService` → `VideoClient`（`packages/platform/subsystem-requester-sdk`）→ 子系统路由至 adapter。
5. **监控授权与 MQ**：`monitor/server/src/auth/rocketmq/consumer/`；Topic 见 [references/external-api.md](./references/external-api.md)。
6. **数据库**：gateway `cube_base_videogw`；monitor `cube-biz-vmonitor`；Liquibase 默认关闭，见各 `application.yaml`。

## 构建与入口

在仓库根目录执行（Bazel 版本见根目录 `.bazelversion`）。

| 子服务 | 主类（`BUILD.bazel` `_MAIN_CLASS`） | 常用命令 |
|--------|--------------------------------------|----------|
| adapter | `com.guhecloud.cube.srv.gen.adapter.video.server.AdapterVideoServer` | `bazel run //packages/base/video/adapter:server-exec` |
| gateway | `com.guhecloud.cube.base.videogw.server.VideogwServer` | `bazel run //packages/base/video/gateway/server:server-exec` |
| monitor | `com.guhecloud.cube.biz.vmonitor.server.VMonitorServer` | `bazel run //packages/base/video/monitor/server:server-exec` |

- **库目标**：`*:server`（源码 + resources）；**二进制**：`*:server-bin`；**Spring Boot fat-jar**：`*:server-exec`；**Maven 发布**：`*:package`；**部署元数据**：`*:server-info`。
- **videogw 客户端 JAR**：`bazel build //packages/base/video/gateway/client:export`（坐标见 `gateway/variables.bzl` 与 `client/BUILD.bazel`）。

## 对外契约

- **HTTP**：经网关时前缀见上；应用内路径族见 [references/external-api.md](./references/external-api.md)。gateway 核心资源路径为 `/video/{deviceId}/...`、`/video/file/...` 等；adapter 按厂商前缀分流；monitor 为业务路径（`/multiple-video/...`、`/camera-patrol-...` 等）。
- **流媒体协议**：以 `VideoProtocolEnum` 及预览查询参数为准；代码与注释中还出现 **FLV**（大华/宇视等对接说明）、**GB28181**（海康请求注释中的传输说明），**非**网关枚举中的独立取值；是否在生产启用 GB28181 全链路 **待团队补充**。
- **RocketMQ**：monitor 订阅设备组变更、区域变更；adapter/gateway 的 `application.yaml` 虽引入 `common-rocketmq.yaml`，**源码中未见** `@RocketMQMessageListener`（是否仅预留 **待团队补充**）。

## 数据与配置

- **配置中心**：各服务 `spring.config.import` 拉取 Nacos 多份 `common-*.yaml`（env、jackson、redis、security、rocketmq、platform-*、datasource/mybatis 等，adapter 无 datasource 导入）。
- **配置前缀/占位**：端口 `common-env.video-adapter.port`、`common-env.video-gateway.port`、`common-env.video-monitor.port`；数据库主机端口等来自 `common-env.default-infer.ip`、`common-env.db.port`（gateway/monitor）。
- **Liquibase**：gateway/monitor `spring.liquibase.enabled: false`，另有 `liqui-base.execute` 与 `change-log` 指向各模块 changelog；是否在某环境启用 **待团队补充**。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（Bazel 标签、HTTP 前缀、协议与 MQ）
- [references/overview.md](./references/overview.md)（目录树、BUILD 目标、主 Controller/Service、运行命令）
