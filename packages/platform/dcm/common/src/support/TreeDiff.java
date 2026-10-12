package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.dcm.Identifiable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 一个基础的树形结构比较的能力实现。配合DiffContext, TreeAdaptor实现树形结构的比较。
 *
 * @param <T> 实现了ITreeNode接口的类型
 *
 * @see TreeAdaptor
 * @see ITreeNode
 */
public class TreeDiff<T extends ITreeNode<T>> {
    private final TreeAdaptor<T> fromTree;
    private final TreeAdaptor<T> toTree;
    private final boolean addOrUpdateOnly;

    private TreeDiff(TreeAdaptor<T> fromTree, TreeAdaptor<T> toTree) {
        this(fromTree, toTree, false);
    }

    private TreeDiff(TreeAdaptor<T> fromTree, TreeAdaptor<T> toTree, boolean addOrUpdateOnly) {
        this.fromTree = fromTree;
        this.toTree = toTree;
        this.addOrUpdateOnly = addOrUpdateOnly;
    }

    public static <E extends ITreeNode<E>> DiffContext<E> diff(TreeAdaptor<E> from, TreeAdaptor<E> to) {
        return diff(from, to, false);
    }

    public static <E extends ITreeNode<E>> DiffContext<E> diff(TreeAdaptor<E> from, TreeAdaptor<E> to, boolean addOrUpdateOnly) {
        DiffContext<E> context = new DiffContext<>();
        TreeDiff<E> treeDiff = new TreeDiff<>(from, to, addOrUpdateOnly);
        treeDiff.diffNode(from.getRoot(), to.getRoot(), context, new LinkedList<>());
        return context;
    }

    private void diffNode(T fromNode, T toNode, DiffContext<T> context, LinkedList<String> parentPath) {
        if (fromNode == null && toNode == null) {
            return;
        }

        if (fromNode == null) {
            context.addAddedNode(toNode);
            return;
        }

        if (toNode == null && !addOrUpdateOnly) {
            context.addDeletedNode(fromNode);
            return;
        }

        if (!fromNode.getNodeContentFp().equals(toNode.getNodeContentFp())) {
            context.addModifiedNode(fromNode, toNode);
        }

        Collection<? extends Identifiable> children1 = fromTree.getChildren(fromNode);
        Collection<? extends Identifiable> children2 = toTree.getChildren(toNode);
        diffNodes(children1, children2, parentPath, context);
    }

    /**
     * 批量比较
     */
    private void diffNodes(Collection<? extends Identifiable> fromNodes, Collection<? extends Identifiable> toNodes,
                           LinkedList<String> parentNodePath, DiffContext<T> context
                          ) {
        Set<String> fromIds = fromNodes.stream().map(Identifiable::getId).collect(Collectors.toSet());
        Set<String> toIds = toNodes.stream().map(Identifiable::getId).collect(Collectors.toSet());
        Collection<String> toBeAddIds = CollectionUtils.removeAll(toIds, fromIds);
        Collection<String> toBeDeleteIds = CollectionUtils.removeAll(fromIds, toIds);
        Collection<String> commonIds = CollectionUtils.intersection(fromIds, toIds);

        for (String newId : toBeAddIds) {
            parentNodePath.add(newId);
            try {
                context.addAddedNode((T) toTree.getNode(parentNodePath));
            } finally {
                parentNodePath.removeLast();
            }
        }
        if (!addOrUpdateOnly) {
            for (String deletedId : toBeDeleteIds) {
                parentNodePath.add(deletedId);
                try {
                    context.addDeletedNode((T) fromTree.getNode(parentNodePath));
                } finally {
                    parentNodePath.removeLast();
                }
            }
        }
        for (String commonId : commonIds) {
            parentNodePath.add(commonId);
            try {
                diffNode(fromTree.getNode(parentNodePath),
                         toTree.getNode(parentNodePath), context, parentNodePath);
            } finally {
                parentNodePath.removeLast();
            }
        }
    }
}

