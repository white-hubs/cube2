package com.guhecloud.platform.base.util;

import com.guhecloud.platform.base.model.Tree;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TreeUtils {
    private TreeUtils() {}

    /**
     * 树节点组装，自动关联树节点并返回根节点列表
     *
     * @param trees 树对象实例列表
     * @param <T>   树对象类型
     *
     * @return 树根节点列表
     */
    public static <T extends Tree<?, T>> List<T> assembleTrees(Collection<T> trees) {
        if (CollectionUtils.isEmpty(trees)) {
            return Collections.emptyList();
        }

        // 构建树主键/实例映射表，并初始化树的子节点集合
        Map<?, T> mapping = trees.stream().peek(tree -> tree.setChildren(new LinkedList<>()))
                .collect(Collectors.toMap(Tree::getId, t -> t, (o, n) -> n));

        // 查找并关联树节点，返回所有没有父节点的树
        return trees.stream().filter(tree -> {
            T parent = ObjectUtils.ifNull(tree.getParentId(), mapping::get);
            if (parent != null) {
                parent.getChildren().add(tree);
            }
            return Objects.isNull(parent);
        }).collect(Collectors.toList());
    }

    /**
     * 遍历树节点
     *
     * @param trees    树对象列表
     * @param consumer 树节点处理回调方法
     * @param <T>      树对象类型
     */
    public static <T extends Tree<?, T>> void iterateTrees(Collection<T> trees, Consumer<T> consumer) {
        if (!CollectionUtils.isEmpty(trees)) {
            trees.forEach(tree -> {
                consumer.accept(tree);
                iterateTrees(ObjectUtils.ifNull(tree, Tree::getChildren), consumer);
            });
        }
    }
}
