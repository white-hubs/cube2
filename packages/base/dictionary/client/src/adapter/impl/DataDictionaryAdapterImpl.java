package com.guhecloud.cube.base.dictionary.adapter.impl;

import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
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
 * @Date: 2024/10/25 14:05
 * @Description:
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DataDictionaryAdapterImpl implements DataDictionaryAdapter {
    private final DataDictionaryClient dataDictionaryClient;
    private final DictionaryRedisUtils dictionaryRedisUtils;

    @Override
    public List<DataDictionaryDTO> getDataDictionaryByItem(DataDictionaryaramParam param) {

        List<DataDictionaryDTO> dataDictionaryVOList = dictionaryRedisUtils
                .getListByCode(param.getItemCode(), DataDictionaryDTO.class, param);
        if (CollectionUtils.isEmpty(dataDictionaryVOList)) {
            log.debug("can not get dataDictionary from redis, try to get from remote service");
            return dataDictionaryClient.getDataDictionaryByItem(param);
        }
        return dataDictionaryVOList;

    }

    @Override
    public Pagination<DataDictionaryDTO> getDataDictionaryByItemPage(DataDictionaryPageParam param) {
        List<DataDictionaryDTO> dataDictionaryVOList = dictionaryRedisUtils
                .getListByCode(param.getItemCode(), DataDictionaryDTO.class, param);
        if (CollectionUtils.isEmpty(dataDictionaryVOList)) {
            log.debug("can not get dataDictionary from redis, try to get from remote service");
            return dataDictionaryClient.getDataDictionaryByItemPage(param);
        } else {
            long pageL = Long.parseLong(String.valueOf(param.getPageNumber()));
            long pageSizeL = Long.parseLong(String.valueOf(param.getPageSize()));
            return Pagination.<DataDictionaryDTO>builder()
                    .total((long) dataDictionaryVOList.size())
                    .pages(((dataDictionaryVOList.size() / pageSizeL) + 1))
                    .records(dataDictionaryVOList.stream().skip(pageSizeL * pageL).limit(pageSizeL).collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public DataDictionaryDTO getDataDictionaryByItemAndCode(DataDictionaryaramByItemAndCodeParam param) {
        DataDictionaryDTO dataDictionaryVO = dictionaryRedisUtils.get(param.getItemCode(), param.getCode(), DataDictionaryDTO.class);
        if (Objects.isNull(dataDictionaryVO)) {
            log.debug("can not get dataDictionary from redis, try to get from remote service");
            return dataDictionaryClient.getDataDictionaryByItemAndCode(param);
        }
        return dataDictionaryVO;
    }

    @Override
    public DataDictionaryDTO addDataDictionary(DataDictionaryAddParam param) {
        return dataDictionaryClient.addDataDictionary(param);
    }

    @Override
    public DataDictionaryDTO updateDataDictionary(DataDictionaryParam param) {
        return dataDictionaryClient.updateDataDictionary(param);
    }

    @Override
    public Boolean deleteDataDictionary(Long id) {
        return dataDictionaryClient.deleteDataDictionary(id);
    }

}
