# Cube2

基于 Bazel 构建的 Java/Spring Boot monorepo，内置 AI 智能开发基础设施。

---

## 目录

- [环境要求](#环境要求)
- [IDE 配置](#ide-配置)
- [项目结构](#项目结构)
- [构建与运行](#构建与运行)
- [依赖管理](#依赖管理)
- [依赖分析](#依赖分析)
- [AI 辅助开发](#ai-辅助开发)
- [注意事项](#注意事项)

---

## 环境要求

| 依赖 | 要求 | 备注 |
|------|------|------|
| JDK | 21+ | 推荐 [jenv](https://github.com/jenv/jenv) 管理多版本 |
| Bazel / Bazelisk | 见 `.bazelversion`（当前 **8.4.2**） | 推荐用 [Bazelisk](https://bazel.google.cn/install?hl=zh-cn) 自动匹配版本 |
| Bun | 最新版 | [安装](https://bun.com)；安装后在项目根执行 `bun ci` |
| gcc | 类 Unix 系统必须 | Windows 推荐使用 WSL |
| git | 必须 | — |
| rustup | 可选 | 仅 Rust 相关开发需要 |

---

## IDE 配置

### IntelliJ IDEA

1. 通过 [Toolbox](https://www.jetbrains.com.cn/toolbox-app/) 安装最新版 IDEA
2. 安装 **Bazel (EAP)** 插件并重启
3. 下载 [buildifier](https://github.com/bazelbuild/buildtools)，在 `构建、执行、部署 → 构建工具 → Bazel` 中配置 `Buildifier Executable Path`
4. 打开项目根目录后执行 `Sync`；可执行目标同步后会出现运行小绿标
5. 下载并导入[代码风格配置](http://git.guhecloud.com:8000/cube/standards/format/-/tree/main/java?ref_type=heads)

> **局部加载**：整仓 Sync 耗时较长时，可右键 `.projects/*.bazelproject` 选择 **Load Project View**，仅加载目标模块依赖。个人组合规则可写在 `.bazelbsp/.bazelproject`（不提交）。

> **首次 debug 未执行**：停止后再启动一次即可。

---

## 项目结构

```text
packages/
├── apps/           # 业务应用（各领域 server，部分含 client/adapter/sdk）
├── base/           # 基础域能力（ouaa、message、device-center、alarm、file 等可复用子域）
├── platform/       # 横切平台能力（base 通用库、database、security、rocketmq、redis 等）
├── customization/  # 客户/项目定制
└── deploy/         # 聚合与网关制品（server-one 变体、gateway；含 tools/ 供各服务 BUILD 引用）

legacy/             # 历史遗留依赖，无迭代，走制品依赖
tools/
└── springboot/     # springboot.bzl — 对 rules_spring 的封装
third-party/
└── java/           # 将 Maven 坐标封装为可引用的 Bazel 标签
bazel-deps-server/  # 依赖分析服务源码

MODULE.bazel        # Bazel 模块规则（版本变量 + maven.install）
MODULE.bazel.lock   # Bazel 模块锁文件，勿轻易修改
maven_install.json  # Maven 依赖锁文件，通过 repin 更新
.bazelversion       # 固定 Bazel 版本，供 Bazelisk 读取
```

---

## 构建与运行

```bash
# 构建 client
bazel build //packages/base/ouaa/client

# 构建 server
bazel build //packages/base/ouaa/server

# 运行 server（java_binary）
bazel run //packages/base/ouaa/server:server-bin

# 打包 server library
bazel build //packages/base/ouaa/server:package

# 发布到私有 Maven 仓库（需预先设置环境变量 MAVEN_REPO / MAVEN_USER / MAVEN_PASSWORD）
bazel run //packages/base/ouaa/server:package.publish

# 构建 server-one 聚合包
bazel build //packages/deploy/server-one/ghy-prod:ghy-prod

# 构建并运行 server-one 可执行包（类 Unix）
bazel run //packages/deploy/server-one/ghy-prod:ghy-prod-spring-exec
```

---

## 依赖管理

### 新增或修改 Maven 依赖

1. 在 `MODULE.bazel` 中添加/修改 Maven 坐标
2. 在 `third-party/java/<vendor>/BUILD.bazel` 中封装为 Bazel 标签（**业务模块 BUILD.bazel 中严禁直接使用 `@maven//:...`**）
3. 执行 repin 更新锁文件：

```bash
REPIN=1 bazel run @maven//:pin
```

> `fail_if_repin_required = True` 已开启，跳过 repin 会导致所有构建失败。

### 合并冲突

`maven_install.json` 出现冲突时，**统一使用本地分支的最新版本**。

---

## 依赖分析

### Bazel Query

```bash
# 分析某目标对特定包的直接/间接依赖
bazel query 'somepath(//packages/..., @maven//:org_springframework_security_oauth_spring_security_oauth2)' \
  --noimplicit_deps --output graph > /tmp/graph.in

# 分析 server 的完整依赖图
bazel query 'deps(//packages/base/ouaa/server:server)' --output graph > /tmp/graph.in

# 仅显示显式依赖（过滤隐式）
bazel query 'kind("java_library", deps(//packages/base/...))' --noimplicit_deps --output graph > /tmp/graph.in

# 将 graph 转为 SVG（需安装 graphviz）
dot -Tsvg < /tmp/graph.in > ~/Downloads/graph.svg
```

> 查询语言文档：<https://bazel.google.cn/query/language?hl=zh-cn>

### 依赖分析服务

```bash
bun run deps-server        # 启动服务
bun run deps-server:dev    # 监听模式（热重载）
```

---

## AI 辅助开发

Cube2 内置了 AI 智能基础设施，帮助 AI 精准定位模块、解析依赖、校验构建，减少上下文污染与幻觉输出。推荐配合 **Cursor IDE / Cursor CLI** 使用。

### 快速启用

**安装依赖**（`bun install` 时随根工作空间一并安装，一般无需单独执行）：

```bash
bun install   # 在仓库根目录执行
```

**Cursor IDE — 启用 MCP Server**

打开 `Cursor 设置 → Features → MCP`，找到 **cube2-harness** 并点击 **Enable**，展开后逐个开启以下工具：

`resolve_java_label` · `navigate_module` · `get_module_deps` · `validate_build` · `check_commit` · `list_outdated_skills` · `check_environment`

> MCP 指示灯为红色时：在仓库根执行 `bun install` 后重启 Cursor MCP 连接，或在对话中调用 `check_environment` 诊断。

**Cursor CLI — 开箱即用**

`.cursor/cli.json` 已预批准所有工具，无需额外配置：

```bash
agent          # 启动交互式 AI Agent
agent --plan   # 先规划再执行，适合大改动
```

### MCP 工具速查

| 工具 | 触发场景 |
|------|----------|
| `resolve_java_label` | 需要在 `deps` 中写某个 Java 类的 Bazel label 时 |
| `navigate_module` | 开始任务前了解目标模块的文档与边界 |
| `get_module_deps` | 查看某 target 的直接依赖，按平台层/基础层/第三方分组 |
| `validate_build` | 修改 BUILD.bazel 后验证构建，错误自动分类并给出建议 |
| `check_commit` | 提交前验证 message 格式是否符合 Cube2 规范 |
| `list_outdated_skills` | 检查哪些模块文档落后于源码 |
| `check_environment` | 工具链诊断，可选自动安装 node_modules |

### 典型工作场景

**修改模块依赖**

```
"help me add OuaaFeignClient as a dependency of packages/apps/alarm/server"
```

AI 自动执行：`navigate_module` 找到 alarm 文档 → `resolve_java_label` 确认 label → 修改 BUILD.bazel → `validate_build` 验证。

**分析依赖边界**

```
"分析 //packages/base/ouaa/server:server 的直接依赖"
```

AI 调用 `get_module_deps`，按层分组返回依赖列表，辅助判断越层依赖。

**提交前格式校验**

```
"check my commit message: feat(ouaa): add token refresh"
```

AI 调用 `check_commit`，返回 pass/fail 与修正建议。

**批量生成/刷新模块文档**

```
/batch-doc-gen   # 并行批量生成多个模块的 SKILL.md
/skill-writer    # 为单个模块生成或刷新文档
/build-fixer     # 自动诊断并修复 BUILD.bazel 错误
```

### AI 知识库结构

```text
.cursor/
├── rules/cube2.md          # 主协议，每次对话自动加载（核心术语、MCP 提示）
├── agents/                 # Sub-agent 定义
│   ├── skill-writer.md     # 为单个模块生成 SKILL.md
│   ├── skill-reviewer.md   # 对比代码变更与现有文档，发现不一致
│   ├── batch-doc-gen.md    # 并行批量生成模块文档
│   ├── build-fixer.md      # 诊断并修复 BUILD.bazel 错误
│   └── commit-checker.md   # 校验并润色提交信息
└── mcp.json                # MCP server 连接配置

.agents/
├── skills/cube2-repo/      # 仓库级 Skill（Bazel 规范、术语表、编码标准）
│   └── references/
│       ├── glossary.md             # 仓库专属术语表，防 AI 幻觉首选
│       ├── coding-standards.md     # 编码规范索引
│       └── module-skill-index.md   # 已建文档的模块清单
├── decisions/              # 架构决策记录（ADR）
└── specs/                  # 开放规格文档

cube2-harness/              # MCP Server 源码（Bun + TypeScript）
```

---

## 注意事项

- **Windows**：不要执行任何 `springboot` 规则，该规则基于 `sh` 脚本，无法在 Windows 上运行。需要在 Windows 开发时，请使用 WSL。
- **`MODULE.bazel.lock`**：Bazel 模块锁文件，勿轻易提交无关变更（仅在引入或更新规则版本时才会变化）。
- **`maven_install.json`**：通过 repin 更新，禁止手动编辑；合并冲突时统一采用本地分支版本。
- **IDEA 项目结构**：不要随意修改 `项目结构` 中任何设置，配置由 Bazel 插件自动管理。
- **`@maven//:...` 直写**：`packages/` 下业务模块的 `BUILD.bazel` 中严禁直接引用 `@maven//:...`，必须通过 `third-party/java/` 封装后引用。
- **YAML 文件后缀**：仓库内统一使用 `.yaml`，禁止使用 `.yml`。
