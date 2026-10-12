import { checkEnv } from "../lib/env-check.js";
import { join } from "path";

export async function checkEnvironment(
  autoInstall: boolean,
  verify: boolean
): Promise<{ content: [{ type: "text"; text: string }] }> {
  const before = await checkEnv();

  if (before.ok) {
    return {
      content: [{
        type: "text",
        text: `✅ ${before.summary}\n\n**详情：**\n${formatStatus(before)}`,
      }],
    };
  }

  // 有问题时
  const hasNodeModulesMissing = before.critical.some(
    i => i.name === "node_modules" && i.status === "missing"
  );

  if (!autoInstall) {
    return {
      content: [{
        type: "text",
        text: `⚠️ ${before.summary}\n\n**详情：**\n${formatStatus(before)}\n\n` +
          `**修复步骤：**\n${before.critical.filter(i => i.status !== "ok").map(i => `- ${i.name}：${i.fix}`).join("\n")}\n\n` +
          `如需自动安装 node_modules，请调用 \`check_environment\` 并传入 \`autoInstall: true\`。`,
      }],
    };
  }

  // autoInstall = true
  if (!hasNodeModulesMissing) {
    return {
      content: [{
        type: "text",
        text: `⚠️ ${before.summary}\n\nautoInstall 仅能安装 node_modules，其他问题需手动修复：\n${formatStatus(before)}`,
      }],
    };
  }

  // 执行 bun install
  process.stderr.write("[cube2-harness] 正在执行 bun install...\n");
  const installResult = Bun.spawnSync(
    ["bun", "install"],
    {
      cwd: join(import.meta.dir, ".."),
      stdout: "pipe",
      stderr: "pipe",
    }
  );

  const installOut = new TextDecoder().decode(installResult.stdout);
  const installErr = new TextDecoder().decode(installResult.stderr);

  if (installResult.exitCode !== 0) {
    return {
      content: [{
        type: "text",
        text: `❌ bun install 失败（exit ${installResult.exitCode}）：\n${installErr}\n\n请手动在仓库根目录执行 \`bun install\`。`,
      }],
    };
  }

  if (!verify) {
    return {
      content: [{
        type: "text",
        text: `✅ bun install 完成。\n\n输出：${installOut.slice(-500)}`,
      }],
    };
  }

  // 重新验证
  const after = await checkEnv();
  return {
    content: [{
      type: "text",
      text: `**安装前：** ${before.summary}\n**安装后：** ${after.summary}\n\n**当前状态：**\n${formatStatus(after)}\n\n` +
        (after.ok ? "✅ 环境就绪，cube2-harness 工具可正常使用。" : "⚠️ 仍有问题，请按上述修复建议处理。"),
    }],
  };
}

function formatStatus(status: import("../lib/env-check.js").EnvStatus): string {
  const lines: string[] = [];
  for (const item of [...status.critical, ...status.optional]) {
    const icon = item.status === "ok" ? "✅" : item.status === "mismatch" ? "⚠️" : "❌";
    lines.push(`${icon} **${item.name}**：${item.detail}${item.fix ? `\n   修复：${item.fix}` : ""}`);
  }
  return lines.join("\n");
}
