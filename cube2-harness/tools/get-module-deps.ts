import { executeBazelQuery } from "../lib/bazel.js";
import type { EnvStatus } from "../lib/env-check.js";

export async function getModuleDeps(
  target: string,
  envStatus: EnvStatus
): Promise<{ content: [{ type: "text"; text: string }] }> {
  if (!envStatus.ok) {
    return { content: [{ type: "text", text: `无法执行：${envStatus.summary}\n请先调用 check_environment 修复环境。` }] };
  }

  try {
    // deps(target, 1) 取直接依赖，再用 kind() 过滤只保留 java_library/java_binary
    const kindFilter = `kind("java_library|java_binary", //...)`;
    const query = `(deps(${target}, 1) except ${target}) intersect (${kindFilter})`;
    const result = await executeBazelQuery(query);
    const deps = result
      .split("\n")
      .map(l => l.trim())
      .filter(l => l.length > 0)
      .sort();

    if (deps.length === 0) {
      return { content: [{ type: "text", text: `${target} 没有直接依赖（或 target 不存在）。` }] };
    }

    const thirdParty = deps.filter(d => d.startsWith("//third-party"));
    const platformDeps = deps.filter(d => d.startsWith("//packages/platform"));
    const baseDeps = deps.filter(d => d.startsWith("//packages/base"));
    const otherDeps = deps.filter(d => !thirdParty.includes(d) && !platformDeps.includes(d) && !baseDeps.includes(d));

    const sections: string[] = [`**${target}** 的直接依赖（共 ${deps.length} 个）：`];
    if (platformDeps.length > 0) sections.push(`\n**平台层（${platformDeps.length}）：**\n${platformDeps.map(d => `  - \`${d}\``).join("\n")}`);
    if (baseDeps.length > 0) sections.push(`\n**基础层（${baseDeps.length}）：**\n${baseDeps.map(d => `  - \`${d}\``).join("\n")}`);
    if (otherDeps.length > 0) sections.push(`\n**其他（${otherDeps.length}）：**\n${otherDeps.map(d => `  - \`${d}\``).join("\n")}`);
    if (thirdParty.length > 0) sections.push(`\n**第三方（${thirdParty.length}）：**\n${thirdParty.map(d => `  - \`${d}\``).join("\n")}`);

    return { content: [{ type: "text", text: sections.join("") }] };
  } catch (error) {
    const msg = error instanceof Error ? error.message : String(error);
    return {
      content: [{
        type: "text",
        text: `查询依赖失败：${msg}\n\n可能原因：\n- target 不存在：检查 label 拼写\n- bazel 未初始化：先执行 bazel build //packages/... 热身`,
      }],
    };
  }
}
