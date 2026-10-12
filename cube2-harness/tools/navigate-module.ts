import { existsSync, readFileSync } from "fs";
import { join } from "path";


const MODULE_INDEX_PATH = ".agents/skills/cube2-repo/references/module-skill-index.md";

interface ModuleEntry {
  path: string;
  type: string;
  name: string;
  description: string;
}

function parseModuleIndex(): ModuleEntry[] {
  const indexFile = join(process.cwd(), MODULE_INDEX_PATH);
  if (!existsSync(indexFile)) return [];

  const content = readFileSync(indexFile, "utf-8");
  const entries: ModuleEntry[] = [];

  for (const line of content.split("\n")) {
    // 匹配表格行：| `packages/...` | 分流/叶子 | `name` | 描述 |
    const match = line.match(/^\|\s*`([^`]+)`\s*\|\s*([^|]+)\|\s*`([^`]+)`\s*\|\s*([^|]+)\|/);
    if (match) {
      entries.push({
        path: match[1].trim(),
        type: match[2].trim(),
        name: match[3].trim(),
        description: match[4].trim(),
      });
    }
  }
  return entries;
}

function score(entry: ModuleEntry, query: string): number {
  const q = query.toLowerCase();
  const pathLower = entry.path.toLowerCase();
  const nameLower = entry.name.toLowerCase();
  const descLower = entry.description.toLowerCase();

  if (pathLower === q || nameLower === q) return 100;
  if (pathLower.includes(q) || nameLower.includes(q)) return 80;
  if (descLower.includes(q)) return 60;
  // 按词匹配
  const words = q.split(/[\s\-_\/]+/).filter(w => w.length > 1);
  const matched = words.filter(w => pathLower.includes(w) || descLower.includes(w));
  if (matched.length > 0) return 30 + (matched.length / words.length) * 30;
  return 0;
}

export async function navigateModule(
  query: string
): Promise<{ content: [{ type: "text"; text: string }] }> {
  const entries = parseModuleIndex();

  if (entries.length === 0) {
    return {
      content: [{
        type: "text",
        text: `未找到模块索引文件（${MODULE_INDEX_PATH}）。请确认仓库结构完整。`,
      }],
    };
  }

  const scored = entries
    .map(e => ({ entry: e, score: score(e, query) }))
    .filter(x => x.score > 0)
    .sort((a, b) => b.score - a.score)
    .slice(0, 3);

  if (scored.length === 0) {
    return {
      content: [{
        type: "text",
        text: `未找到匹配 "${query}" 的模块。\n可用模块清单见：${MODULE_INDEX_PATH}`,
      }],
    };
  }

  const lines = scored.map(({ entry }) => {
    const skillPath = `${entry.path}/SKILL.md`;
    const skillExists = existsSync(join(process.cwd(), skillPath));
    const skillNote = skillExists ? `\`${skillPath}\`` : `\`${skillPath}\`（⚠️ 文件尚未创建，可运行 \`/skill-writer\` 生成）`;
    return `- **${entry.path}**（${entry.type}）\n  SKILL：${skillNote}\n  说明：${entry.description}`;
  });

  return {
    content: [{
      type: "text",
      text: `找到 ${scored.length} 个匹配模块：\n\n${lines.join("\n\n")}`,
    }],
  };
}
