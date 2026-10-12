# base/ocr — 对外契约（external-api）

## 对外 Bazel 标签

| 用途 | 标签 |
|------|------|
| 服务端 `java_library` | `//packages/base/ocr/vehicle/server:server` |
| 可执行 `java_binary` | `//packages/base/ocr/vehicle/server:server-bin` |
| Spring Boot 可执行（rules_spring 封装） | `//packages/base/ocr/vehicle/server:server-exec` |
| `server_info`（网关前缀、server_id 等元数据） | `//packages/base/ocr/vehicle/server:server-info` |
| 依赖聚合（私有） | `//packages/base/ocr/vehicle/server:deps` |

**说明**：`packages/base/ocr` 根目录无 `BUILD.bazel`；未发现 `client` 子包或 `export` Maven 导出 target，跨模块 Java 依赖方式 **待团队补充**。

## HTTP 接口路由前缀

- **网关前缀**（`server_info.gw_prefix`）：`/ocr-vehicle/`
- **应用内路径**（`VehicleOcrController`）：`POST ocr/vehicle-number`
- **拼接示例**（网关不 strip 时）：`/ocr-vehicle/ocr/vehicle-number`；若网关配置了 strip 或 rewrite，以部署配置为准（**待团队补充**）。

## OCR 识别 API

### `POST .../ocr/vehicle-number`

- **Content-Type**：Controller 使用 `@RequestBody`，通常为 **`application/json`**（请求体为 `OcrVehicleReq`）。`MultipartFile` 字段在 JSON 场景下的实际调用方式 **待团队补充**（若走 multipart/form-data，需与网关/客户端约定是否另有接口）。

#### 请求体 `OcrVehicleReq`（字段）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| `vehicleOcrManufacture` | `VehicleOcrManufactureEnum` | `@NotNull` | 厂家，当前枚举仅 **`ALI`** |
| `ocrFileProvideType` | `OcrFileProvideType` | `@NotNull` | `URL` / `FILE` / `BASE64` |
| `imgUrl` | `String` | 条件必填 | `ocrFileProvideType == URL` 时必填 |
| `imgBase64` | `String` | 条件必填 | `BASE64` 时必填（**阿里云实现当前抛出不支持 Base64**，与业务预期是否一致 **待团队补充**） |
| `imgFile` | `MultipartFile` | 条件必填 | `FILE` 时必填 |
| `imgFileVO` | `FileVO` | 可选 | 文件元数据；阿里云实现会校验后缀是否支持 |

#### 响应体 `OcrVehicleRes`

| 字段 | 类型 | 说明 |
|------|------|------|
| `vehicleNumber` | `String` | 识别出的车牌号 |
| `vehicleType` | `String` | 车辆类型（如阿里云返回解析） |
| `extra` | `Object` | 预留扩展 |

#### 侧车：字典与厂家配置

- 字典项编码：**`VEHICLE_NUMBER_OCR`**，子项与厂家枚举名一致（如 `ALI`），`variable` 为厂家 JSON 配置。
- **阿里云**（`AliDictConfig`）：`accessKey`、`secretKey`、`region`、`endpoint`（JSON 反序列化后校验非空）。

## MQ 消息

- 本模块 Java 源码中 **未发现** RocketMQ 消费者/生产者注解或监听器。
- `application.yaml` 仍引入 Nacos 的 `common-rocketmq.yaml`，是否在其他配置类中启用 **待团队补充**。
