import { existsSync, readFileSync, statSync } from "fs";
import { join } from "path";

const MODULE_INDEX_PATH = ".agents/skills/cube2-repo/references/module-skill-index.md";

function getGitMtime(path: string): number | null {
  const result = Bun.spawnSync(
    ["git", "log", "-1", "--format=%ct", "--", path],
    { cwd: process.cwd(), stdout: "pipe", stderr: "pipe" }
  );
  if (result.exitCode !== 0) return null;
  const ts = parseInt(new TextDecoder().decode(result.stdout).trim(), 10);
  return isNaN(ts) ? null : ts;
}

function getFileMtime(path: string): number | null {
  try {
    return Math.floor(statSync(join(process.cwd(), path)).mtimeMs / 1000);
  } catch {
    return null;
  }
}

function parseModulePaths(): string[] {
  const indexFile = join(process.cwd(), MODULE_INDEX_PATH);
  if (!existsSync(indexFile)) return [];
  const content = readFileSync(indexFile, "utf-8");
  const paths: string[] = [];
  let inCodeBlock = false;
  for (const line of content.split("\n")) {
    if (line.trim().startsWith("```")) { inCodeBlock = !inCodeBlock; continue; }
    if (inCodeBlock) continue;
    const match = line.match(/^\|\s*`(packages\/[^`<>]+)`/);
    if (match) paths.push(match[1]);
  }
  return paths;
}

export async function listOutdatedSkills(): Promise<{ content: [{ type: "text"; text: string }] }> {
  // 检测 git 是否可用
  const gitCheck = Bun.spawnSync(["which", "git"], { stdout: "pipe", stderr: "pipe" });
  if (gitCheck.exitCode !== 0) {
    return {
      content: [{
        type: "text",
        text: `git 不可用，无法比较时间戳。请安装 git 后重试。`,
      }],
    };
  }

  const modulePaths = parseModulePaths();
  if (modulePaths.length === 0) {
    return {
      content: [{
        type: "text",
        text: `未找到模块索引（${MODULE_INDEX_PATH}）或索引为空。`,
      }],
    };
  }

  const outdated: string[] = [];
  const noSkill: string[] = [];

  for (const modPath of modulePaths) {
    const skillPath = `${modPath}/SKILL.md`;
    if (!existsSync(join(process.cwd(), skillPath))) {
      noSkill.push(modPath);
      continue;
    }

    const skillMtime = getGitMtime(skillPath) ?? getFileMtime(skillPath);
    // 检查该模块目录下源码的最后提交时间
    const srcMtime = getGitMtime(modPath);

    if (skillMtime !== null && srcMtime !== null && srcMtime > skillMtime) {
      const diff = Math.floor((srcMtime - skillMtime) / 86400);
      outdated.push(`- \`${modPath}\` — 源码比 SKILL.md 新 ${diff} 天`);
    }
  }

  const lines: string[] = [];
  if (outdated.length > 0) {
    lines.push(`**⚠️ 文档可能过期（${outdated.length} 个模块）：**\n${outdated.join("\n")}`);
    lines.push(`\n建议对上述模块运行 \`/skill-reviewer\` 检查并同步文档。`);
  }
  if (noSkill.length > 0) {
    lines.push(`\n**未建文档的模块（${noSkill.length} 个）：**\n${noSkill.slice(0, 10).map(p => `- \`${p}\``).join("\n")}${noSkill.length > 10 ? `\n...等共 ${noSkill.length} 个` : ""}`);
    lines.push(`\n可使用 \`/batch-doc-gen\` 批量生成文档。`);
  }
  if (lines.length === 0) {
    lines.push(`✅ 所有已登记模块的文档均为最新（检查了 ${modulePaths.length} 个模块）。`);
  }

  return { content: [{ type: "text", text: lines.join("\n") }] };
}
