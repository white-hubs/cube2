package com.guhecloud.cube.srv.prepaid.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.prepaid.common.vo.AmmeterDTO;
import com.guhecloud.cube.srv.prepaid.server.third.utils.GhHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电表第三方接口
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmmeterClient {

    private final GhHttpUtils ghHttpUtils;

    /**
     * 获取电表列表
     *
     * @param meterId 表id(非模糊匹配
     *
     * @return 电表列表
     */
    public List<AmmeterDTO> getAmmeterList(String meterId) {
        String ammeterListUrl = "/api/Yun/GetAllMeters";

        List<AmmeterDTO> resultList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        if (StringUtils.isNotBlank(meterId)) {
            dataMap.put("meterId", meterId);
        }
        long startHttp = System.currentTimeMillis();
        Object obj = ghHttpUtils.doubleRequestAgain(ammeterListUrl, dataMap);
        log.warn("电表第三方http请求耗时(毫秒)：{}", System.currentTimeMillis() - startHttp);
        if (obj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) obj;
        resultList = jsonArray.toJavaList(AmmeterDTO.class);
        return resultList;
    }
}
