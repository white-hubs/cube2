# Bazel 依赖分析工具

一个基于 Bun 的 Web 服务，用于可视化分析 Bazel 项目的依赖关系。

## 功能特性

- 📋 **Target 列表**: 列出所有源码级 target，支持实时搜索
- 🔍 **依赖关系查看**: 点击任意 target 查看其依赖项和反向依赖项
- 🗺️ **图形视图模式**: 可视化展示依赖关系图，支持交互式浏览
- 🎨 **现代化 UI**: 美观的用户界面，响应式设计
- ⚡ **高性能**: 基于 Bun 运行时，快速响应
- 🎯 **Rule 类型过滤**: 支持按 rule 类型过滤 targets，默认只显示 `java_library` 和 `java_binary`
- 🔧 **可配置过滤**: 可以自定义选择要显示的 rule 类型，减少依赖项的复杂度
- 🎛️ **视图切换**: 支持在列表视图和图形视图之间切换

## 使用方法

### 启动服务器

```bash
# 开发模式（支持热重载）
bun run deps-server:dev

# 生产模式
bun run deps-server
```

服务器将在 `http://localhost:3000` 启动。

### API 端点

- `GET /api/targets?ruleTypes=<type1,type2,...>` - 获取所有源码级 targets（支持 rule 类型过滤）
- `GET /api/rule-types` - 获取项目中所有可用的 rule 类型
- `GET /api/deps?target=<target>&ruleTypes=<type1,type2,...>` - 获取指定 target 的依赖项（支持过滤）
- `GET /api/rdeps?target=<target>&ruleTypes=<type1,type2,...>` - 获取指定 target 的反向依赖项（支持过滤）
- `GET /api/path?from=<from>&to=<to>` - 获取两个 target 之间的依赖路径

## 使用说明

1. 打开浏览器访问 `http://localhost:3000`
2. 在页面顶部的 "Rule 类型过滤" 区域选择要显示的 rule 类型（默认已选中 `java_library` 和 `java_binary`）
3. 在左侧列表中找到或搜索你感兴趣的 target
4. 点击 target 查看其依赖关系（依赖项也会根据选择的 rule 类型进行过滤）
5. 在依赖列表中点击任意依赖项可以跳转到该 target 的详情页

### 视图模式

#### 列表视图（默认）
- 以列表形式展示依赖项和反向依赖项
- 适合查看详细的依赖信息
- 点击依赖项可以快速跳转

#### 图形视图
- 点击右上角的 "🗺️ 图形视图" 按钮切换到图形模式
- 可视化展示依赖关系图：
  - **中心节点**（紫色高亮）：当前选中的 target
  - **左侧节点**（蓝色）：依赖项，箭头指向中心节点
  - **右侧节点**（绿色）：反向依赖项，箭头从中心节点指向它们
- **交互功能**：
  - 点击节点：选中该 target 并查看其依赖关系
  - 双击节点：切换到列表视图并选中该 target
  - 拖拽：移动图形位置
  - 滚轮：缩放图形
- **控制按钮**：
  - 🎯 **居中**：将当前选中的 target 居中显示
  - 🔍 **适应窗口**：自动调整图形大小以适应窗口
  - 🔄 **重置**：重置图形布局和物理引擎

### Rule 类型过滤

- 默认情况下，只显示 `java_library` 和 `java_binary` 类型的 targets
- 可以通过顶部的复选框选择/取消选择不同的 rule 类型
- 过滤会影响：
  - Target 列表显示
  - 依赖项列表（只显示匹配的依赖项）
  - 反向依赖项列表（只显示匹配的反向依赖项）
  - 图形视图中的节点显示

## 技术栈

- **后端**: Bun + TypeScript
- **前端**: 原生 HTML/CSS/JavaScript
- **图形可视化**: vis-network (用于依赖关系图可视化)
- **构建工具**: Bazel Query API

## 注意事项

- 确保已安装 Bazel 并在项目根目录运行
- 首次加载 targets 列表可能需要一些时间，取决于项目规模
- 查询大型项目的依赖关系可能需要几秒钟时间

