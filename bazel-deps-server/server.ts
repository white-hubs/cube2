import { serve } from "bun";
import { spawn } from "bun";

const PORT = 9110;

// 默认过滤的 rule 类型
const DEFAULT_RULE_TYPES = ["java_library", "java_binary"];

// 构建 rule 类型过滤查询
function buildRuleFilterQuery(ruleTypes: string[]): string {
  if (ruleTypes.length === 0) {
    return 'kind(rule, //...)';
  }
  // 构建类似 kind(java_library, //...) union kind(java_binary, //...) 的查询
  const kindQueries = ruleTypes.map(rule => `kind("${rule}", //...)`).join(' union ');
  return kindQueries;
}

// 执行 Bazel query 命令
async function executeBazelQuery(query: string, outputFormat: string = "label"): Promise<string> {
  return new Promise((resolve, reject) => {
    const proc = spawn({
      cmd: ["bazel", "query", query, `--output=${outputFormat}`],
      cwd: process.cwd(),
      stdout: "pipe",
      stderr: "pipe",
    });

    let stdout = "";
    let stderr = "";

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
      if (code === 0) {
        resolve(stdout);
      } else {
        reject(new Error(`Bazel query failed: ${stderr}`));
      }
    });
  });
}

// 获取所有源码级 target（支持 rule 类型过滤）
async function getAllSourceTargets(ruleTypes: string[] = DEFAULT_RULE_TYPES): Promise<string[]> {
  try {
    const filterQuery = buildRuleFilterQuery(ruleTypes);
    const result = await executeBazelQuery(filterQuery);
    const targets = result
      .split("\n")
      .map((line) => line.trim())
      .filter((line) => line.length > 0);
    return targets.sort();
  } catch (error) {
    console.error("Error getting source targets:", error);
    // 如果上面的查询失败，尝试更简单的查询
    try {
      const result = await executeBazelQuery("kind(rule, //...)");
      return result
        .split("\n")
        .map((line) => line.trim())
        .filter((line) => line.length > 0)
        .sort();
    } catch (fallbackError) {
      console.error("Fallback query also failed:", fallbackError);
      return [];
    }
  }
}

// 获取 target 的直接依赖关系（只返回直接依赖，不包括传递依赖）
// 注意：Bazel query 的 deps() 函数不支持深度参数，我们需要使用其他方法
async function getDirectDeps(target: string, ruleTypes: string[] = DEFAULT_RULE_TYPES): Promise<string[]> {
  try {
    // 使用 kind(rule, deps($target, 1)) 来获取深度为 1 的依赖
    // 但 Bazel 的 deps() 实际上不支持第二个参数
    // 我们需要使用 BUILD 文件中的直接依赖关系
    // 这里我们使用一个变通方法：获取所有依赖，然后过滤出直接依赖
    // 实际上，我们可以通过查询 BUILD 文件来获取直接依赖
    // 但更简单的方法是：先获取所有依赖，然后对每个依赖检查它是否在目标的直接依赖中
    
    // 尝试使用 deps() 配合其他查询
    // 由于 Bazel query 的限制，我们使用一个近似方法：
    // 获取所有依赖，然后通过分析依赖关系来确定直接依赖
    
    // 先获取所有依赖
    const allDeps = await getTargetDeps(target, ruleTypes);
    
    // 对于每个依赖，检查它是否可能是直接依赖
    // 我们通过检查该依赖是否不在其他依赖的依赖列表中来判断
    const directDeps: string[] = [];
    const indirectDeps = new Set<string>();
    
    // 对于每个依赖，获取它的依赖列表
    for (const dep of allDeps) {
      try {
        const depDeps = await getTargetDeps(dep, ruleTypes);
        depDeps.forEach(d => indirectDeps.add(d));
      } catch (error) {
        // 忽略错误，继续处理
      }
    }
    
    // 直接依赖 = 所有依赖 - 间接依赖
    allDeps.forEach(dep => {
      if (!indirectDeps.has(dep)) {
        directDeps.push(dep);
      }
    });
    
    return directDeps;
  } catch (error) {
    console.error(`Error getting direct deps for ${target}:`, error);
    // 如果失败，返回空数组，让调用者使用所有依赖
    return [];
  }
}

// 获取 target 的依赖关系（支持 rule 类型过滤）
async function getTargetDeps(target: string, ruleTypes: string[] = DEFAULT_RULE_TYPES): Promise<string[]> {
  try {
    let query = `deps(${target})`;
    // 如果指定了 rule 类型过滤，应用过滤
    if (ruleTypes.length > 0) {
      const filterQuery = buildRuleFilterQuery(ruleTypes);
      query = `${query} intersect ${filterQuery}`;
    }
    const result = await executeBazelQuery(query);
    return result
      .split("\n")
      .map((line) => line.trim())
      .filter((line) => line.length > 0 && line !== target);
  } catch (error) {
    console.error(`Error getting deps for ${target}:`, error);
    return [];
  }
}

// 解析 Bazel graph 输出格式（DOT 格式）
// 格式示例:
// digraph {
//   "//target1" -> "//target2"
//   "//target2" -> "//target3"
// }
function parseGraphOutput(graphOutput: string): Map<string, string[]> {
  const depsTree = new Map<string, string[]>();
  const lines = graphOutput.split('\n');
  
  for (const line of lines) {
    const trimmed = line.trim();
    // 跳过注释、空行、digraph 声明和结束大括号
    if (!trimmed || 
        trimmed.startsWith('digraph') || 
        trimmed.startsWith('}') || 
        trimmed.startsWith('//') ||
        trimmed.startsWith('/*') ||
        trimmed === '{') {
      continue;
    }
    
    // 解析格式: "target1" -> "target2" 或 "target1"->"target2"
    // 支持带引号和不带引号的格式
    const match = trimmed.match(/"([^"]+)"\s*->\s*"([^"]+)"/);
    if (match) {
      const from = match[1];
      const to = match[2];
      
      if (!depsTree.has(from)) {
        depsTree.set(from, []);
      }
      const deps = depsTree.get(from)!;
      if (!deps.includes(to)) {
        deps.push(to);
      }
    }
  }
  
  return depsTree;
}

// 获取依赖树结构（返回依赖关系映射）- 优化版本，使用 graph 输出
async function getDepsTree(target: string, ruleTypes: string[] = DEFAULT_RULE_TYPES): Promise<Map<string, string[]>> {
  try {
    // 构建查询
    let query = `deps(${target})`;
    if (ruleTypes.length > 0) {
      const filterQuery = buildRuleFilterQuery(ruleTypes);
      query = `${query} intersect ${filterQuery}`;
    }
    
    // 使用 graph 输出格式一次性获取所有依赖关系
    const graphOutput = await executeBazelQuery(query, 'graph');
    
    // 解析 graph 输出
    const depsTree = parseGraphOutput(graphOutput);
    
    // 确保目标节点存在（即使没有依赖）
    if (!depsTree.has(target)) {
      depsTree.set(target, []);
    }
    
    return depsTree;
  } catch (error) {
    // 如果 graph 查询失败，回退到空树
    const depsTree = new Map<string, string[]>();
    depsTree.set(target, []);
    return depsTree;
  }
}

// 获取 target 的反向依赖关系（支持 rule 类型过滤）
async function getTargetRdeps(target: string, universe: string = "//...", ruleTypes: string[] = DEFAULT_RULE_TYPES): Promise<string[]> {
  try {
    let query = `rdeps(${universe}, ${target})`;
    // 如果指定了 rule 类型过滤，应用过滤
    if (ruleTypes.length > 0) {
      const filterQuery = buildRuleFilterQuery(ruleTypes);
      query = `${query} intersect ${filterQuery}`;
    }
    const result = await executeBazelQuery(query);
    return result
      .split("\n")
      .map((line) => line.trim())
      .filter((line) => line.length > 0 && line !== target);
  } catch (error) {
    console.error(`Error getting rdeps for ${target}:`, error);
    return [];
  }
}

// 获取两个 target 之间的依赖路径
async function getDependencyPath(from: string, to: string): Promise<string[]> {
  try {
    const result = await executeBazelQuery(`somepath(${from}, ${to})`);
    return result
      .split("\n")
      .map((line) => line.trim())
      .filter((line) => line.length > 0);
  } catch (error) {
    console.error(`Error getting path from ${from} to ${to}:`, error);
    return [];
  }
}

// 启动服务器
serve({
  port: PORT,
  async fetch(req) {
    const url = new URL(req.url);

    // CORS 头
    const corsHeaders = {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type",
    };

    // 处理 OPTIONS 请求
    if (req.method === "OPTIONS") {
      return new Response(null, { headers: corsHeaders });
    }

    // 静态文件服务
    if (url.pathname === "/" || url.pathname === "/index.html") {
      const file = Bun.file("./bazel-deps-server/index.html");
      return new Response(file, {
        headers: { ...corsHeaders, "Content-Type": "text/html" },
      });
    }

    if (url.pathname === "/style.css") {
      const file = Bun.file("./bazel-deps-server/style.css");
      return new Response(file, {
        headers: { ...corsHeaders, "Content-Type": "text/css" },
      });
    }

    if (url.pathname === "/app.js") {
      const file = Bun.file("./bazel-deps-server/app.js");
      return new Response(file, {
        headers: { ...corsHeaders, "Content-Type": "application/javascript" },
      });
    }

    // API 端点
    if (url.pathname === "/api/targets") {
      try {
        // 从查询参数获取 rule 类型过滤
        const ruleTypesParam = url.searchParams.get("ruleTypes");
        const ruleTypes = ruleTypesParam 
          ? ruleTypesParam.split(",").map(r => r.trim()).filter(r => r.length > 0)
          : DEFAULT_RULE_TYPES;
        const targets = await getAllSourceTargets(ruleTypes);
        return Response.json(targets, { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    // 获取可用的 rule 类型列表
    if (url.pathname === "/api/rule-types") {
      try {
        // 查询所有规则类型，使用 label_kind 输出格式
        const result = await executeBazelQuery('kind(rule, //...)', 'label_kind');
        const ruleTypes = new Set<string>();
        result.split("\n").forEach(line => {
          const trimmed = line.trim();
          if (trimmed) {
            // label_kind 格式: "java_library rule //path/to:target"
            const parts = trimmed.split(/\s+/);
            if (parts.length >= 2 && parts[1] === 'rule') {
              ruleTypes.add(parts[0]);
            }
          }
        });
        return Response.json(Array.from(ruleTypes).sort(), { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    if (url.pathname === "/api/deps") {
      const target = url.searchParams.get("target");
      if (!target) {
        return Response.json(
          { error: "target parameter is required" },
          { status: 400, headers: corsHeaders }
        );
      }

      try {
        const ruleTypesParam = url.searchParams.get("ruleTypes");
        const ruleTypes = ruleTypesParam 
          ? ruleTypesParam.split(",").map(r => r.trim()).filter(r => r.length > 0)
          : DEFAULT_RULE_TYPES;
        const deps = await getTargetDeps(target, ruleTypes);
        return Response.json(deps, { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    if (url.pathname === "/api/deps-tree") {
      const target = url.searchParams.get("target");
      if (!target) {
        return Response.json(
          { error: "target parameter is required" },
          { status: 400, headers: corsHeaders }
        );
      }

      try {
        const ruleTypesParam = url.searchParams.get("ruleTypes");
        const ruleTypes = ruleTypesParam 
          ? ruleTypesParam.split(",").map(r => r.trim()).filter(r => r.length > 0)
          : DEFAULT_RULE_TYPES;
        const depsTree = await getDepsTree(target, ruleTypes);
        // 将 Map 转换为对象
        const treeObj: Record<string, string[]> = {};
        depsTree.forEach((deps, target) => {
          treeObj[target] = deps;
        });
        return Response.json(treeObj, { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    if (url.pathname === "/api/rdeps") {
      const target = url.searchParams.get("target");
      if (!target) {
        return Response.json(
          { error: "target parameter is required" },
          { status: 400, headers: corsHeaders }
        );
      }

      try {
        const ruleTypesParam = url.searchParams.get("ruleTypes");
        const ruleTypes = ruleTypesParam 
          ? ruleTypesParam.split(",").map(r => r.trim()).filter(r => r.length > 0)
          : DEFAULT_RULE_TYPES;
        const rdeps = await getTargetRdeps(target, "//...", ruleTypes);
        return Response.json(rdeps, { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    if (url.pathname === "/api/path") {
      const from = url.searchParams.get("from");
      const to = url.searchParams.get("to");
      if (!from || !to) {
        return Response.json(
          { error: "from and to parameters are required" },
          { status: 400, headers: corsHeaders }
        );
      }

      try {
        const path = await getDependencyPath(from, to);
        return Response.json(path, { headers: corsHeaders });
      } catch (error) {
        return Response.json(
          { error: error instanceof Error ? error.message : "Unknown error" },
          { status: 500, headers: corsHeaders }
        );
      }
    }

    return new Response("Not Found", { status: 404, headers: corsHeaders });
  },
});

console.log(`🚀 Bazel 依赖分析服务器运行在 http://localhost:${PORT}`);

