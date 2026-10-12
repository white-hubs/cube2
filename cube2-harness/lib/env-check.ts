/**
 * 环境前提条件检测模块
 * 被 server.ts 启动期调用，也被 check-environment 工具直接调用。
 */

import { existsSync, readFileSync } from "fs";
import { join } from "path";

interface SkillsLock {
  version: number;
  skills: Record<string, { source: string; sourceType: string; computedHash: string }>;
}

export interface EnvItem {
  name: string;
  status: "ok" | "missing" | "mismatch";
  detail: string;
  fix: string;
}

export interface EnvStatus {
  ok: boolean;
  /** critical 缺失则所有 bazel 相关工具失效 */
  critical: EnvItem[];
  /** optional 缺失则局部降级 */
  optional: EnvItem[];
  /** 一句话摘要，供 MCP 工具直接返回给 AI */
  summary: string;
}

function which(cmd: string): boolean {
  const result = Bun.spawnSync(["which", cmd], { stdout: "pipe", stderr: "pipe" });
  return result.exitCode === 0;
}

function bazelVersion(): string | null {
  const result = Bun.spawnSync(["bazel", "version", "--gnu_format"], {
    stdout: "pipe",
    stderr: "pipe",
  });
  if (result.exitCode !== 0) return null;
  const out = new TextDecoder().decode(result.stdout);
  // 匹配 "bazel X.Y.Z" 行，避免误匹配 Bazelisk 自身的版本号
  const match = out.match(/^bazel\s+(\d+\.\d+\.\d+)/m);
  return match ? match[1] : null;
}

function expectedBazelVersion(): string | null {
  const versionFile = join(process.cwd(), ".bazelversion");
  if (!existsSync(versionFile)) return null;
  return readFileSync(versionFile, "utf-8").trim();
}

export async function checkEnv(): Promise<EnvStatus> {
  const critical: EnvItem[] = [];
  const optional: EnvItem[] = [];

  // --- Critical: node_modules ---
  const nodeModulesPath = join(import.meta.dir, "..", "node_modules");
  if (existsSync(nodeModulesPath)) {
    critical.push({
      name: "node_modules",
      status: "ok",
      detail: "cube2-harness/node_modules 存在",
      fix: "",
    });
  } else {
    critical.push({
      name: "node_modules",
      status: "missing",
      detail: "cube2-harness/node_modules 不存在，MCP server 无法启动",
      fix: "在仓库根目录执行：bun install",
    });
  }

  // --- Critical: bazel ---
  if (which("bazel")) {
    const actual = bazelVersion();
    const expected = expectedBazelVersion();
    if (actual && expected && actual !== expected) {
      critical.push({
        name: "bazel",
        status: "mismatch",
        detail: `当前版本 ${actual}，.bazelversion 要求 ${expected}`,
        fix: `安装 Bazelisk（https://github.com/bazelbuild/bazelisk）并确保 PATH 中的 bazel 通过 Bazelisk 代理`,
      });
    } else {
      critical.push({
        name: "bazel",
        status: "ok",
        detail: `bazel ${actual ?? "（版本未知）"} 可用`,
        fix: "",
      });
    }
  } else {
    critical.push({
      name: "bazel",
      status: "missing",
      detail: "bazel 不在 PATH 中，所有构建相关工具将失效",
      fix: "安装 Bazelisk：brew install bazelisk 或 npm install -g @bazel/bazelisk",
    });
  }

  // --- Optional: git ---
  if (which("git")) {
    optional.push({ name: "git", status: "ok", detail: "git 可用", fix: "" });
  } else {
    optional.push({
      name: "git",
      status: "missing",
      detail: "git 不在 PATH，list_outdated_skills 将降级跳过时间戳比较",
      fix: "安装 git：https://git-scm.com",
    });
  }

  // --- Optional: skills (skills-lock.json) ---
  const repoRoot = process.cwd();
  const skillsLockPath = join(repoRoot, "skills-lock.json");
  if (existsSync(skillsLockPath)) {
    let lock: SkillsLock | null = null;
    try {
      lock = JSON.parse(readFileSync(skillsLockPath, "utf-8")) as SkillsLock;
    } catch {
      optional.push({
        name: "skills-lock",
        status: "mismatch",
        detail: "skills-lock.json 存在但无法解析",
        fix: "检查 skills-lock.json 格式是否合法",
      });
    }
    if (lock?.skills) {
      const missing: string[] = [];
      for (const skillName of Object.keys(lock.skills)) {
        const skillFile = join(repoRoot, ".agents", "skills", skillName, "SKILL.md");
        if (!existsSync(skillFile)) {
          missing.push(skillName);
        }
      }
      if (missing.length === 0) {
        optional.push({
          name: "skills",
          status: "ok",
          detail: `skills-lock.json 中全部 ${Object.keys(lock.skills).length} 个 skill 已安装`,
          fix: "",
        });
      } else {
        optional.push({
          name: "skills",
          status: "missing",
          detail: `以下 skill 在 skills-lock.json 中声明但未安装：${missing.join("、")}`,
          fix: "在 Cursor 中通过 Skill 管理功能重新安装，或手动将对应 SKILL.md 放入 .agents/skills/<name>/",
        });
      }
    }
  }

  const criticalOk = critical.every((i) => i.status === "ok");
  const missingCritical = critical.filter((i) => i.status !== "ok");
  const missingOptional = optional.filter((i) => i.status !== "ok");

  let summary: string;
  if (criticalOk && missingOptional.length === 0) {
    summary = "环境就绪，所有工具可正常使用。";
  } else if (!criticalOk) {
    summary =
      `⚠️ 关键依赖缺失：${missingCritical.map((i) => i.name).join("、")}。` +
      `请先修复后再使用 cube2-harness 工具。修复方法：${missingCritical[0].fix}`;
  } else {
    summary = `环境基本就绪（部分可选工具降级：${missingOptional.map((i) => i.name).join("、")}）。`;
  }

  return {
    ok: criticalOk,
    critical,
    optional,
    summary,
  };
}
