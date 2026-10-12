export async function checkCommit(
  message: string
): Promise<{ content: [{ type: "text"; text: string }] }> {
  // 来自 cube2-commit SKILL.md 的合法 action 列表
  const VALID_ACTIONS = ["feat", "fix", "docs", "chore", "refactor", "test", "style", "perf", "build", "ci"];

  const singlePattern = /^([a-z]+)\(([^)]+)\):\s+.+/;
  const multiPattern = /^\{([a-z]+\([^)]+\)(?:,\s*[a-z]+\([^)]+\))*)\}:\s+.+/;

  const trimmed = message.trim();

  // 检查多范围格式
  const multiMatch = trimmed.match(multiPattern);
  if (multiMatch) {
    const pairs = multiMatch[1].split(",").map(s => s.trim());
    const invalidActions: string[] = [];
    for (const pair of pairs) {
      const actionMatch = pair.match(/^([a-z]+)\(/);
      if (actionMatch && !VALID_ACTIONS.includes(actionMatch[1])) {
        invalidActions.push(actionMatch[1]);
      }
    }
    if (invalidActions.length > 0) {
      return {
        content: [{
          type: "text",
          text: `❌ 提交格式基本正确，但包含非法 action：${invalidActions.join("、")}\n合法 action：${VALID_ACTIONS.join(" / ")}\n\n原始：${trimmed}`,
        }],
      };
    }
    return { content: [{ type: "text", text: `✅ 多范围格式正确：${trimmed}` }] };
  }

  // 检查单范围格式
  const singleMatch = trimmed.match(singlePattern);
  if (singleMatch) {
    const action = singleMatch[1];
    if (!VALID_ACTIONS.includes(action)) {
      const suggestions = VALID_ACTIONS.filter(a => a.startsWith(action[0]));
      return {
        content: [{
          type: "text",
          text: `❌ action "${action}" 不合法。\n合法 action：${VALID_ACTIONS.join(" / ")}\n${suggestions.length > 0 ? `建议：${suggestions.join(" 或 ")}` : ""}\n\n原始：${trimmed}`,
        }],
      };
    }
    return { content: [{ type: "text", text: `✅ 单范围格式正确：${trimmed}` }] };
  }

  // 格式不匹配，生成修正建议
  const suggestion = generateSuggestion(trimmed, VALID_ACTIONS);
  return {
    content: [{
      type: "text",
      text: `❌ 格式不符合规范。\n\n**期望格式：**\n- 单范围：\`action(scope): 说明\`\n- 多范围：\`{action(scope), action(scope)}: 说明\`\n\n**合法 action：** ${VALID_ACTIONS.join(" / ")}\n\n**建议修正：**\n${suggestion}\n\n**原始：** ${trimmed}`,
    }],
  };
}

function generateSuggestion(message: string, validActions: string[]): string {
  // 尝试猜测 scope（取第一个有意义词）
  const words = message.split(/\s+/);
  const guessScope = words[0]?.toLowerCase().replace(/[^a-z0-9-]/g, "") || "scope";
  const subject = message || "说明变更内容";
  return `\`fix(${guessScope}): ${subject}\` 或 \`feat(${guessScope}): ${subject}\``;
}
