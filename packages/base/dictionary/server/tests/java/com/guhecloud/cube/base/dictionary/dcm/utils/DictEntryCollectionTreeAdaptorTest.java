package com.guhecloud.cube.base.dictionary.dcm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.utils.DictEntryCollectionTreeAdaptor.DictEntryCollectionWrapper;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.support.DiffContext;
import com.guhecloud.platform.dcm.support.ITreeNode;
import com.guhecloud.platform.dcm.support.TreeDiff;
import com.guhecloud.platform.test.MockSupport;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DictEntryCollectionTreeAdaptorTest {

    private static final TypeReference<List<DictEntryCollectionParam>> LIST_DICT_ENTRY_COLLECTION = new TypeReference<List<DictEntryCollectionParam>>() {
    };

    @Test
    void testDiff() throws IOException {
        List<DictEntryCollectionParam> from = MockSupport.mockResultsFromResource("example/treediff/common_from_dict_entry.yaml", LIST_DICT_ENTRY_COLLECTION);
        List<DictEntryCollectionParam> to = MockSupport.mockResultsFromResource("example/treediff/common_to_dict_entry.yaml", LIST_DICT_ENTRY_COLLECTION);

        DictEntryCollectionTreeAdaptor fromTree = new DictEntryCollectionTreeAdaptor(from);
        DictEntryCollectionTreeAdaptor toTree = new DictEntryCollectionTreeAdaptor(to);
        ITreeNode<DictEntryCollectionWrapper> fromRoot = fromTree.getRoot();
        ITreeNode<DictEntryCollectionWrapper> toRoot = toTree.getRoot();

        // 根节点的规范约束
        assertEquals(fromRoot.getId(), toRoot.getId());
        assertEquals(fromRoot.getNodeContentFp(), toRoot.getNodeContentFp());
        assertEquals(fromRoot.getChildren().stream().map(x -> x.getId()).collect(Collectors.toList()),
                     toRoot.getChildren().stream().map(x -> x.getId()).collect(Collectors.toList()));

        DiffContext<DictEntryCollectionWrapper> x = TreeDiff.diff(fromTree, toTree);
        assertThat("should has one add", x.getAddedNodes(), hasSize(1));
        assertThat("add code path unexpected:", x.getAddedNodes().get(0).getCodePath(), equalTo("root3::child1"));
        assertThat("should has one remove", x.getDeletedNodes(), hasSize(1));
        assertThat("delete code path unexpected:", x.getDeletedNodes().get(0).getCodePath(), equalTo("root2::child1"));
        assertThat("should has one modification", x.getModifiedNodes(), hasSize(1));
        Pair<DictEntryCollectionWrapper, DictEntryCollectionWrapper> o = x.getModifiedNodes().get(0);
        assertThat("modified code path unexpected:", o.getKey().getCodePath(), equalTo("root1::child1"));
        assertThat("code unexpected of modified node", o.getKey().getCode(), equalTo("child1"));
        assertEquals(o.getKey().getId(), o.getValue().getId(), "content should not match");
        assertNotEquals(o.getKey().getNodeContentFp(), o.getValue().getNodeContentFp(), "content should not match");
    }

    @Test
    void testParse() throws IOException {
        List<DictEntryCollectionParam> from = MockSupport.mockResultsFromResource("example/treediff/common_from_dict_entry.yaml", LIST_DICT_ENTRY_COLLECTION);
        DictEntryCollectionTreeAdaptor fromTree = new DictEntryCollectionTreeAdaptor(from);
        ITreeNode<DictEntryCollectionWrapper> root = fromTree.getRoot();
        // assertThat(root.getId(), equalTo(""));
        // assertThat(root.getNodeContentFp(), anyOf(nullValue(), equalTo("")));
        assertThat(root.getChildren(), hasSize(3));
        // assertThat(root.getChildren().stream().findFirst().get().getId(), equalTo("root1"));
        List<Identifiable> children = (List<Identifiable>) fromTree.getChildren(Arrays.asList("", "root1"));
        assertThat("should has 1 element", children, hasSize(1));
        assertThat("entry mismatch", ((DictEntryCollectionParam.DictEntryParam) children.get(0)).getKey(), equalTo("child1"));
        // assertThat("should have 1 element", children.get(0).getId(), equalTo("child1"));

    }
}
