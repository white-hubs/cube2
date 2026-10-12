package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridCodeNameStringStringVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridDictionaryUtilServiceImpl {
    private final DataDictionaryClient dataDictionaryClient;
    private static final String DAILY_TYPE_CODE = "GRID_MANAGE_TASK";

    public Set<String> getDictionaryBusinessTypes(String code) {
        Set<String> businessTypes = new HashSet<>();
        List<GridCodeNameStringStringVO> pairs = this.getDictionaryPairs(code);
        if (pairs != null) {
            pairs.forEach(pair -> businessTypes.add(pair.getCode()));
        }
        return businessTypes;
    }

    public List<GridCodeNameStringStringVO> getDictionaryPairs(String code) {
        var pairs = new ArrayList<GridCodeNameStringStringVO>();
        var dictionaryaramParam = new DataDictionaryaramByItemAndCodeParam();
        dictionaryaramParam.setItemCode(DAILY_TYPE_CODE);
        dictionaryaramParam.setCode(code);
        DataDictionaryDTO dictionary;
        try {
            dictionary = dataDictionaryClient.getDataDictionaryByItemAndCode(dictionaryaramParam);
        } catch (Exception e) {
            log.error("获取字典项失败", e);
            throw new ServiceException("网格配置获取失败!");
        }
        if (dictionary == null) {
            return pairs;
        }
        try {
            return JsonUtils.deserializeToList(dictionary.getVariable(), GridCodeNameStringStringVO.class);
        } catch (Exception e) {
            throw new ServiceException("配置格式错误!");
        }
    }
}
