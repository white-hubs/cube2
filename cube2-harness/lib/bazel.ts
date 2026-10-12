/**
 * Bazel query 核心工具函数
 * 提取自 bazel-deps-server/server.ts，供 cube2-harness MCP 工具复用。
 * 两个 server 互相独立，不产生跨目录依赖。
 */

import { spawn } from "bun";

export const DEFAULT_RULE_TYPES = ["java_library", "java_binary"];

const QUERY_TIMEOUT_MS = 30_000;

/** 执行 bazel query 并返回原始 stdout 字符串（超时 30s） */
export async function executeBazelQuery(
  query: string,
  outputFormat: string = "label"
): Promise<string> {
  return new Promise((resolve, reject) => {
    const proc = spawn({
      cmd: ["bazel", "query", query, `--output=${outputFormat}`],
      cwd: process.cwd(),
      stdout: "pipe",
      stderr: "pipe",
    });

    let stdout = "";
    let stderr = "";

    const timer = setTimeout(() => {
      proc.kill();
      reject(new Error(`bazel query 超时（>${QUERY_TIMEOUT_MS / 1000}s）：${query.slice(0, 120)}`));
    }, QUERY_TIMEOUT_MS);

    proc.stdout?.pipeTo(
      new WritableStream({
        write(chunk) {
          stdout += new TextDecoder().decode(chunk);
        },
      })
    );

    proc.stderr?.pipeTo(
      new WritableStream({
        write(chunk) {
          stderr += new TextDecoder().decode(chunk);
        },
      })
    );

    proc.exited.then((code) => {
      clearTimeout(timer);
      if (code === 0) {
        resolve(stdout);
      } else {
        reject(new Error(stderr.trim() || `bazel query exited with code ${code}`));
      }
    });
  });
}

/** 获取 target 的所有依赖（过滤到 java_library/java_binary） */
export async function getTargetDeps(
  target: string,
  ruleTypes: string[] = DEFAULT_RULE_TYPES
): Promise<string[]> {
  let query = `deps(${target})`;
  if (ruleTypes.length > 0) {
    const kindQueries = ruleTypes.map((r) => `kind("${r}", //...)`).join(" union ");
    query = `${query} intersect (${kindQueries})`;
  }
  const result = await executeBazelQuery(query);
  return result
    .split("\n")
    .map((l) => l.trim())
    .filter((l) => l.length > 0 && l !== target);
}

/** 解析 bazel query --output=graph 的 DOT 格式输出 */
export function parseGraphOutput(graphOutput: string): Map<string, string[]> {
  const depsTree = new Map<string, string[]>();
  for (const line of graphOutput.split("\n")) {
    const trimmed = line.trim();
    if (
      !trimmed ||
      trimmed.startsWith("digraph") ||
      trimmed.startsWith("}") ||
      trimmed === "{"
    )
      continue;
    const match = trimmed.match(/"([^"]+)"\s*->\s*"([^"]+)"/);
    if (match) {
      const [, from, to] = match;
      if (!depsTree.has(from)) depsTree.set(from, []);
      const deps = depsTree.get(from)!;
      if (!deps.includes(to)) deps.push(to);
    }
  }
  return depsTree;
}

/** 获取依赖树结构（Map: target → 直接依赖列表） */
export async function getDepsTree(
  target: string,
  ruleTypes: string[] = DEFAULT_RULE_TYPES
): Promise<Map<string, string[]>> {
  let query = `deps(${target})`;
  if (ruleTypes.length > 0) {
    const kindQueries = ruleTypes.map((r) => `kind("${r}", //...)`).join(" union ");
    query = `${query} intersect (${kindQueries})`;
  }
  try {
    const graphOutput = await executeBazelQuery(query, "graph");
    return parseGraphOutput(graphOutput);
  } catch {
    const labels = await getTargetDeps(target, ruleTypes);
    const tree = new Map<string, string[]>();
    tree.set(target, labels);
    return tree;
  }
}
