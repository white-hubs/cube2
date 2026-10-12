package com.guhecloud.cube.base.dictionary.dcm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.matcher.RequestedConfigItemMatcher;
import com.guhecloud.platform.dcm.param.DcmRequirementParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.dcm.support.DiffContext;
import com.guhecloud.platform.dcm.support.TreeDiff;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.dictionary.dcm.utils.DictionaryDcmSupport.compare;
import static com.guhecloud.platform.test.MockSupport.mockResultsFromResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryDcmSupportTest {
    private static final TypeReference<List<DictEntryCollectionParam>> LIST_DICT_ENTRY_COLLECTION = new TypeReference<List<DictEntryCollectionParam>>() {
    };

    @Test
    void convertDiffContext2RequestedConfigItem() throws IOException {
        List<DictEntryCollectionParam> from = mockResultsFromResource("example/treediff/common_from_dict_entry.yaml", LIST_DICT_ENTRY_COLLECTION);
        List<DictEntryCollectionParam> to = mockResultsFromResource("example/treediff/common_to_dict_entry.yaml", LIST_DICT_ENTRY_COLLECTION);

        DictEntryCollectionTreeAdaptor fromTree = new DictEntryCollectionTreeAdaptor(from);
        DictEntryCollectionTreeAdaptor toTree = new DictEntryCollectionTreeAdaptor(to);

        DiffContext<DictEntryCollectionTreeAdaptor.DictEntryCollectionWrapper> x = TreeDiff.diff(fromTree, toTree);
        assertThat("should has one add", x.getAddedNodes(), hasSize(1));
        assertThat("should has one remove", x.getDeletedNodes(), hasSize(1));
        assertThat("should has one modification", x.getModifiedNodes(), hasSize(1));

        Collection<RequestedConfigItem> ret = DictionaryDcmSupport.convertCascadeDiffContext2RequestedConfigItem(x, Collections.singletonList("123"));
        assertNotNull(ret);
        assertThat("should include 3 items", ret.size(), equalTo(3));

    }

    private TypeReference<List<DcmRequirementParam>> REF_PARAM_TYPE = new TypeReference<List<DcmRequirementParam>>() {
    };
    private TypeReference<List<RequestedConfigItemParam>> REF_RESULT_TYPE = new TypeReference<List<RequestedConfigItemParam>>() {
    };

    @Test
    void testCompare1() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                Pair.of("case_1_", false)
                                                         );
        testCompareInner("basic compare", cases);
    }

    @Test
    void testComparePartialNoRemoval() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                //Pair.of("case_2_", true)
                Pair.of("case_2_", false)
                                                         );
        testCompareInner("partial_diff_no_removal", cases);
    }

    @Test
    void testComparePartialRemoval() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                Pair.of("case_4_", true)
                                                         );
        testCompareInner("partial_diff_no_removal", cases);
    }

    @Test
    void testCompareFull() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                // Pair.of("case_1", true),
                Pair.of("case_3_", true)
                                                         );
        testCompareInner("partial_diff_no_removal", cases);
    }

    @Test
    void testCompareCascadeTreeCompare() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                Pair.of("case_5_", false)
                                                         );
        testCompareInner("cascade_tree diff", cases);
    }

    @Test
    void testCompareCascadePartialCompare() throws IOException {
        String basePath = "example/diff/case_5_";
        testCompareWithResourcePaths("Cascade compare with partial request",
                                     basePath + "requested_partial.yaml",
                                     basePath + "existed.yaml",
                                     basePath + "tenant_diff_partial.yaml",
                                     false);
    }

    @Test
    void testRemoveUnexisted() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                Pair.of("remove_unexisted/", true)
                                                         );
        testCompareInner("RemoveUnexisted", cases);
    }

    @Test
    void testNonPartialEmpty() throws IOException {
        List<Pair<String, Boolean>> cases = Arrays.asList(
                Pair.of("case_6_", true),
                Pair.of("case_6_", false)
                                                         );
        testCompareInner("non-partial, empty request", cases);
    }

    /**
     * @param title information may exists in assert failure message.
     * @param cases Pair of &lt;case prefix string, is case for system config&gt;.
     */
    private void testCompareInner(String title, List<Pair<String, Boolean>> cases) throws IOException {
        String caseBaseDir = "example/diff/";
        for (Pair<String, Boolean> case_ : cases) {
            String casePrefix = case_.getKey();
            boolean isSystem = case_.getValue();
            String requestPath = String.format("%s%s%s", caseBaseDir, casePrefix, "requested.yaml");
            String existedPath = String.format("%s%s%s", caseBaseDir, casePrefix, "existed.yaml");
            String expectedPath = String.format("%s%s%s%s", caseBaseDir, casePrefix, (isSystem ? "system" : "tenant"), "_diff.yaml");
            testCompareWithResourcePaths(title, requestPath, existedPath, expectedPath, isSystem);
        }
    }

    private void testCompareWithResourcePaths(String title, String requestPath, String existedPath, String expectedPath,
                                              boolean isSystem
                                             ) throws IOException {
        List<DcmRequirementParam> requested = mockResultsFromResource(requestPath, REF_PARAM_TYPE);
        List<RequestedConfigItem> existed = mockResultsFromResource(existedPath, REF_RESULT_TYPE).stream()
                .map(RequestedConfigItemParam::toEntity).collect(Collectors.toList());
        List<RequestedConfigItem> expectedResult = mockResultsFromResource(expectedPath, REF_RESULT_TYPE).stream()
                .map(RequestedConfigItemParam::toEntity).collect(Collectors.toList());
        List<RequestedConfigItem> diffResult =
                compare(requested.stream().map(DcmRequirementParam::toEntity).collect(Collectors.toList()),
                        existed.stream().collect(Collectors.groupingBy(x -> x.getConfigType().toUpperCase())),
                        isSystem, isSystem ? null : "1");
        assertThat("[" + title +
                   "] failed: diff got mismatch with expected", diffResult, RequestedConfigItemMatcher.isRequestConfigItemList(expectedResult));
    }

}
