package com.guhecloud.cube.base.dictionary.adapter.impl;

import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.utils.DictionaryRedisUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/25 16:50
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictionaryAdapterImpl implements SysDictionaryAdapter {
    private final SysDictionaryClient sysDictionaryClient;
    private final DictionaryRedisUtils dictionaryRedisUtils;

    @Override
    public SysDictionaryDTO getSysDictionaryByCodeAndItemCode(String itemCode, String code) {
        SysDictionaryDTO sysDictionaryDTO = dictionaryRedisUtils.get(itemCode, code, "0", SysDictionaryDTO.class);
        if (Objects.isNull(sysDictionaryDTO)) {
            log.debug("can not get sysDictionary from redis, try to get from remote service");
            sysDictionaryDTO = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(itemCode, code);
        }
        return sysDictionaryDTO;
    }

    @Override
    public List<SysDictionaryDTO> getSysDictionaryByItem(SysDictionaryaramParam param) {
        List<SysDictionaryDTO> sysDictionaryDTOList = dictionaryRedisUtils
                .getListByCode(param.getItemCode(), SysDictionaryDTO.class, param);
        if (CollectionUtils.isEmpty(sysDictionaryDTOList)) {
            log.debug("can not get sysDictionary from redis, try to get from remote service");
            sysDictionaryDTOList = sysDictionaryClient.getSysDictionaryByItem(param);
        }
        return sysDictionaryDTOList;
    }

    @Override
    public Pagination<SysDictionaryDTO> getSysDictionaryByItemPage(SysDictionaryPageParam param) {
        List<SysDictionaryDTO> sysDictionaryDTOList = dictionaryRedisUtils
                .getListByCode(param.getItemCode(), SysDictionaryDTO.class, param);
        if (CollectionUtils.isEmpty(sysDictionaryDTOList)) {
            log.debug("can not get sysDictionary from redis, try to get from remote service");
            return sysDictionaryClient.getSysDictionaryByItemPage(param);
        } else {
            long pageL = Long.parseLong(String.valueOf(param.getPageNumber()));
            long pageSizeL = Long.parseLong(String.valueOf(param.getPageSize()));
            return Pagination.<SysDictionaryDTO>builder()
                    .total((long) sysDictionaryDTOList.size())
                    .pages(((sysDictionaryDTOList.size() / pageSizeL) + 1))
                    .records(sysDictionaryDTOList.stream().skip(pageSizeL * pageL).limit(pageSizeL).collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public SysDictionaryDTO addSysDictionary(SysDictionaryAddParam param) {
        return sysDictionaryClient.addSysDictionary(param);
    }

    @Override
    public SysDictionaryDTO updateSysDictionary(SysDictionaryParam param) {
        return sysDictionaryClient.updateSysDictionary(param);
    }

    @Override
    public Boolean deleteSysDictionary(Long id) {
        return sysDictionaryClient.deleteSysDictionary(id);
    }
}
