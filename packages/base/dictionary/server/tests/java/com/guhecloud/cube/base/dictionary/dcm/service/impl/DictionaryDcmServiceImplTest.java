package com.guhecloud.cube.base.dictionary.dcm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.service.DictionaryDcmService;
import com.guhecloud.cube.base.dictionary.server.dao.DataDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.cube.base.dictionary.server.service.CascadeDictionaryService;
import com.guhecloud.cube.base.dictionary.server.service.DataDictionaryServer;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryItemService;
import com.guhecloud.cube.base.dictionary.server.service.SysDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;
import com.guhecloud.cube.base.dictionary.server.vo.SysDictionaryVO;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.test.MockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictionaryDcmServiceImplTest extends MockSupport {
    private static final TypeReference SYS_DICT_VO_LIST_TYPE_REF = new TypeReference<List<SysDictionaryVO>>() {};
    private static final TypeReference DICTIONARY_ITEM_VO_LIST_TYPE_REF = new TypeReference<List<DictionaryItemVO>>() {};
    private static final TypeReference DATA_DICT_VO_LIST_TYPE_REF = new TypeReference<List<DataDictionaryVO>>() {};

    private static final TypeReference CASCADE_DICTIONARY_LIST_TYPE_REF = new TypeReference<List<CascadeDictionary>>() {};
    @Mock
    private SysDictionaryService sysDictionaryService;
    @Mock
    private DictionaryItemService dictionaryItemService;
    @Mock
    private DataDictionaryServer dataDictionaryServer;
    @Mock
    private CascadeDictionaryService cascadeDictionaryService;

    private DictionaryDcmService dictionaryDcmService;

    @Mock
    private DataDictionaryMapper dataDictionaryMapper;

    @BeforeEach
    public void setup() throws IOException {
        dictionaryDcmService = new DictionaryDcmServiceImpl(sysDictionaryService, dataDictionaryServer,
                                                            dictionaryItemService, cascadeDictionaryService, dataDictionaryMapper);
        if (System.currentTimeMillis() < 0) {
            // Never Execute
            when(sysDictionaryService.getAll()).thenReturn((List<SysDictionaryVO>) mockResultsFromResource(
                    "mockdb/sysdict.yaml", SYS_DICT_VO_LIST_TYPE_REF));
            when(dictionaryItemService.getDictionaryItemAll()).thenReturn((List<DictionaryItemVO>) mockResultsFromResource(
                    "mockdb/dictionary_item.yaml", DICTIONARY_ITEM_VO_LIST_TYPE_REF));
            when(dataDictionaryServer.getByTenant(null)).thenReturn((List<DataDictionaryVO>) mockResultsFromResource(
                    "mockdb/datadict.yaml", DATA_DICT_VO_LIST_TYPE_REF));
            when(cascadeDictionaryService.getByTenant(null)).thenReturn((List<CascadeDictionary>) mockResultsFromResource(
                    "mockdb/cascadedict.yaml", CASCADE_DICTIONARY_LIST_TYPE_REF));
            when(dataDictionaryServer.getByTenant(1L)).thenReturn((List<DataDictionaryVO>) mockResultsFromResource(
                    "mockdb/datadict_tenant_1.yaml", DATA_DICT_VO_LIST_TYPE_REF));
            when(cascadeDictionaryService.getByTenant(1L)).thenReturn((List<CascadeDictionary>) mockResultsFromResource(
                    "mockdb/cascadedict_tenant_1.yaml", CASCADE_DICTIONARY_LIST_TYPE_REF));
        }
    }

    @Test
    void ensureDictionaryTypeExists() {
    }

    @Test
    void ensureDictionaryItemExists() {
    }

    @Test
    void fetchRuntimeDictionaryItems() throws IOException {
        when(sysDictionaryService.getAll()).thenReturn((List<SysDictionaryVO>) mockResultsFromResource(
                "mockdb/sysdict.yaml", SYS_DICT_VO_LIST_TYPE_REF));
        //        when(dictionaryItemService.getDictionaryItemAll()).thenReturn((List<DictionaryItemVO>) mockResultsFromResource(
        //                "mockdb/dictionary_item.yaml", DICTIONARY_ITEM_VO_LIST_TYPE_REF));
        when(dataDictionaryServer.getByTenant(1L)).thenReturn((List<DataDictionaryVO>) mockResultsFromResource(
                "mockdb/datadict.yaml", DATA_DICT_VO_LIST_TYPE_REF));
        when(cascadeDictionaryService.getByTenant(1L)).thenReturn((List<CascadeDictionary>) mockResultsFromResource(
                "mockdb/cascadedict.yaml", CASCADE_DICTIONARY_LIST_TYPE_REF));

        List<DictEntryCollectionParam> dictEntryCollectionParams = dictionaryDcmService.fetchRuntimeDictEntryCollections(null);
        assertNotNull(dictEntryCollectionParams);
        assertEquals(2, dictEntryCollectionParams.size());

        List<DictEntryCollectionParam> tenantEntryCollectionParams = dictionaryDcmService.fetchRuntimeDictEntryCollections("1");
        assertNotNull(tenantEntryCollectionParams);
        assertEquals(4, tenantEntryCollectionParams.size()); // one data dict entry, 3 cascade dict entry

        for (DictEntryCollectionParam dictEntryCollectionParam : tenantEntryCollectionParams) {
            assertThat(dictEntryCollectionParam.getDictEntries(), not(empty()));
        }
    }

    @Test
    void testFetchCascadeDictEntries() throws IOException {
        //        when(sysDictionaryService.getAll()).thenReturn((List<SysDictionaryVO>) mockResultsFromResource(
        //                "mockdb/sysdict.yaml", SYS_DICT_VO_LIST_TYPE_REF));
        when(dictionaryItemService.getDictionaryItemAll()).thenReturn((List<DictionaryItemVO>) mockResultsFromResource(
                "mockdb/dictionary_item.yaml", DICTIONARY_ITEM_VO_LIST_TYPE_REF));
        when(dataDictionaryServer.getByTenant(1L)).thenReturn(Collections.emptyList());
        when(cascadeDictionaryService.getByTenant(1L)).thenReturn((List<CascadeDictionary>) mockResultsFromResource(
                "mockdb/cascadedict.yaml", CASCADE_DICTIONARY_LIST_TYPE_REF));

        List<DictEntryCollectionParam> tenantEntryCollectionParams = dictionaryDcmService.fetchRuntimeDictEntryCollections("1");
        assertNotNull(tenantEntryCollectionParams);
        assertEquals(3, tenantEntryCollectionParams.size()); // one data dict entry, 3 cascade dict entry
    }

    @Test
    void testFetchCascadeDictEntriesCase2() throws IOException {
        //        when(sysDictionaryService.getAll()).thenReturn((List<SysDictionaryVO>) mockResultsFromResource(
        //                "mockdb/sysdict.yaml", SYS_DICT_VO_LIST_TYPE_REF));
        when(dictionaryItemService.getDictionaryItemAll()).thenReturn((List<DictionaryItemVO>) mockResultsFromResource(
                "mockdb/dictionary_item.yaml", DICTIONARY_ITEM_VO_LIST_TYPE_REF));
        when(dataDictionaryServer.getByTenant(1L)).thenReturn(Collections.emptyList());
        when(cascadeDictionaryService.getByTenant(1L)).thenReturn((List<CascadeDictionary>) mockResultsFromResource(
                "mockdb/cascade_dict_2_tenant_1.yaml", CASCADE_DICTIONARY_LIST_TYPE_REF));

        List<DictEntryCollectionParam> tenantEntryCollectionParams = dictionaryDcmService.fetchRuntimeDictEntryCollections("1");
        assertNotNull(tenantEntryCollectionParams);
        assertEquals(3, tenantEntryCollectionParams.size()); // one data dict entry, 3 cascade dict entry
        // 三级嵌套
        assertEquals(1, tenantEntryCollectionParams.get(1).getParent().split("::", -1).length);
        assertEquals(2, tenantEntryCollectionParams.get(2).getParent().split("::", -1).length);
    }

    @Test
    void fetchRuntimeDictTypes() throws IOException {
        when(dictionaryItemService.getDictionaryItemAll()).thenReturn((List<DictionaryItemVO>) mockResultsFromResource(
                "mockdb/dictionary_item.yaml", DICTIONARY_ITEM_VO_LIST_TYPE_REF));
        List<DictTypeCollectionParam.DictTypeParam> dictTypeParams = dictionaryDcmService.fetchRuntimeDictTypes(null);
        assertNotNull(dictTypeParams);
        assertEquals(2, dictTypeParams.size());
        List<DictTypeCollectionParam.DictTypeParam> tenantTypeParams = dictionaryDcmService.fetchRuntimeDictTypes("1");
        assertNotNull(tenantTypeParams);
        assertEquals(2, tenantTypeParams.size());
    }

    @Test
    void testDumpOfRequestConfigItemParam() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestedConfigItemParam requestedConfigItemParam = new RequestedConfigItemParam();
        requestedConfigItemParam.setConfigType("ABC");
        String content = objectMapper.writeValueAsString(requestedConfigItemParam);
        String expected = "{\"configType\":\"ABC\"}";
        String expected2 = "[{\"configType\":\"ABC\"}]";
        assertEquals(expected, content);
        RequestedConfigItem requestedConfigItem = new RequestedConfigItem();
        requestedConfigItem.setConfigType("ABC");
        String content2 = objectMapper.writeValueAsString(requestedConfigItem);
        assertEquals(expected, content2);
        assertEquals(expected2, objectMapper.writeValueAsString(Collections.singletonList(requestedConfigItem)));
    }
}
