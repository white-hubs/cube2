package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 提供树形数据访问的接口。
 *
 * @param <T>
 */
public interface TreeAdaptor<T extends ITreeNode> {

    /**
     * 获取根节点
     */
    default T getRoot() {
        return getNode(Collections.emptyList());
    }

    /**
     * 依据节点的路径, 获取节点
     */
    T getNode(List<String> nodePath);

    /**
     * 获取指定节点的子节点
     */
    Collection<? extends Identifiable> getChildren(T parent);

    default Collection<? extends Identifiable> getChildren(List<String> nodePath) {
        T parent = getNode(nodePath);
        if (parent == null) {
            return Collections.emptyList();
        } else {
            return getChildren(parent);
        }
    }
}
