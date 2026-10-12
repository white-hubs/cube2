import { spawn } from "bun";
import type { EnvStatus } from "../lib/env-check.js";

const BUILD_TIMEOUT_MS = 120_000;

interface BuildError {
  type: "visibility" | "undeclared_dep" | "no_such_target" | "compile" | "unknown";
  raw: string;
  suggestion: string;
}

function classifyError(stderr: string): BuildError[] {
  const errors: BuildError[] = [];
  const lines = stderr.split("\n");

  for (const line of lines) {
    if (line.includes("visibility")) {
      errors.push({
        type: "visibility",
        raw: line.trim(),
        suggestion: `可见性错误：目标对当前包不可见。检查目标的 visibility 属性，或将其改为 //packages:__subpackages__。`,
      });
    } else if (line.includes("no such target") || line.includes("no such package")) {
      const match = line.match(/'([^']+)'/);
      const label = match ? match[1] : "（未知标签）";
      errors.push({
        type: "no_such_target",
        raw: line.trim(),
        suggestion: `标签 \`${label}\` 不存在。请使用 resolve_java_label 或检查 BUILD.bazel 中的 target 名称拼写。`,
      });
    } else if (
      line.includes("cannot find symbol") ||
      line.includes("does not contain symbol") ||
      line.includes("package does not exist") ||
      line.includes("undeclared inclusion") ||
      line.includes("missing dep")
    ) {
      errors.push({
        type: "undeclared_dep",
        raw: line.trim(),
        suggestion: `缺少依赖声明：需要在 BUILD.bazel 的 deps 中添加对应标签。使用 resolve_java_label 找到正确的 label。`,
      });
    } else if (line.includes("error:") && line.includes(".java")) {
      errors.push({
        type: "compile",
        raw: line.trim(),
        suggestion: `Java 编译错误，需要修复源码。`,
      });
    }
  }

  return errors;
}

export async function validateBuild(
  target: string,
  envStatus: EnvStatus
): Promise<{ content: [{ type: "text"; text: string }] }> {
  if (!envStatus.ok) {
    return { content: [{ type: "text", text: `无法执行：${envStatus.summary}\n请先调用 check_environment 修复环境。` }] };
  }

  const startTime = Date.now();

  return new Promise((resolve) => {
    const proc = spawn({
      cmd: ["bazel", "build", target],
      cwd: process.cwd(),
      stdout: "pipe",
      stderr: "pipe",
    });

    let stderr = "";
    proc.stderr?.pipeTo(
      new WritableStream({ write(chunk) { stderr += new TextDecoder().decode(chunk); } })
    );

    const timer = setTimeout(() => {
      proc.kill();
      resolve({
        content: [{
          type: "text",
          text: `⏱️ 构建超时（>${BUILD_TIMEOUT_MS / 1000}s）：\`${target}\`\n\n可能原因：\n- 冷缓存首次构建耗时较长\n- target 范围过大（如 //packages/...）\n\n建议：缩小 target 范围后重试，或在终端直接运行 \`bazel build ${target}\`。`,
        }],
      });
    }, BUILD_TIMEOUT_MS);

    proc.exited.then((code) => {
      clearTimeout(timer);
      const elapsed = ((Date.now() - startTime) / 1000).toFixed(1);

      if (code === 0) {
        resolve({
          content: [{
            type: "text",
            text: `✅ 构建成功：\`${target}\`（${elapsed}s）`,
          }],
        });
        return;
      }

      const errors = classifyError(stderr);
      const uniqueErrors = errors.filter((e, i, arr) =>
        arr.findIndex(x => x.type === e.type && x.raw === e.raw) === i
      );

      let text = `❌ 构建失败：\`${target}\`（${elapsed}s）\n\n`;

      if (uniqueErrors.length > 0) {
        text += `**诊断（${uniqueErrors.length} 个问题）：**\n\n`;
        uniqueErrors.forEach((err, i) => {
          text += `${i + 1}. **[${err.type}]** ${err.suggestion}\n   > ${err.raw}\n\n`;
        });
      } else {
        text += `**原始错误（摘要）：**\n\`\`\`\n${stderr.slice(-2000)}\n\`\`\`\n`;
      }

      text += `\n**下一步**：根据上述诊断修复 BUILD.bazel 或源码后，再次调用 validate_build 验证。`;

      resolve({ content: [{ type: "text", text }] });
    });
  });
}
