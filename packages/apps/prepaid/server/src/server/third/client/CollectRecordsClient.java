package com.guhecloud.cube.srv.prepaid.server.third.client;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.prepaid.common.vo.CollectRecordsDTO;
import com.guhecloud.cube.srv.prepaid.server.third.utils.GhHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 电表抄表记录第三方接口
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectRecordsClient {

    private final GhHttpUtils ghHttpUtils;

    /**
     * 获取电表抄表记录
     *
     * @param meterId 表id(非模糊匹配
     *
     * @return 电表抄表记录列表
     */
    public CollectRecordsDTO getCollectRecordsDTO(String meterId) {
        String collectRecordsListUrl = "/api/Yun/GetCollect";

        Map<String, Object> dataMap = new HashMap<>();
        if (StringUtils.isNotBlank(meterId)) {
            dataMap.put("meterId", meterId);
        }
        long startHttp = System.currentTimeMillis();
        Object obj = ghHttpUtils.doubleRequestAgain(collectRecordsListUrl, dataMap);
        log.warn("电表抄表记录第三方http请求耗时(毫秒)：{}", System.currentTimeMillis() - startHttp);
        if (obj == null) {
            return null;
        }
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.toJavaObject(CollectRecordsDTO.class);
    }
}
