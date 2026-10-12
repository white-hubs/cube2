// 全局状态
let allTargets = [];
let currentTarget = null;
let filteredTargets = [];
let availableRuleTypes = [];
let selectedRuleTypes = ['java_library']; // 默认选中的 rule 类型
let currentView = 'list'; // 'list' 或 'graph'
let currentGraphType = 'deps'; // 'deps' 或 'rdeps'
let depsNetwork = null; // 直接依赖图形实例
let rdepsNetwork = null; // 反向依赖图形实例
let depsGraphData = { nodes: [], edges: [] }; // 直接依赖图形数据
let rdepsGraphData = { nodes: [], edges: [] }; // 反向依赖图形数据

// DOM 元素
const searchInput = document.getElementById('searchInput');
const targetList = document.getElementById('targetList');
const targetDetails = document.getElementById('targetDetails');
const targetCount = document.getElementById('targetCount');
const ruleFilter = document.getElementById('ruleFilter');
const listViewBtn = document.getElementById('listViewBtn');
const graphViewBtn = document.getElementById('graphViewBtn');
const graphView = document.getElementById('graphView');
const depsGraphContainer = document.getElementById('depsGraphContainer');
const rdepsGraphContainer = document.getElementById('rdepsGraphContainer');
const graphInfo = document.getElementById('graphInfo');
const depsGraphTab = document.getElementById('depsGraphTab');
const rdepsGraphTab = document.getElementById('rdepsGraphTab');
const centerGraphBtn = document.getElementById('centerGraphBtn');
const fitGraphBtn = document.getElementById('fitGraphBtn');
const resetGraphBtn = document.getElementById('resetGraphBtn');
const fullscreenBtn = document.getElementById('fullscreenBtn');

// 初始化
async function init() {
    await loadAvailableRuleTypes();
    renderRuleFilter();
    await loadTargets();
    initGraphView();
    setupEventListeners();
    // 初始化全屏按钮文本
    updateFullscreenButton();
}

// 创建图形网络实例
function createNetwork(container, graphData, onNodeClick, onNodeDoubleClick) {
    const options = {
        nodes: {
            shape: 'box',
            font: {
                size: 14,
                face: 'monospace'
            },
            borderWidth: 2,
            shadow: true,
            color: {
                border: '#667eea',
                background: '#ffffff',
                highlight: {
                    border: '#764ba2',
                    background: '#f0f0ff'
                }
            }
        },
        edges: {
            arrows: {
                to: {
                    enabled: true,
                    scaleFactor: 0.8
                }
            },
            color: {
                color: '#667eea',
                highlight: '#764ba2'
            },
            smooth: {
                type: 'continuous',
                roundness: 0.5
            },
            width: 2
        },
        physics: {
            enabled: true,
            stabilization: {
                iterations: 200
            },
            barnesHut: {
                gravitationalConstant: -2000,
                centralGravity: 0.3,
                springLength: 200,
                springConstant: 0.04,
                damping: 0.09
            }
        },
        interaction: {
            hover: true,
            tooltipDelay: 200,
            zoomView: true,
            dragView: true
        },
        layout: {
            improvedLayout: true,
            hierarchical: {
                enabled: false
            }
        }
    };

    const network = new vis.Network(container, graphData, options);

    // 节点点击事件
    network.on('click', (params) => {
        if (params.nodes.length > 0 && onNodeClick) {
            onNodeClick(params, graphData);
        }
    });

    // 双击节点事件
    network.on('doubleClick', (params) => {
        if (params.nodes.length > 0 && onNodeDoubleClick) {
            onNodeDoubleClick(params, graphData);
        }
    });

    return network;
}

// 初始化图形视图
function initGraphView() {
    // 等待 vis-network 库加载
    if (typeof vis === 'undefined' || typeof vis.Network === 'undefined') {
        console.warn('vis-network 库未加载，将在库加载后重试');
        setTimeout(() => {
            if (typeof vis !== 'undefined' && typeof vis.Network !== 'undefined') {
                initGraphView();
            }
        }, 100);
        return;
    }

    // 创建直接依赖图形
    depsNetwork = createNetwork(
        depsGraphContainer,
        depsGraphData,
        (params, graphData) => {
            const nodeId = params.nodes[0];
            const node = graphData.nodes.find(n => n.id === nodeId);
            if (node && node.target) {
                selectTarget(node.target);
            }
        },
        (params, graphData) => {
            const nodeId = params.nodes[0];
            const node = graphData.nodes.find(n => n.id === nodeId);
            if (node && node.target) {
                switchView('list');
                selectTarget(node.target);
            }
        }
    );

    // 创建反向依赖图形
    rdepsNetwork = createNetwork(
        rdepsGraphContainer,
        rdepsGraphData,
        (params, graphData) => {
            const nodeId = params.nodes[0];
            const node = graphData.nodes.find(n => n.id === nodeId);
            if (node && node.target) {
                selectTarget(node.target);
            }
        },
        (params, graphData) => {
            const nodeId = params.nodes[0];
            const node = graphData.nodes.find(n => n.id === nodeId);
            if (node && node.target) {
                switchView('list');
                selectTarget(node.target);
            }
        }
    );
}

// 加载可用的 rule 类型
async function loadAvailableRuleTypes() {
    try {
        const response = await fetch('/api/rule-types');
        if (response.ok) {
            availableRuleTypes = await response.json();
            // 确保默认选中的类型在可用列表中
            selectedRuleTypes = selectedRuleTypes.filter(type => 
                availableRuleTypes.includes(type)
            );
            // 如果没有默认类型可用，选择前几个
            if (selectedRuleTypes.length === 0 && availableRuleTypes.length > 0) {
                selectedRuleTypes = availableRuleTypes.slice(0, 5);
            }
        }
    } catch (error) {
        console.error('加载 rule 类型失败:', error);
        // 使用默认值
        availableRuleTypes = ['java_library', 'java_binary', 'java_test', 'cc_library', 'cc_binary'];
    }
}

// 渲染 rule 类型过滤器
function renderRuleFilter() {
    if (availableRuleTypes.length === 0) {
        ruleFilter.innerHTML = '<div class="loading">加载中...</div>';
        return;
    }

    ruleFilter.innerHTML = availableRuleTypes.map(ruleType => {
        const isChecked = selectedRuleTypes.includes(ruleType);
        return `
            <label class="rule-checkbox">
                <input 
                    type="checkbox" 
                    value="${escapeHtml(ruleType)}"
                    ${isChecked ? 'checked' : ''}
                />
                <span>${escapeHtml(ruleType)}</span>
            </label>
        `;
    }).join('');

    // 添加事件监听器
    ruleFilter.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            updateSelectedRuleTypes();
        });
    });
}

// 更新选中的 rule 类型
function updateSelectedRuleTypes() {
    selectedRuleTypes = Array.from(ruleFilter.querySelectorAll('input[type="checkbox"]:checked'))
        .map(cb => cb.value);
    
    // 重新加载 targets
    loadTargets();
    
    // 如果当前有选中的 target，重新加载其详情
    if (currentTarget) {
        loadTargetDetails(currentTarget);
    }
}

// 加载所有 targets
async function loadTargets() {
    try {
        targetList.innerHTML = '<div class="loading">正在加载 targets...</div>';
        // 构建查询参数
        const ruleTypesParam = selectedRuleTypes.length > 0 
            ? selectedRuleTypes.join(',')
            : '';
        const url = ruleTypesParam 
            ? `/api/targets?ruleTypes=${encodeURIComponent(ruleTypesParam)}`
            : '/api/targets';
        
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        allTargets = await response.json();
        filteredTargets = allTargets;
        renderTargetList();
        updateTargetCount();
    } catch (error) {
        console.error('加载 targets 失败:', error);
        targetList.innerHTML = `<div class="error">加载失败: ${error.message}</div>`;
    }
}

// 渲染 target 列表
function renderTargetList() {
    if (filteredTargets.length === 0) {
        targetList.innerHTML = '<div class="loading">没有找到匹配的 targets</div>';
        return;
    }

    targetList.innerHTML = filteredTargets.map(target => 
        `<div class="target-item" data-target="${escapeHtml(target)}">${escapeHtml(target)}</div>`
    ).join('');

    // 添加点击事件
    targetList.querySelectorAll('.target-item').forEach(item => {
        item.addEventListener('click', () => {
            const target = item.dataset.target;
            selectTarget(target);
        });
    });
}

// 选择 target
async function selectTarget(target) {
    // 更新 UI 状态
    targetList.querySelectorAll('.target-item').forEach(item => {
        item.classList.toggle('active', item.dataset.target === target);
    });

    currentTarget = target;
    
    if (currentView === 'list') {
        await loadTargetDetails(target);
    } else {
        await loadGraphView(target);
    }
}

// 加载 target 详情
async function loadTargetDetails(target) {
    targetDetails.innerHTML = `
        <div class="target-header">
            <h2>Target: <span class="target-label">${escapeHtml(target)}</span></h2>
        </div>
        <div class="loading">正在加载依赖关系...</div>
    `;

    try {
        // 构建查询参数
        const ruleTypesParam = selectedRuleTypes.length > 0 
            ? selectedRuleTypes.join(',')
            : '';
        const queryParam = ruleTypesParam 
            ? `&ruleTypes=${encodeURIComponent(ruleTypesParam)}`
            : '';
        
        // 并行加载依赖和反向依赖
        const [deps, rdeps] = await Promise.all([
            fetch(`/api/deps?target=${encodeURIComponent(target)}${queryParam}`).then(r => r.json()),
            fetch(`/api/rdeps?target=${encodeURIComponent(target)}${queryParam}`).then(r => r.json())
        ]);

        renderTargetDetails(target, deps, rdeps);
    } catch (error) {
        console.error('加载依赖关系失败:', error);
        targetDetails.innerHTML = `
            <div class="target-header">
                <h2>Target: <span class="target-label">${escapeHtml(target)}</span></h2>
            </div>
            <div class="error">加载失败: ${error.message}</div>
        `;
    }
}

// 加载图形视图
async function loadGraphView(target) {
    graphInfo.textContent = `正在加载 ${target} 的依赖关系图...`;
    depsGraphData = { nodes: [], edges: [] };
    rdepsGraphData = { nodes: [], edges: [] };

    try {
        // 构建查询参数
        const ruleTypesParam = selectedRuleTypes.length > 0 
            ? selectedRuleTypes.join(',')
            : '';
        const queryParam = ruleTypesParam 
            ? `&ruleTypes=${encodeURIComponent(ruleTypesParam)}`
            : '';
        
        // 加载依赖树和反向依赖
        const depsTreeUrl = `/api/deps-tree?target=${encodeURIComponent(target)}${queryParam}`;
        
        // 创建带超时的 fetch（60秒超时）
        const fetchWithTimeout = (url, timeout = 60000) => {
            return Promise.race([
                fetch(url),
                new Promise((_, reject) => 
                    setTimeout(() => reject(new Error('Request timeout')), timeout)
                )
            ]);
        };
        
        const [depsTree, rdeps] = await Promise.all([
            fetchWithTimeout(depsTreeUrl).then(r => {
                if (!r.ok) {
                    throw new Error(`HTTP ${r.status}: ${r.statusText}`);
                }
                return r.json();
            }),
            fetchWithTimeout(`/api/rdeps?target=${encodeURIComponent(target)}${queryParam}`).then(r => r.json())
        ]);

        renderGraph(target, depsTree, rdeps);
    } catch (error) {
        console.error('加载图形视图失败:', error);
        graphInfo.textContent = `加载失败: ${error.message}`;
    }
}

// 构建依赖树层级
function buildDepsTreeLevels(depsTree, rootTarget) {
    const levels = new Map(); // target -> level
    const visited = new Set();
    
    // BFS 遍历，计算每个节点的层级
    function calculateLevels(target, level) {
        if (visited.has(target) || !depsTree[target]) {
            return;
        }
        visited.add(target);
        levels.set(target, level);
        
        const children = depsTree[target] || [];
        children.forEach(child => {
            if (!levels.has(child) || levels.get(child) > level + 1) {
                calculateLevels(child, level + 1);
            }
        });
    }
    
    calculateLevels(rootTarget, 0);
    return levels;
}

// 渲染直接依赖图形（树形结构）
function renderDepsGraph(target, depsTree) {
    const nodes = [];
    const edges = [];
    const nodeMap = new Map();

    // 构建层级关系
    const levels = buildDepsTreeLevels(depsTree, target);
    
    // 添加中心节点（当前 target）
    const centerNodeId = target;
    const centerNode = {
        id: centerNodeId,
        label: target.split(':').pop() || target.split('/').pop(),
        title: target,
        target: target,
        level: 0,
        color: {
            border: '#764ba2',
            background: '#e6f0ff',
            highlight: {
                border: '#764ba2',
                background: '#d0e0ff'
            }
        },
        font: {
            bold: true,
            size: 16
        },
        size: 30
    };
    nodes.push(centerNode);
    nodeMap.set(centerNodeId, centerNode);

    // 添加所有依赖节点并构建边
    function addNodeAndEdges(currentTarget, parentLevel) {
        const children = depsTree[currentTarget] || [];
        
        children.forEach(child => {
            if (!nodeMap.has(child)) {
                const childLevel = levels.get(child) || parentLevel + 1;
                const childNode = {
                    id: child,
                    label: child.split(':').pop() || child.split('/').pop(),
                    title: child,
                    target: child,
                    level: childLevel,
                    color: {
                        border: '#667eea',
                        background: '#ffffff',
                        highlight: {
                            border: '#667eea',
                            background: '#f0f0ff'
                        }
                    },
                    size: 20
                };
                nodes.push(childNode);
                nodeMap.set(child, childNode);
            }
            
            // 添加边（从子节点指向父节点，因为依赖关系是子依赖父）
            edges.push({
                from: child,
                to: currentTarget,
                color: { color: '#667eea' },
                title: `${child} → ${currentTarget}`
            });
            
            // 递归处理子节点的依赖
            if (depsTree[child]) {
                addNodeAndEdges(child, levels.get(child) || parentLevel + 1);
            }
        });
    }
    
    addNodeAndEdges(target, 0);

    depsGraphData = {
        nodes: nodes,
        edges: edges
    };

    if (depsNetwork) {
        depsNetwork.setData(depsGraphData);
        
        // 使用层次布局，显示树形结构
        depsNetwork.setOptions({
            layout: {
                hierarchical: {
                    enabled: true,
                    direction: 'LR', // 从左到右
                    sortMethod: 'directed',
                    levelSeparation: 250,
                    nodeSpacing: 150,
                    treeSpacing: 200,
                    blockShifting: true,
                    edgeMinimization: true,
                    parentCentralization: true
                }
            }
        });
        
        // 适应窗口
        setTimeout(() => {
            depsNetwork.fit({
                animation: {
                    duration: 1000,
                    easingFunction: 'easeInOutQuad'
                }
            });
        }, 100);
    }
}

// 渲染反向依赖图形
function renderRdepsGraph(target, rdeps) {
    const nodes = [];
    const edges = [];

    // 添加中心节点（当前 target）
    const centerNodeId = target;
    nodes.push({
        id: centerNodeId,
        label: target.split(':').pop() || target.split('/').pop(),
        title: target,
        target: target,
        level: 0,
        color: {
            border: '#764ba2',
            background: '#e6f0ff',
            highlight: {
                border: '#764ba2',
                background: '#d0e0ff'
            }
        },
        font: {
            bold: true,
            size: 16
        },
        size: 30
    });

    // 添加反向依赖节点（右侧，level 1）
    rdeps.forEach((rdep) => {
        const nodeId = rdep;
        nodes.push({
            id: nodeId,
            label: rdep.split(':').pop() || rdep.split('/').pop(),
            title: rdep,
            target: rdep,
            level: 1,
            color: {
                border: '#48bb78',
                background: '#f0fff4',
                highlight: {
                    border: '#48bb78',
                    background: '#e6ffed'
                }
            },
            size: 20
        });
        // 添加从中心到反向依赖的边
        edges.push({
            from: centerNodeId,
            to: nodeId,
            color: { color: '#48bb78' },
            title: `${target} → ${rdep}`
        });
    });

    rdepsGraphData = {
        nodes: nodes,
        edges: edges
    };

    if (rdepsNetwork) {
        rdepsNetwork.setData(rdepsGraphData);
        
        // 使用层次布局
        rdepsNetwork.setOptions({
            layout: {
                hierarchical: {
                    enabled: true,
                    direction: 'LR', // 从左到右
                    sortMethod: 'directed',
                    levelSeparation: 300,
                    nodeSpacing: 200,
                    treeSpacing: 300
                }
            }
        });
        
        // 适应窗口
        setTimeout(() => {
            rdepsNetwork.fit({
                animation: {
                    duration: 1000,
                    easingFunction: 'easeInOutQuad'
                }
            });
        }, 100);
    }
}

// 渲染图形视图
function renderGraph(target, deps, rdeps) {
    renderDepsGraph(target, deps);
    renderRdepsGraph(target, rdeps);
    
    // 默认显示直接依赖图形
    if (!currentGraphType) {
        currentGraphType = 'deps';
    }
    
    // 切换到当前选中的图形类型
    switchGraphType(currentGraphType);
}

// 更新图形信息
function updateGraphInfo(target, depsCount, rdepsCount) {
    if (currentGraphType === 'deps') {
        const nodeCount = depsGraphData.nodes.length - 1; // 减去中心节点
        graphInfo.textContent = `显示 ${target} 的依赖树 (${nodeCount} 个节点)`;
    } else {
        graphInfo.textContent = `显示 ${target} 的反向依赖 (${rdepsCount} 个)`;
    }
}

// 切换图形类型（直接依赖/反向依赖）
function switchGraphType(type) {
    currentGraphType = type;
    
    if (type === 'deps') {
        depsGraphTab.classList.add('active');
        rdepsGraphTab.classList.remove('active');
        depsGraphContainer.classList.remove('hidden');
        rdepsGraphContainer.classList.add('hidden');
        updateGraphInfo(currentTarget, depsGraphData.nodes.length - 1, rdepsGraphData.nodes.length - 1);
    } else {
        depsGraphTab.classList.remove('active');
        rdepsGraphTab.classList.add('active');
        depsGraphContainer.classList.add('hidden');
        rdepsGraphContainer.classList.remove('hidden');
        updateGraphInfo(currentTarget, depsGraphData.nodes.length - 1, rdepsGraphData.nodes.length - 1);
    }
}

// 切换视图
function switchView(view) {
    if (view === currentView) return;

    currentView = view;

    if (view === 'list') {
        listViewBtn.classList.add('active');
        graphViewBtn.classList.remove('active');
        targetDetails.classList.remove('hidden');
        graphView.classList.add('hidden');
        
        // 如果当前有选中的 target，重新加载列表视图
        if (currentTarget) {
            loadTargetDetails(currentTarget);
        }
    } else {
        listViewBtn.classList.remove('active');
        graphViewBtn.classList.add('active');
        targetDetails.classList.add('hidden');
        graphView.classList.remove('hidden');
        
        // 如果当前有选中的 target，加载图形视图
        if (currentTarget) {
            loadGraphView(currentTarget);
        } else {
            graphInfo.textContent = '从左侧选择一个 target 查看依赖关系图';
            depsGraphData = { nodes: [], edges: [] };
            rdepsGraphData = { nodes: [], edges: [] };
            if (depsNetwork) {
                depsNetwork.setData(depsGraphData);
            }
            if (rdepsNetwork) {
                rdepsNetwork.setData(rdepsGraphData);
            }
            // 默认显示直接依赖图形
            switchGraphType('deps');
        }
    }
}

// 渲染 target 详情
function renderTargetDetails(target, deps, rdeps) {
    const statsHtml = `
        <div class="stats">
            <div class="stat-card">
                <h4>直接依赖</h4>
                <div class="value">${deps.length}</div>
            </div>
            <div class="stat-card">
                <h4>反向依赖</h4>
                <div class="value">${rdeps.length}</div>
            </div>
        </div>
    `;

    const depsHtml = `
        <div class="deps-section">
            <h3>📦 依赖项 (${deps.length})</h3>
            <div class="deps-list">
                ${deps.length > 0 
                    ? deps.map(dep => `<div class="dep-item" data-target="${escapeHtml(dep)}">${escapeHtml(dep)}</div>`).join('')
                    : '<div class="dep-item empty">无依赖项</div>'
                }
            </div>
        </div>
    `;

    const rdepsHtml = `
        <div class="deps-section">
            <h3>🔗 反向依赖项 (${rdeps.length})</h3>
            <div class="deps-list">
                ${rdeps.length > 0 
                    ? rdeps.map(rdep => `<div class="dep-item" data-target="${escapeHtml(rdep)}">${escapeHtml(rdep)}</div>`).join('')
                    : '<div class="dep-item empty">无反向依赖项</div>'
                }
            </div>
        </div>
    `;

    targetDetails.innerHTML = `
        <div class="target-header">
            <h2>Target: <span class="target-label">${escapeHtml(target)}</span></h2>
        </div>
        ${statsHtml}
        ${depsHtml}
        ${rdepsHtml}
    `;

    // 为依赖项添加点击事件
    targetDetails.querySelectorAll('.dep-item[data-target]').forEach(item => {
        item.addEventListener('click', () => {
            const depTarget = item.dataset.target;
            selectTarget(depTarget);
            // 滚动到顶部
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    });
}

// 搜索功能
function filterTargets(searchTerm) {
    const term = searchTerm.toLowerCase().trim();
    if (term === '') {
        filteredTargets = allTargets;
    } else {
        filteredTargets = allTargets.filter(target => 
            target.toLowerCase().includes(term)
        );
    }
    renderTargetList();
    updateTargetCount();
}

// 更新 target 计数
function updateTargetCount() {
    if (filteredTargets.length === allTargets.length) {
        targetCount.textContent = `${allTargets.length}`;
    } else {
        targetCount.textContent = `${filteredTargets.length} / ${allTargets.length}`;
    }
}

// 设置事件监听器
function setupEventListeners() {
    // 搜索输入
    searchInput.addEventListener('input', (e) => {
        filterTargets(e.target.value);
    });

    // 回车键快速选择第一个结果
    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && filteredTargets.length > 0) {
            selectTarget(filteredTargets[0]);
            searchInput.blur();
        }
    });

    // 视图切换按钮
    listViewBtn.addEventListener('click', () => switchView('list'));
    graphViewBtn.addEventListener('click', () => switchView('graph'));

    // 图形类型切换
    depsGraphTab.addEventListener('click', () => switchGraphType('deps'));
    rdepsGraphTab.addEventListener('click', () => switchGraphType('rdeps'));

    // 图形控制按钮
    centerGraphBtn.addEventListener('click', () => {
        const network = currentGraphType === 'deps' ? depsNetwork : rdepsNetwork;
        const graphData = currentGraphType === 'deps' ? depsGraphData : rdepsGraphData;
        
        if (network && currentTarget) {
            const centerNode = graphData.nodes.find(n => n.target === currentTarget);
            if (centerNode) {
                network.focus(centerNode.id, {
                    scale: 1.2,
                    animation: {
                        duration: 1000,
                        easingFunction: 'easeInOutQuad'
                    }
                });
            }
        }
    });

    fitGraphBtn.addEventListener('click', () => {
        const network = currentGraphType === 'deps' ? depsNetwork : rdepsNetwork;
        if (network) {
            network.fit({
                animation: {
                    duration: 1000,
                    easingFunction: 'easeInOutQuad'
                }
            });
        }
    });

    resetGraphBtn.addEventListener('click', () => {
        const network = currentGraphType === 'deps' ? depsNetwork : rdepsNetwork;
        if (network) {
            network.setOptions({
                physics: {
                    enabled: true
                }
            });
            setTimeout(() => {
                network.fit({
                    animation: {
                        duration: 1000,
                        easingFunction: 'easeInOutQuad'
                    }
                });
            }, 100);
        }
    });

    // 全屏功能
    fullscreenBtn.addEventListener('click', () => {
        toggleFullscreen();
    });

    // 监听全屏状态变化
    document.addEventListener('fullscreenchange', handleFullscreenChange);
    document.addEventListener('webkitfullscreenchange', handleFullscreenChange);
    document.addEventListener('mozfullscreenchange', handleFullscreenChange);
    document.addEventListener('MSFullscreenChange', handleFullscreenChange);
}

// 处理全屏状态变化
function handleFullscreenChange() {
    updateFullscreenButton();
    const isFullscreen = !!(document.fullscreenElement || 
                           document.webkitFullscreenElement || 
                           document.mozFullScreenElement || 
                           document.msFullscreenElement);
    
    if (isFullscreen) {
        // 全屏时自动适应窗口
        setTimeout(() => {
            const network = currentGraphType === 'deps' ? depsNetwork : rdepsNetwork;
            if (network) {
                network.fit({
                    animation: {
                        duration: 1000,
                        easingFunction: 'easeInOutQuad'
                    }
                });
            }
        }, 300);
    }
}

// 切换全屏
function toggleFullscreen() {
    const elem = graphView;
    
    if (!document.fullscreenElement && 
        !document.webkitFullscreenElement && 
        !document.mozFullScreenElement && 
        !document.msFullscreenElement) {
        // 进入全屏
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    } else {
        // 退出全屏
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }
}

// 更新全屏按钮文本
function updateFullscreenButton() {
    const isFullscreen = !!(document.fullscreenElement || 
                           document.webkitFullscreenElement || 
                           document.mozFullScreenElement || 
                           document.msFullscreenElement);
    fullscreenBtn.textContent = isFullscreen ? '⛶ 退出全屏' : '⛶ 全屏';
}

// HTML 转义
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 启动应用
init();

