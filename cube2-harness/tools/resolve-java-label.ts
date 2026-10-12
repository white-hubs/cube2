import { existsSync, readFileSync } from "fs";
import { join, relative, dirname } from "path";
import type { EnvStatus } from "../lib/env-check.js";

export async function resolveJavaLabel(
  className: string,
  _envStatus: EnvStatus
): Promise<{ content: [{ type: "text"; text: string }] }> {
  // 注意：此工具仅使用 find + 文件读取，不依赖 bazel，无需 envStatus.ok 门控

  // 提取简单类名（去掉包路径）
  const simpleName = className.includes(".") ? className.split(".").pop()! : className;
  const fileName = `${simpleName}.java`;

  // 在 packages/ 下递归查找 .java 文件
  // 使用 Bun.spawnSync 调用 find 命令
  const findResult = Bun.spawnSync(
    ["find", "packages", "-name", fileName, "-type", "f"],
    { cwd: process.cwd(), stdout: "pipe", stderr: "pipe" }
  );

  if (findResult.exitCode !== 0) {
    return { content: [{ type: "text", text: `查找文件失败：${new TextDecoder().decode(findResult.stderr)}` }] };
  }

  const found = new TextDecoder().decode(findResult.stdout)
    .split("\n")
    .map(l => l.trim())
    .filter(l => l.length > 0);

  if (found.length === 0) {
    return { content: [{ type: "text", text: `未找到 ${fileName}，请确认类名拼写正确。` }] };
  }

  // 对每个找到的文件，向上查找最近的 BUILD.bazel，推断 Bazel label
  const results: string[] = [];
  for (const filePath of found) {
    const label = await inferBazelLabel(filePath);
    results.push(`- \`${filePath}\` → \`${label}\``);
  }

  const text = found.length === 1
    ? `找到 ${simpleName}：\n${results[0]}`
    : `找到 ${found.length} 处匹配：\n${results.join("\n")}\n\n请根据上下文选择正确的 label。`;

  return { content: [{ type: "text", text }] };
}

/**
 * 从文件路径推断 Bazel label：向上找 BUILD.bazel，优先匹配含该文件名的 srcs，
 * 其次取第一个 java_library target 名。
 */
async function inferBazelLabel(filePath: string): Promise<string> {
  const cwd = process.cwd();
  const fileName = filePath.split("/").pop()!;
  let dir = join(cwd, dirname(filePath));

  // 向上最多 10 层找 BUILD.bazel
  for (let i = 0; i < 10; i++) {
    const buildFile = join(dir, "BUILD.bazel");
    if (existsSync(buildFile)) {
      const content = readFileSync(buildFile, "utf-8");
      const relDir = relative(cwd, dir).replace(/\\/g, "/");

      // 尝试找 srcs 中包含该文件名的 java_library target
      const targetName = findTargetBySrc(content, fileName) ??
        (content.match(/java_library\s*\(\s*name\s*=\s*"([^"]+)"/) ?? [])[1] ??
        "server";

      return `//${relDir}:${targetName}`;
    }
    const parent = dirname(dir);
    if (parent === dir) break;
    dir = parent;
  }
  // 兜底：用目录路径推断
  const relDir = relative(cwd, join(cwd, dirname(filePath))).replace(/\\/g, "/");
  return `//${relDir}:（未找到 BUILD.bazel，请手动确认）`;
}

/**
 * 在 BUILD.bazel 内容中找 srcs 包含指定文件名的 java_library target name。
 * 简单的文本扫描，处理 glob（如 srcs = glob(["**\/*.java"])）时直接返回 null。
 */
function findTargetBySrc(buildContent: string, fileName: string): string | null {
  // 匹配形如 java_library( name = "foo", ... srcs = ["Bar.java", ...], ... )
  const targetPattern = /java_library\s*\([^)]*name\s*=\s*"([^"]+)"[^)]*srcs\s*=\s*\[([^\]]*)\]/gs;
  let m: RegExpExecArray | null;
  while ((m = targetPattern.exec(buildContent)) !== null) {
    const name = m[1];
    const srcsBlock = m[2];
    if (srcsBlock.includes(fileName)) return name;
  }
  return null;
}
