package com.guhecloud.cube.base.dictionary.dcm.utils;

import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.DictEntryParam;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.support.ITreeNode;
import com.guhecloud.platform.dcm.support.TreeAdaptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * 适配级联数据字典到TreeAdaptor接口，以使用TreeDiff支持比较。
 *
 * @see TreeAdaptor
 * @see com.guhecloud.platform.dcm.support.DiffContext
 * @see ITreeNode
 */
@Slf4j
public class DictEntryCollectionTreeAdaptor implements TreeAdaptor<DictEntryCollectionTreeAdaptor.DictEntryCollectionWrapper> {
    private static final String CODE_PATH_SEPARATOR = "::";
    private static final DictEntryParam DUMMY_ROOT_ENTRY = new DictEntryParam();

    private static final String ROOT_CODE_PATH = "";
    private Map<String, DictEntryParam> dictEntryByCodePath = new HashMap<>();
    private Map<String, DictEntryCollectionWrapper> dictEntryCollectionWrapperByCodePath = new LinkedHashMap<>();

    private DictEntryCollectionParam root;

    /**
     * 构建Adaptor, 需要传入的是同类的DictEntryCollectionParam
     */
    public DictEntryCollectionTreeAdaptor(List<DictEntryCollectionParam> dictEntryCollectionParams) {
        List<DictEntryCollectionParam> rootNodes = dictEntryCollectionParams.stream()
                .filter(x -> !StringUtils.isNotEmpty(x.getParent()))
                .collect(Collectors.toList());
        if (rootNodes.isEmpty()) {
            log.error("Build DictEntryCollectionTreeAdaptor with collection w/o root nodes");
            throw new ResourcePreconditionFailedException("dictEntryCollectionParams", "should include root nodes");
        }
        if (rootNodes.size() > 1) {
            log.error("Multiple root nodes in dictEntryCollectionParams");
            throw new ResourcePreconditionFailedException("dictEntryCollectionParams", "should not include multiple root nodes");
        }
        for (DictEntryCollectionParam entryCollectionParam : dictEntryCollectionParams) {
            dictEntryCollectionWrapperByCodePath.put(entryCollectionParam.getParent(),
                                                     new DictEntryCollectionWrapper(entryCollectionParam));
        }
        dictEntryByCodePath.put(ROOT_CODE_PATH, DUMMY_ROOT_ENTRY);
        root = rootNodes.get(0);

        Queue<DictEntryCollectionParam> inProcess = new LinkedList<>();
        inProcess.addAll(rootNodes);
        while (!inProcess.isEmpty()) {
            DictEntryCollectionParam node = inProcess.poll();
            String codePath = node.getParent();
            for (DictEntryParam child : node.getDictEntries()) {
                String childCodePath = buildCodePath(codePath, child.getKey());
                dictEntryByCodePath.put(childCodePath, child);
                DictEntryCollectionWrapper wrapper = dictEntryCollectionWrapperByCodePath.get(childCodePath);
                if (wrapper != null) {
                    inProcess.add(dictEntryCollectionWrapperByCodePath.get(childCodePath).wrapped);
                }
            }
        }
        // 把叶子节点也放进来。
        for (Map.Entry<String, DictEntryParam> entry : dictEntryByCodePath.entrySet()) {
            if (!dictEntryCollectionWrapperByCodePath.containsKey(entry.getKey())) {
                dictEntryCollectionWrapperByCodePath.put(entry.getKey(),
                                                         buildLeafDictEntryCollectionWrapper(entry.getKey()));
            }
        }
    }

    public DictEntryCollectionWrapper getWrapper(DictEntryCollectionParam wrapped) {
        return new DictEntryCollectionWrapper(wrapped);
    }

    @SneakyThrows
    private DictEntryCollectionWrapper buildLeafDictEntryCollectionWrapper(String codePath) {
        DictEntryCollectionParam node = (DictEntryCollectionParam) root.clone();
        node.setParent(codePath);
        node.setDictEntries(Collections.emptyList());
        return new DictEntryCollectionWrapper(node);
    }

    private String buildCodePath(String parentCodePath, String code) {
        if (ROOT_CODE_PATH.equals(parentCodePath)) {
            return code;
        } else {
            return parentCodePath + CODE_PATH_SEPARATOR + code;
        }
    }

    @Override
    public DictEntryCollectionWrapper getNode(List<String> nodePath) {
        if (!nodePath.isEmpty() && ROOT_CODE_PATH.equals(nodePath.get(0))) {
            nodePath = nodePath.subList(1, nodePath.size());
        }
        return dictEntryCollectionWrapperByCodePath.get(
                nodePath.stream().collect(Collectors.joining(CODE_PATH_SEPARATOR)));
    }

    @Override
    public Collection<Identifiable> getChildren(DictEntryCollectionWrapper parent) {
        // 绕了一圈，从前面的SimpleTreeNodeAdaptor 的经验看应该是有必要的？
        return dictEntryCollectionWrapperByCodePath.get(parent.getCodePath()).getChildren();
    }

    public Collection<DictEntryCollectionParam> getAllDictEntryCollectionParams() {
        return dictEntryCollectionWrapperByCodePath.values().stream()
                .filter(x -> x.wrapped.getDictEntries() != null && !x.wrapped.getDictEntries().isEmpty())
                .map(x -> x.wrapped)
                .collect(Collectors.toList());
    }

    public class DictEntryCollectionWrapper implements ITreeNode<DictEntryCollectionWrapper> {
        private final DictEntryCollectionParam wrapped;

        private DictEntryCollectionWrapper(DictEntryCollectionParam dictEntryCollectionParam) {
            wrapped = dictEntryCollectionParam;
        }

        public String getCodePath() {
            return wrapped.getParent();
        }

        public String getCode() {
            int pos = wrapped.getParent().lastIndexOf(CODE_PATH_SEPARATOR);
            return wrapped.getParent().substring(pos == -1 ? 0 : pos + CODE_PATH_SEPARATOR.length());
        }

        public DictEntryParam getNode() {
            return dictEntryByCodePath.get(wrapped.getParent());
        }

        public DictEntryCollectionParam getWrapped() {
            return wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getId();
        }

        @Override
        public String getFingerPrint() {
            return wrapped.getFingerPrint();
        }

        @Override
        public String getNodeContentFp() {
            Objects.requireNonNull(dictEntryByCodePath.get(wrapped.getParent()));
            return dictEntryByCodePath.get(wrapped.getParent()).getFingerPrint();
        }

        @Override
        public List<Identifiable> getChildren() {
            return wrapped.getDictEntries().stream().collect(Collectors.toList());
        }
    }
}
