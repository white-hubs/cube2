package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.base.model.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiffContext<T extends ITreeNode<T>> {
    private final List<T> addedNodes = new ArrayList<>();
    private final List<T> deletedNodes = new ArrayList<>();
    private final List<Pair<T, T>> modifiedNodes = new ArrayList<>();

    public DiffContext() {
    }

    public void addAddedNode(T node) {
        addedNodes.add(node);
    }

    public void addDeletedNode(T node) {
        deletedNodes.add(node);
    }

    public void addModifiedNode(T originalNode, T modifiedNode) {
        Pair<T, T> modifiedPair = Pair.of(originalNode, modifiedNode);
        modifiedNodes.add(modifiedPair);
    }

    public List<T> getAddedNodes() {
        return addedNodes;
    }

    public List<T> getDeletedNodes() {
        return deletedNodes;
    }

    public List<Pair<T, T>> getModifiedNodes() {
        return modifiedNodes;
    }

    public boolean hasChanges() {
        return !addedNodes.isEmpty() || !deletedNodes.isEmpty() || !modifiedNodes.isEmpty();
    }
}
