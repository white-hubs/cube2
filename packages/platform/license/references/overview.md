# platform/license 综述

- 仓库内直接 `BUILD` 引用较少；按需显式加入 `deps`。

## 模块综述与边界

本模块是 **许可证校验与展示相关的平台基础库**：基于 TrueLicense 提供证书安装/校验、自定义 `LicenseManager`（含服务器硬件信息校验）、Servlet 过滤器、以及对外只读查询证书信息的 REST 端点（`/public/license`）等，供业务应用在依赖中显式引入后装配使用。

**不负责**：许可证签发策略、商业授权规则、法务结论；也不承担「许可证管理服务」的独立部署与运维——后者见 `packages/base/license`（管理服务）等与产品侧约定。**与 `base/license` 区分**：本目录为可被引用的 **库**；`base/license` 为 **管理服务** 形态（以仓库内实际 `SKILL.md` / 部署说明为准）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

- **`BUILD.bazel`**：本模块 Bazel 目标与 Maven 导出。
- **`src/`**（Java 源码，包根 `com.guhecloud.platform.license`）：
  - **根包**：`LicenseManagerHolder`、`LicenseVerifyHolder`、启动阶段装配用的 `LicenseCheckListener`（读取 `license.*` 配置）。
  - **`controller/`**：`LicenseController`，对外 `GET /public/license` 返回证书摘要（`LicenseVO`）。
  - **`filter/`**：`LicenseValidationFilter`，请求链上校验许可证（放行 `/public/license` 等逻辑见源码）。
  - **`manage/`**：`CustomLicenseManager`，扩展 TrueLicense 的创建/安装/校验与服务端信息绑定。
  - **`verify/`**、**`verify/param/`**：`LicenseVerify` 与 `LicenseVerifyParam`，安装与校验证书。
  - **`keystore/`**：`CustomKeyStoreParam`，密钥库参数封装。
  - **`notary/`**：`RsaLicenseNotary`，与证书签名/公证相关扩展。
  - **`model/`**、**`vo/`**：校验模型与对外展示 VO。
  - **`util/`**：`ServerInfoUtils` 及按 OS 采集机器信息的实现类（供许可证与机器绑定等场景）。

## BUILD target 说明

| Target | 用途 |
|--------|------|
| `//packages/platform/license:license` | `java_library`，本模块全部 Java 源码，供其他包在 `deps` 中引用。 |
| `//packages/platform/license:export` | `java_export`，发布 Maven 构件 `platform-license`（坐标见 `BUILD.bazel` 中 `PACKAGE_GROUP` / `PLATFORM_VERSION`）。 |
| `//packages/platform/license:bom` | `maven_bom`，聚合本模块导出构件的 BOM，供依赖对齐版本。 |

可见性：`BUILD.bazel` 中默认对 `//packages/base/license/server/...` 与 `//packages/platform/security/...` 开放；其他包若需引用须符合仓库可见性约定或调整 `package` 可见性（待团队补充具体策略）。

## 本地运行

本模块为纯库，不独立运行。集成方通过依赖本 target 并在宿主 Spring Boot 应用中加载相关 Bean/过滤器后验证行为；本地联调方式与配置样例见团队文档或消费方模块说明。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `//packages/platform/license:license` 的 `deps` 摘要（「提供什么能力」为便于理解的概括；以源码与 BUILD 为准）：

| Bazel 依赖 | 说明 |
|------------|------|
| `//packages/platform/base` | 平台基础能力聚合。 |
| `//packages/platform/base/model:error-response` | 统一错误响应模型（过滤器写入 HTTP 响应等）。 |
| `//packages/platform/base/standard:exception` | 标准业务异常（如 Controller 抛出）。 |
| `//packages/platform/base/utils:assert` 等 | 断言、日期、JSON、Spring 文件、字符串等工具。 |
| `//packages/platform/web` | 平台 Web 相关基础（与 Spring MVC 等集成）。 |
| `//third-party/java:lombok` | Lombok 注解。 |
| `//third-party/java/apache:tomcat` | Servlet API（过滤器等）。 |
| `//third-party/java/jackson` | JSON 序列化。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:spring_boot`、`//third-party/java/spring:springframework` | Spring Boot / Spring Framework（Controller、事件监听、过滤器等）。 |
| `//third-party/java/truelicense` | TrueLicense 证书安装、校验与 `LicenseManager` 基础设施。 |

## 常见开发场景

1. **消费方引入本库并在应用中启用校验**  
   - 在消费模块的 `BUILD.bazel` 中为 `java_library` / `java_binary` 增加 `//packages/platform/license:license`（或经 BOM 对齐的 Maven 坐标，待团队补充发布消费方式）。  
   - 配置 `license.subject`、`license.publicAlias`、`license.storePass`、`license.licensePath`、公钥库路径等（见 `LicenseCheckListener` 与 `LicenseVerifyParam` 使用处）。  
   - 注册 `LicenseValidationFilter`、`LicenseCheckListener` 等 Bean 的方式以宿主应用配置为准（待团队补充标准装配文档）。

2. **调整校验逻辑或机器信息绑定**  
   - 修改 `src/manage/CustomLicenseManager.java`、`src/util/*ServerInfos*.java` 等；注意与 TrueLicense 生命周期及安全约束，变更后跑宿主应用集成测试。

3. **对外证书信息接口变更**  
   - 修改 `src/controller/LicenseController.java` 与 `src/vo/LicenseVO.java`；同步更新依赖该路径的前端或其它服务契约（待团队补充 API 文档位置）。

## 注意事项

- 本库包含 **`/public/license` 只读接口**与**全局请求过滤器**；接入时须评估与网关、安全策略、以及「公开」路径白名单的一致性，避免误拦截或暴露敏感信息。  
- 证书与密钥路径、口令等属于敏感配置，须通过安全配置与部署规范注入，**禁止**硬编码或提交到仓库。  
- 与 **`packages/base/license`（管理服务）**的职责边界：业务「谁有权签发/吊销/上传证书」应在管理服务或运维流程中完成；本库侧重**运行时装载与校验**。若边界在演进中有调整，待团队补充权威说明。  
- 仓库内直接引用面较窄（见文首 bullet）；新增依赖本库的包应显式维护 `deps`，避免传递依赖假设。
