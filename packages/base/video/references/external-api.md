# base/video — 对外契约摘要

## 各子服务 Bazel 标签（对外常用）

| 子服务 | 说明 | 标签 |
|--------|------|------|
| adapter | 服务端库 | `//packages/base/video/adapter:server` |
| adapter | 可执行（非 fat-jar） | `//packages/base/video/adapter:server-bin` |
| adapter | Spring Boot 可运行 | `//packages/base/video/adapter:server-exec` |
| adapter | Maven 发布 | `//packages/base/video/adapter:package`（artifactId `adapter-video-server`） |
| adapter | 部署元数据 | `//packages/base/video/adapter:server-info` |
| gateway | 服务端库 | `//packages/base/video/gateway/server:server` |
| gateway | 可执行 | `//packages/base/video/gateway/server:server-bin` |
| gateway | Spring Boot 可运行 | `//packages/base/video/gateway/server:server-exec` |
| gateway | Maven 发布 | `//packages/base/video/gateway/server:package`（`videogw-server`） |
| gateway | 部署元数据 | `//packages/base/video/gateway/server:server-info` |
| gateway | Feign/客户端导出 | `//packages/base/video/gateway/client:export`（及 `:export-dev`、`:export-test`） |
| monitor | 服务端库 | `//packages/base/video/monitor/server:server` |
| monitor | 可执行 | `//packages/base/video/monitor/server:server-bin` |
| monitor | Spring Boot 可运行 | `//packages/base/video/monitor/server:server-exec` |
| monitor | Maven 发布 | `//packages/base/video/monitor/server:package`（`vmonitor-server`） |
| monitor | 部署元数据 | `//packages/base/video/monitor/server:server-info` |
| gateway | DB 初始化脚本包 | `//packages/base/video/gateway/server/init:init-mysql` |
| monitor | DB 初始化脚本包 | `//packages/base/video/monitor/server/init:init-mysql` |

根目录 `//packages/base/video` 无 BUILD 目标。

## HTTP 网关前缀（`server_info`）

| 子服务 | `gw_prefix` |
|--------|----------------|
| adapter | `/video-adapter/` |
| gateway | `/videogw/` |
| monitor | `/vmonitor/` |

完整对外 URL 通常为「全局网关前缀 + 上表 + 应用内 `@*Mapping` 路径」；多环境路由 **待团队补充**。

## 应用内路由前缀（节选）

### gateway（`VideoController`、`VideoFileController`）

- `/video/{deviceId}/preview-url`（GET）
- `/video/{deviceId}/snap-capture`（POST）
- `/video/{deviceId}/video-capture`（POST）
- `/video/{deviceId}/ptz/control`（POST）
- `/video/auth-info`（GET）
- `/video/file/{id}`（GET）
- `/video/files`（POST）

### adapter（厂商 Controller，路径已含厂商段）

- 海康 ISC：`/hikvision-isc/video/{deviceId}/...`、`/hikvision-isc/video/auth-info`、accessible 抓拍等
- 大华：`/dahua/video/...`
- 古河云（guheyun wvp 视频适配）：`/ghy/video/...`
- H5S：`/h5s/video/...`

### monitor（业务 Controller，节选）

- 多路视频：`/multiple-video/...`、`/safety-multiple-video/devices`
- 收藏：`/multiple-video-collect...`
- 巡逻路线/记录/统计：`/camera-patrol-line...`、`/camera-patrol-record...`、`/camera-patrol-statistics...` 等
- 授权：`/camera-auths...`
- 安全设备授权：`/security/device/auth...` 等

完整列表以各 `*Controller.java` 为准。

## 流媒体与接入协议（从代码归纳）

- **网关统一枚举**（`VideoProtocolEnum`）：`NATIVE`、`RTSP`、`WS`、`WSS`、`RTC`、`HLS`、`HLS_S`、`RTMP`。预览默认 **WS**（见 `VideoPreviewUrlQuery` / `SubsystemVideoSdkService` 注释与实现）。
- **厂商适配层**：各 `*Convert` / API 注释中还涉及 **FLV**、以及 **GB28181** 传输说明（海康侧注释）；**不代表**本仓库实现完整 GB28181 平台接入，是否作为产品能力 **待团队补充**。
- **RTMP/HLS 能力边界**：枚举与注释中说明部分协议仅适用于海康 SDK/EHOME/ONVIF 等接入方式及编码限制，细节以厂商文档与运行时配置为准。

## RocketMQ

| 消费方 | ConsumerGroup | Topic | 说明 |
|--------|---------------|-------|------|
| monitor `AuthScopeDevGroupChangeConsumer` | `CAMERA_AUTH_SCOPE` | `DEVICE_GROUP_CHANGE`（常量定义于 `device-center` 元数据 `DeviceGroupChange.TOPIC`） | 设备组变更后刷新授权范围名称 |
| monitor `AuthScopeAreaChangeConsumer` | `CAMERA_AUTH_SCOPE` | `AREA_CHANGE`（`AreaChangeMessage.TOPIC`） | 区域变更联动授权 |

**Tag**：监听器未在源码中指定 `selectorExpression`，是否使用 Tag **待团队补充**。

adapter、gateway：**未发现** `@RocketMQMessageListener`；配置仍引入 `common-rocketmq.yaml`，是否发送消息 **待团队补充**。
