# fusion-communicate 对外契约速查

以下标签均在仓库根目录执行 `bazel build` / `bazel run` 时使用。

## 对外 Bazel 标签

| 标签 | 说明 |
|------|------|
| `//packages/base/fusion-communicate/server:deps` | 私有聚合依赖（`visibility` private），一般不直接被外部引用。 |
| `//packages/base/fusion-communicate/server:server` | 服务端 `java_library`（`src/**/*.java` + `resources/**`）。 |
| `//packages/base/fusion-communicate/server:server-bin` | `java_binary`，`main_class` 为 `FusionCommunicateServer`。 |
| `//packages/base/fusion-communicate/server:server-exec` | `springboot` 可执行产物（fat-jar）。 |
| `//packages/base/fusion-communicate/server:package` | Maven 发布（`artifactId = fusioncommunicate-server`）。 |
| `//packages/base/fusion-communicate/server:server-info` | `server_info`：库名、网关前缀、主类、`server_id` 等（`//packages:__subpackages__` 可见）。 |
| `//packages/base/fusion-communicate/server/init:init-mysql` | 初始化 SQL 的 `pkg_files`（供 `//packages/deploy/database` 等消费）。 |

模块根目录 `//packages/base/fusion-communicate` 当前 **BUILD.bazel 无 java 目标**（仅注释「融合通讯」）。

## HTTP 接口路由前缀

- **网关前缀**（`server/BUILD.bazel` → `server_info` → `gw_prefix`）：**`/fusion-communicate/`**  
  对外完整路径通常为：**`/fusion-communicate` + 下表应用内路径**（是否还有统一 StripPrefix/重写以网关配置为准，**待团队补充**）。

以下为源码中 **Controller 方法级路径**（类上无统一 `@RequestMapping` 前缀时，与网关前缀拼接）：

### 融合通讯账号与文件（`server/src/controller`）

| 方法 | 应用内路径 |
|------|------------|
| GET | `/fusion-communicate-account/page` |
| GET | `/fusion-communicate/sys-user/{fusionCommunicateAccount}` |
| POST | `/fusion-communicate/sys-users` |
| POST | `/fusion-communicate/assign/sys-user` |
| POST | `/fusion-communicate/assign/current-user` |
| PUT | `/fusion-communicate/disable` |
| PUT | `/fusion-communicate/disable/current-user` |
| PUT | `/fusion-communicate/keep-alive` |
| PUT | `/fusion-communicate/current-user/keep-alive` |
| GET | `/fusion-communicate/users` |
| GET | `/fusion-communicate-file/page` |
| PUT | `/fusion-communicate-file/{id}` |

### 会议房间（`server/src/conference/interfaces/ConferenceRoomController.java`）

| 方法 | 应用内路径 |
|------|------------|
| POST | `/conference-room/create` |
| POST | `/conference-room/propose` |
| POST | `/conference-room/invite` |
| POST | `/conference-room/refuse-join` |
| POST | `/conference-room/join` |
| POST | `/conference-room/leave` |
| POST | `/conference-room/remove/user` |
| POST | `/conference-room/close` |
| GET | `/conference-room/{roomNumber}` |

### 腾讯实时音视频（`server/src/third/tencent/controller`）

| 方法 | 应用内路径 |
|------|------------|
| GET | `/tencent/communication/sig` |

### 迅一（`server/src/third/xunyi/controller`）

| 方法 | 应用内路径 |
|------|------------|
| GET | `/xun-yi/fusion-communicate/account/page` |
| GET | `/xun-yi/fusion-communicate/sys-user/{fusionCommunicateAccount}` |
| POST | `/xun-yi/fusion-communicate/sys-users` |
| POST | `/xun-yi/fusion-communicate/assign/sys-user` |

### GPS（`server/src/gps/controller`）

| 方法 | 应用内路径 |
|------|------------|
| POST | `/gps/upload` |
| GET | `/gps/page` |

## MQ 消息（Topic / Tag）

- 配置层：`server/resources/application.yaml` 通过 Nacos 引入 `common-rocketmq.yaml`。
- 代码层：本模块 `server/src` 下**未发现** RocketMQ 监听/发送注解或显式 Topic 常量引用。

**待团队补充**：若生产环境存在 MQ 生产/消费，请补充 Topic、Tag、消费组及用途。

## 融合通信协议说明

- **REST**：上述 HTTP 路径，请求/响应体以 JSON 为主（具体 DTO 见各 Controller 与 `vo`/`param` 包）。
- **腾讯 TRTC**：
  - **UserSig**：通过腾讯云 **TLSSigAPIv2** 为当前登录用户生成短期签名，供客户端 TRTC SDK 进房；接口返回 `sdkAppId` 与 `sig`（见 `TencentCommunicationController`、`TencentRealTimeCommunicationApiService`）。
  - **服务端房间管控**：使用腾讯云 **TRTC OpenAPI**（`trtc.v20190722.TrtcClient`），例如按字符串房间 ID 解散房间、移除用户等（与会议业务编排配合）。
  - **媒体面**：音视频传输为 **TRTC 客户端 SDK 协议**；本服务不承载 RTP/WebRTC 媒体面。
- **会议业务**：本服务维护会议房间相关业务状态与 HTTP 编排；与 TRTC 房间 ID 的绑定与一致性策略见 `conference` 与 `third/tencent` 代码（细粒度字段级说明 **待团队补充**）。
- **迅一**：HTTP 对接迅一侧账号能力；具体对方协议版本与鉴权 **待团队补充**。
