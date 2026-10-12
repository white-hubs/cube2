package com.guhecloud.cube.base.dictionary.fallBack;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataDictionaryClientFallBack implements FallbackFactory<DataDictionaryClient> {

    @Override
    public DataDictionaryClient create(Throwable cause) {
        return new DataDictionaryClient() {
            @Override
            public DataDictionaryDTO getDataDictionaryById(Long id) {
                return new DataDictionaryDTO();
            }

            @Override
            public List<DataDictionaryDTO> getDataDictionaryByIds(List<Long> ids) {
                return new ArrayList<>();
            }

            @Override
            public List<DataDictionaryDTO> getDataDictionaryByIdsStr(String ids) {
                return new ArrayList<>();
            }

            @Override
            public List<DataDictionaryDTO> getDataDictionaryByItem(DataDictionaryaramParam param) {
                return new ArrayList<>();
            }

            @Override
            public Pagination<DataDictionaryDTO> getDataDictionaryByItemPage(DataDictionaryPageParam param) {
                return new Pagination<>();
            }

            @Override
            public DataDictionaryDTO getDataDictionaryByItemAndCode(DataDictionaryaramByItemAndCodeParam param) {
                return new DataDictionaryDTO();
            }

            @Override
            public DataDictionaryDTO addDataDictionary(DataDictionaryAddParam param) {
                return new DataDictionaryDTO();
            }

            @Override
            public DataDictionaryDTO updateDataDictionary(DataDictionaryParam param) {
                return new DataDictionaryDTO();
            }

            @Override
            public Boolean deleteDataDictionary(Long id) {
                return false;
            }
        };
    }
}