package com.guhecloud.cube.base.dictionary.fallBack;

import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryaramParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SysDictionaryClientFallBack implements FallbackFactory<SysDictionaryClient> {

    @Override
    public SysDictionaryClient create(Throwable cause) {
        return new SysDictionaryClient() {
            @Override
            public SysDictionaryDTO getSysDictionaryById(Long id) {
                return new SysDictionaryDTO();
            }

            @Override
            public List<SysDictionaryDTO> getSysDictionaryByIds(List<Long> ids) {
                return Arrays.asList();
            }

            @Override
            public List<SysDictionaryDTO> getSysDictionaryByIdsStr(String ids) {
                return new ArrayList<>();
            }

            @Override
            public SysDictionaryDTO getSysDictionaryByCode(String code) {
                return new SysDictionaryDTO();
            }

            @Override
            public SysDictionaryDTO getSysDictionaryByCodeAndItemCode(String itemCode, String code) {
                return null;
            }

            @Override
            public List<SysDictionaryDTO> getSysDictionaryByItem(SysDictionaryaramParam param) {
                return new ArrayList<>();
            }

            @Override
            public Pagination<SysDictionaryDTO> getSysDictionaryByItemPage(SysDictionaryPageParam param) {
                return Pagination.empty();
            }

            @Override
            public SysDictionaryDTO addSysDictionary(SysDictionaryAddParam param) {
                return new SysDictionaryDTO();
            }

            @Override
            public SysDictionaryDTO updateSysDictionary(SysDictionaryParam param) {
                return new SysDictionaryDTO();
            }

            @Override
            public Boolean deleteSysDictionary(Long id) {
                return false;
            }
        };
    }
}
