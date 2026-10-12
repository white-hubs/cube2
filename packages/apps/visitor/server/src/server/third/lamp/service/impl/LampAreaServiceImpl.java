package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 区域
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LampAreaServiceImpl implements LampAreaService {

    private final LampLoginService loginService;
    private static final String AREA_URL = "/evo-apigw/evo-visitor/1.0.0/area/";

    /**
     * 区域下拉列表
     *
     * @param param 入参
     *
     * @return 列表
     */
    @Override
    public List<AreaSelectResult> getAreaList(KeyWordPageParam param) {

        SystemLoginInfoPO loginInfoPO = loginService.loginToken();
        Object obj = LampVisitorHttpUtils.sendThirdGet(loginInfoPO.getBaseUrl() + AREA_URL, null, loginInfoPO);
        if (ObjectUtils.isEmpty(obj)) {
            return new ArrayList<>();
        }
        JSONArray jsonArray = (JSONArray) obj;
        List<Area> areaList = jsonArray.toJavaList(Area.class);

        List<AreaSelectResult> list = new ArrayList<>();
        for (Area area : areaList) {
            AreaSelectResult result = new AreaSelectResult();
            result.setId(area.getId());
            result.setName(area.getAreaName());
            list.add(result);
        }

        return list;
    }

    /**
     * 区域详情
     *
     * @param areaId 区域id
     *
     * @return 详情
     */
    @Override
    public VisitAreaResult areaDetail(Long areaId) {
        VisitAreaResult result = new VisitAreaResult();
        SystemLoginInfoPO loginInfoPO = loginService.loginToken();
        Object obj = LampVisitorHttpUtils.sendThirdGet(loginInfoPO.getBaseUrl() + AREA_URL + areaId, new HashMap<>(), loginInfoPO);
        if (obj == null) {
            return result;
        }
        JSONObject jsonObject = (JSONObject) obj;
        AreaDetail detail = jsonObject.toJavaObject(AreaDetail.class);

        result.setId(detail.id);
        result.setAreaName(detail.getAreaName());
        result.setCreateTime(DateUtils.parse(detail.getCreateTime()));

        Object listObj = jsonObject.get("vimsChannel");
        if (ObjectUtils.isNotEmpty(listObj)) {
            JSONArray jsonArray = (JSONArray) listObj;
            result.setDeviceNumber(jsonArray.size());
        }

        return result;
    }

    @Data
    static class Area {
        private String areaName;
        private String createTime;
        private Long id;
    }

    @Data
    static class AreaDetail {
        private Long id;
        private String areaName;
        private String createTime;
    }
}
