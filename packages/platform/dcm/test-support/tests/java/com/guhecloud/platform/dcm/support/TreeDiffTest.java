package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

class TreeDiffTest {

    @Test
    void testDiffWithAddedNode() {
        // 创建两棵树，其中一棵有额外的节点
        SimpleTreeNode from = createTreeWithNodes("root", "child1", "child2");
        SimpleTreeNode to = createTreeWithNodes("root", "child1", "child2", "child3"); // root2有一个额外的节点"child3"

        DiffContext<SimpleTreeNode> context = TreeDiff.diff(
                new SimpleTreeAdaptor(Collections.singletonList(from)),
                new SimpleTreeAdaptor(Collections.singletonList(to)));

        // 验证结果
        Assertions.assertEquals(1, context.getAddedNodes().size(), "应该有一个添加的节点");
        Assertions.assertEquals("child3", context.getAddedNodes().get(0).getId(), "添加的节点应该是'child3'");
    }

    @Test
    void testDiffWithDeletedNode3Level() {
        // 创建两棵树，其中一棵有额外的节点
        Collection<SimpleTreeNode> from = Arrays.asList(
                createTreeWithNodes("root", "child1", "child2"),
                createTreeWithNodes("child1", "l2Child1", "l2Child2"));

        Collection<SimpleTreeNode> to = Arrays.asList(
                createTreeWithNodes("root", "child1", "child2")
                                                     ); // root2有一个额外的节点"child3"

        SimpleTreeAdaptor fromTree = new SimpleTreeAdaptor(from);
        DiffContext<SimpleTreeNode> context = TreeDiff.diff(
                fromTree,
                new SimpleTreeAdaptor(to));

        // 验证结果
        MatcherAssert.assertThat("应该有一个或二个删除的节点", context.getDeletedNodes().size(), Matchers.in(Arrays.asList(1, 2)));
        if (context.getDeletedNodes().size() == 1) {
            Assertions.assertFalse(true, "还没有支持的特性");
        } else {
            MatcherAssert.assertThat("添加的节点应该包含'l2Child1', 'l2Child2'",
                                     context.getDeletedNodes().stream().map(x -> x.getId()).collect(Collectors.toList()),
                                     IsIterableContainingInAnyOrder.containsInAnyOrder("l2Child1", "l2Child2"));

        }
    }

    @Test
    void testDiffWithDeletedNode() {
        // 创建两棵树，其中一棵少了一个节点
        SimpleTreeNode from = createTreeWithNodes("root", "child1", "child2");
        SimpleTreeNode to = createTreeWithNodes("root", "child1"); // root2少了节点"child2"

        DiffContext<SimpleTreeNode> context = TreeDiff.diff(
                new SimpleTreeAdaptor(Collections.singletonList(from)),
                new SimpleTreeAdaptor(Collections.singletonList(to)));

        // 验证结果
        Assertions.assertEquals(1, context.getDeletedNodes().size(), "应该有一个删除的节点");
        Assertions.assertEquals("child2", context.getDeletedNodes().get(0).getId(), "删除的节点应该是'child2'");
    }

    @Test
    void testDiffWithModifiedNode() {
        // 创建两棵树，其中一个节点的内容发生了变化
        SimpleTreeNode from = createTreeWithNodesAndContent("root", "content1", Pair.of("child1", "content1"));
        SimpleTreeNode to = createTreeWithNodesAndContent("root", "content1", Pair.of("child1", "content2")); // "child1"的内容从"content1"变为"content2"

        DiffContext<SimpleTreeNode> context = TreeDiff.diff(
                new SimpleTreeAdaptor(Collections.singletonList(from)),
                new SimpleTreeAdaptor(Collections.singletonList(to)));

        // 验证结果
        Assertions.assertEquals(1, context.getModifiedNodes().size(), "应该有一个修改的节点");
        Assertions.assertEquals("child1", context.getModifiedNodes().get(0).getKey().getId(), "修改的节点应该是'child1'");
        Assertions.assertNotEquals(context.getModifiedNodes().get(0).getKey().getNodeContentFp(), context.getModifiedNodes().get(0)
                .getValue().getNodeContentFp(), "节点内容应该发生变化");
    }

    @Test
    void testDiffWithDeletedNodeButIgnoreDelete() {
        // 创建两棵树，其中一棵少了一个节点
        SimpleTreeNode from = createTreeWithNodes("root", "child1", "child2");
        SimpleTreeNode to = createTreeWithNodes("root", "child1"); // root2少了节点"child2"

        DiffContext<SimpleTreeNode> context = TreeDiff.diff(
                new SimpleTreeAdaptor(Collections.singletonList(from)),
                new SimpleTreeAdaptor(Collections.singletonList(to)), true);

        // 验证结果
        Assertions.assertEquals(0, context.getDeletedNodes().size(), "应该有一个删除的节点");
    }

    // 创建树的辅助方法，需要根据实际的节点实现进行调整
    private SimpleTreeNode createTreeWithNodes(String rootId, String... childIds) {
        SimpleTreeNode simpleTreeNode = new SimpleTreeNode(rootId, "content " + rootId);
        List<SimpleTreeNode> childrenNodes = new ArrayList<>();
        for (String childId : childIds) {
            childrenNodes.add(createTreeWithNodes(childId));
        }
        if (!childrenNodes.isEmpty()) {
            simpleTreeNode.setChildren(childrenNodes);
        }
        return simpleTreeNode;
    }

    private SimpleTreeNode createTreeWithNodesAndContent(String rootId, String rootContent, Pair<String, String>... childrenPairs) {
        // 实现创建树并设置内容的逻辑
        SimpleTreeNode simpleTreeNode = new SimpleTreeNode(rootId, rootContent);
        List<SimpleTreeNode> childrenNodes = new ArrayList<>();
        for (Pair<String, String> child : childrenPairs) {
            childrenNodes.add(createTreeWithNodesAndContent(child.getKey(), child.getValue()));
        }
        if (!childrenNodes.isEmpty()) {
            simpleTreeNode.setChildren(childrenNodes);
        }
        return simpleTreeNode;
    }
}

/**
 * id 在同一颗树下唯一, 父亲包含儿子信息(不含孙节点信息), 仅做演示.
 */
class SimpleTreeNode implements ITreeNode<SimpleTreeNode> {
    private final String code;
    private final String content;

    private List<SimpleTreeNode> children;

    public SimpleTreeNode(String code, String content) {
        this(code, content, new ArrayList<>());
    }

    public SimpleTreeNode(String code, String content, List<SimpleTreeNode> children) {
        this.code = code;
        this.content = content;
        this.children = children;
    }

    @Override
    public String getId() {
        return code;
    }

    @Override
    public String getFingerPrint() {
        return String.format("%s:[%s]==>[%s]", code, content, StringUtils.join(children, ","));
    }

    @Override
    public String getNodeContentFp() {
        return content;
    }

    @Override
    public List<Identifiable> getChildren() {
        return null == children ? Collections.emptyList()
                                : children.stream().collect(Collectors.toList());
    }

    public void setChildren(Collection<SimpleTreeNode> children) {
        if (children != null) {
            this.children = new ArrayList<>(children);
        }
    }

    @Override
    public String toString() {
        String childrenParts = CollectionUtils.isEmpty(children) ? ""
                                                                 : ", children='" + StringUtils.join(children, ",") + "'";
        return String.format("SimpleTreeNode{code='%s', content='%s'%s}", code, content, childrenParts);
    }
}

class SimpleTreeAdaptor implements TreeAdaptor<SimpleTreeNode> {

    private static final String SEPARATOR = "::";

    private Map<String, SimpleTreeNode> nodesByPath = new LinkedHashMap<>();
    private Map<String, String> nodePathById = new HashMap<>();

    public SimpleTreeAdaptor(Collection<SimpleTreeNode> nodes) {
        Set<String> childrenIds = new HashSet<>();
        Map<String, SimpleTreeNode> nodeById = new HashMap<>();
        for (SimpleTreeNode node : nodes) {
            nodeById.put(node.getId(), node);
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                for (Identifiable child : node.getChildren()) {
                    childrenIds.add(child.getId());
                    nodeById.put(child.getId(), (SimpleTreeNode) child);
                }
            }
        }
        List<SimpleTreeNode> rootNodes = new ArrayList<>();
        for (SimpleTreeNode node : nodes) {
            if (!childrenIds.contains(node.getId())) {
                rootNodes.add(node);
            }
        }
        SimpleTreeNode dummyRoot = new SimpleTreeNode("", "", rootNodes);
        Queue<SimpleTreeNode> queue = new LinkedList<>(rootNodes);
        nodesByPath.put("", dummyRoot);
        nodePathById.put(dummyRoot.getId(), "");
        for (SimpleTreeNode rootNode : rootNodes) {
            nodePathById.put(rootNode.getId(), rootNode.getId());
        }
        // 构建nodePath by id.
        while (!queue.isEmpty()) {
            SimpleTreeNode node = queue.poll();
            String nodePath = nodePathById.get(node.getId());
            for (Identifiable child : node.getChildren()) {
                nodePathById.put(child.getId(), nodePath + SEPARATOR + child.getId());
                nodesByPath.put(nodePath + SEPARATOR + child.getId(), nodeById.get(child.getId())); // 仅仅是为了把叶子节点放进来, 中间节点会被再次覆盖.
                queue.add(nodeById.get(child.getId()));
            }
        }
        // 所有父节点, 还是以包含子节点的节点替换
        for (SimpleTreeNode node : nodes) {
            nodesByPath.put(nodePathById.get(node.getId()), node);
        }
    }

    @Override
    public SimpleTreeNode getNode(List<String> nodePath) {
        return nodesByPath.get(StringUtils.join(nodePath, SEPARATOR));
    }

    @Override
    public Collection<SimpleTreeNode> getChildren(SimpleTreeNode parent) {
        String nodePath = nodePathById.get(parent.getId());
        return nodePath == null ? Collections.emptyList()
                                : nodesByPath.get(nodePath).getChildren().stream()
                       /* 其他的实现， 可能需要另外构建一个SimpleTreeNode */
                       .map(x -> (SimpleTreeNode) x).collect(Collectors.toList());
    }
}
