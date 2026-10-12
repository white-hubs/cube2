#!/usr/bin/env bun
/**
 * cube2-harness MCP Server
 * Cube2 仓库专用 AI 感知引擎，通过 Stdio 传输与 Cursor 集成。
 * 提供 7 个工具：resolve_java_label, navigate_module, get_module_deps,
 * validate_build, check_commit, list_outdated_skills, check_environment
 */

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

import { checkEnv } from "./lib/env-check.js";
import { resolveJavaLabel } from "./tools/resolve-java-label.js";
import { navigateModule } from "./tools/navigate-module.js";
import { getModuleDeps } from "./tools/get-module-deps.js";
import { validateBuild } from "./tools/validate-build.js";
import { checkCommit } from "./tools/check-commit.js";
import { listOutdatedSkills } from "./tools/list-outdated-skills.js";
import { checkEnvironment } from "./tools/check-environment.js";

// 启动期环境检测（捕获异常避免进程崩溃）
let envStatus = await checkEnv().catch((err: unknown) => {
  process.stderr.write(`[cube2-harness] 环境检测抛出异常：${err}\n`);
  return {
    ok: false,
    critical: [],
    optional: [],
    summary: "环境检测异常，请调用 check_environment 手动诊断。",
  };
});
if (!envStatus.ok) {
  process.stderr.write(
    `[cube2-harness] 环境检测失败：${envStatus.summary}\n` +
    envStatus.critical
      .filter((i) => i.status !== "ok")
      .map((i) => `  - ${i.name}: ${i.fix}`)
      .join("\n") + "\n" +
    "[cube2-harness] Server 仍将启动，bazel 相关工具将返回环境错误提示。\n"
  );
}

const server = new McpServer({
  name: "cube2-harness",
  version: "1.0.0",
});

// 工具注册：resolve_java_label
server.tool(
  "resolve_java_label",
  "将 Java 类名解析为其在 Cube2 仓库中对应的 Bazel label（如 //packages/base/ouaa/client:client）。用于确认 BUILD.bazel 的 deps 应写哪个标签。",
  { className: z.string().describe("Java 简单类名或全限定类名，如 OuaaFeignClient 或 com.example.OuaaFeignClient") },
  async ({ className }) => resolveJavaLabel(className, envStatus)
);

// 工具注册：navigate_module
server.tool(
  "navigate_module",
  "根据关键词或路径前缀找到最匹配的 Cube2 模块 SKILL.md 路径。用于在开始任务前快速定位模块文档入口，避免乱搜代码。",
  { query: z.string().describe("模块名、路径前缀或中文关键词，如 ouaa、packages/platform/security、数据库迁移") },
  async ({ query }) => navigateModule(query)
);

// 工具注册：get_module_deps
server.tool(
  "get_module_deps",
  "获取指定 Bazel target 的直接依赖列表（depth=1，过滤 java_library/java_binary）。用于了解模块依赖边界，检查是否有隐式依赖。",
  { target: z.string().describe("Bazel label，如 //packages/base/ouaa/server:server") },
  async ({ target }) => getModuleDeps(target, envStatus)
);

// 工具注册：validate_build
server.tool(
  "validate_build",
  "构建指定 Bazel target 并将错误格式化为 AI 友好的修复建议（分类：visibility 错误、undeclared 依赖、no-such-target 等）。",
  { target: z.string().describe("Bazel label 或通配符，如 //packages/base/ouaa/... 或 //packages/base/ouaa/server:server") },
  async ({ target }) => validateBuild(target, envStatus)
);

// 工具注册：check_commit
server.tool(
  "check_commit",
  "验证提交信息是否符合 Cube2 提交规范（action(scope): subject 或 {action(scope), action(scope)}: subject）。返回 pass/fail 与修正建议。",
  { message: z.string().describe("完整的 git commit message 标题行") },
  async ({ message }) => checkCommit(message)
);

// 工具注册：list_outdated_skills
server.tool(
  "list_outdated_skills",
  "列出 module-skill-index.md 中已登记但 SKILL.md 更新时间早于源码的模块。用于发现文档腐化，触发 skill-reviewer 同步。",
  {},
  async () => listOutdatedSkills()
);

// 工具注册：check_environment
server.tool(
  "check_environment",
  "检测 cube2-harness 运行所需的环境前提（node_modules、bazel、git）。可选 autoInstall=true 自动执行 bun install 并验证结果。当其他工具返回环境错误时优先调用此工具。",
  {
    autoInstall: z.boolean().optional().describe("是否自动安装缺失的 node_modules（仅限 bun install，默认 false）"),
    verify: z.boolean().optional().describe("autoInstall 后是否重新验证环境（默认 true）"),
  },
  async ({ autoInstall, verify }) => checkEnvironment(autoInstall ?? false, verify ?? true)
);

// 启动
const transport = new StdioServerTransport();
await server.connect(transport);
