package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;

import java.util.List;

public interface ITreeNode<T extends ITreeNode> extends Identifiable {

    /**
     * 获取节点自身的内容的FingerPrint. 如果finger print相同， 可以视同节点自身携带内容相同。
     *
     * @return 字符串表示的内容finger print.
     */
    String getNodeContentFp();

    /**
     * 获取节点的子节点,子节点不一定需要返回树结构同构的节点, 但getId 返回内容需要可以有效构成节点路径中的部分.
     *
     * @return 子节点集合。叶子节点返回Empty Collection.
     */
    List<Identifiable> getChildren();
}
