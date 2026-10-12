---
name: cube2-base-ocr
description: >
  Cube2 基础域 OCR 能力聚合：当前提供车牌号码识别微服务（阿里云等厂家可插拔），经网关暴露 REST。
  触发词：packages/base/ocr、ocrVehicle、车牌 OCR、vehicle OCR、gw /ocr-vehicle/、Bazel ocr vehicle server。
---

# base/ocr

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：在 `packages/base/ocr` 下按子域拆分 **OCR 相关基础能力**；当前实现为 **车牌号码 OCR**（非通用全文/票据 OCR）。
- **实现要点**：HTTP 入口委托 `VehicleOcrService`，按字典配置的厂家（如 `ALI`）选择 `VehicleOcr` 实现，调用第三方 OCR SDK（如阿里云 `RecognizeCarNumber`）。
- **不负责**：通用 OCR 平台、非车牌类识别、网关路由与鉴权的具体规则（依赖 `platform/security` 与部署侧配置）；本模块根目录 **无** 聚合 `BUILD.bazel`，请直接定位子目录 `vehicle/server`。

## 子模块分流

| 子模块 | Bazel 标签（主要） | 职责 |
|--------|-------------------|------|
| **vehicle/server** | `//packages/base/ocr/vehicle/server:server`；可执行 `//packages/base/ocr/vehicle/server:server-bin`、`//packages/base/ocr/vehicle/server:server-exec`（springboot）；元数据 `//packages/base/ocr/vehicle/server:server-info` | 车牌 OCR Spring Boot 应用：`VehicleOcrController`、厂家策略、`AliVehicleOcr` 等 |
| **vehicle/server :deps** | `//packages/base/ocr/vehicle/server:deps`（`visibility` 私有） | 仅作 `server` 的依赖聚合，外部勿直接引用 |

当前 `packages/base/ocr` 下仅有 **`vehicle/`** 子目录；若后续增加其他 OCR 子域（如证件、票据），建议在同级新增子目录并补充本表。

## 快速定位清单

1. **服务入口类**：`vehicle/server/src/OcrVehicleServer.java`。
2. **对外 HTTP**：`vehicle/server/src/controller/VehicleOcrController.java`（`POST ocr/vehicle-number`）。
3. **业务编排**：`vehicle/server/src/service/VehicleOcrService.java`。
4. **厂家路由**：`vehicle/server/src/context/OcrVehicleNumberContextHolder.java` + `vehicle/server/src/third/*Manufacture*.java`。
5. **阿里云实现**：`vehicle/server/src/third/ali/AliVehicleOcr.java`、`AliDictConfig.java`。
6. **请求/响应 DTO**：`vehicle/server/src/third/dto/req/OcrVehicleReq.java`、`res/OcrVehicleRes.java`。
7. **网关前缀元数据**：`vehicle/server/BUILD.bazel` 中 `server_info`（`gw_prefix = "/ocr-vehicle/"`，`server_id = "ocrVehicle"`）。
8. **应用名与端口**：`vehicle/server/resources/bootstrap.yaml`、`application.yaml`。

## 构建与入口

- **服务端库**：`//packages/base/ocr/vehicle/server:server`。
- **本地运行**：`bazel run //packages/base/ocr/vehicle/server:server-bin`；Spring Boot 规则产物：`//packages/base/ocr/vehicle/server:server-exec`。
- **Maven 导出**：当前 `vehicle/server/BUILD.bazel` **未** 声明 `export()`/`package` 类 target，**待团队补充**是否在其他位置发布或仅 Bazel 构建部署。

## 对外契约

- **HTTP**：应用内路径为 `POST /ocr/vehicle-number`（Controller 无类级 `@RequestMapping`）。**部署侧网关前缀**（`server_info`）：`/ocr-vehicle/`。对外完整路径常见为 **`/ocr-vehicle/ocr/vehicle-number`**（是否 strip 前缀以网关配置为准）。
- **服务发现**：`spring.application.name` 为 **`ocrVehicle`**（见 `bootstrap.yaml`），供 Feign/负载均衡引用（本仓库内 **未见** 专用 Feign Client 模块，**待团队补充**）。
- **字典配置**：厂家密钥等通过字典项 **`VEHICLE_NUMBER_OCR`** + 厂家枚举名（如 `ALI`）的 `variable` 字段注入（JSON，阿里云见 `AliDictConfig` 字段）。

## 数据与配置

- **spring.application.name**：`ocrVehicle`（`vehicle/server/resources/bootstrap.yaml`）。
- **端口**：`${common-env.ocr-vehicle.port:11111}`（`application.yaml`，实际值以 Nacos `common-env` 为准）。
- **配置导入**：`application.yaml` 从 Nacos 引入 `common-env`、`common-jackson`、安全、Feign、平台 HTTP、**RocketMQ**、Redis 等；本服务代码中 **未发现** RocketMQ 消费者，是否启用 MQ **待团队补充**。
- **数据源**：`server_info` 中 `no_datasource = True`，无本模块库表元数据。

## 深度阅读

- [references/overview.md](./references/overview.md)（目录树、target、本地运行）
- [references/external-api.md](./references/external-api.md)（Bazel 标签、路由、API 形态）
