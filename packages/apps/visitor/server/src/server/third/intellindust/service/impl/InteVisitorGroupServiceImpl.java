package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteVisitorGroupService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils.InteVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils.InteVisitorJsonUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 英特灵达
 * 人员分组服务
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InteVisitorGroupServiceImpl implements InteVisitorGroupService {
    private static final String ADD_GROUP_URL = "/openapi/integrated/person/group/insert";
    private static final String GET_GROUP_URL = "/openapi/integrated/person/group/find";
    private static final String DEFAULT_GROUP_NAME = "古河云访客报警默认组";

    /**
     * 添加人员分组
     *
     * @return 组id
     */
    @Override
    public Long addGroup(SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            return -1L;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", 9999);
        map.put("groupType", 1);
        map.put("groupName", DEFAULT_GROUP_NAME);
        map.put("comment", "古河云黑名单报警人员默认分组");
        Object obj = InteVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + ADD_GROUP_URL, map, loginInfoPO);
        if (obj != null) {
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject.getLongValue("groupId");
        }
        return 0L;
    }

    /**
     * 获取默认分组
     *
     * @return 分组id
     */
    @Override
    public Long getDefaultGroupId(SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            return -1L;
        }
        Map<String, Object> map = new HashMap<>();
        Object obj = InteVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + GET_GROUP_URL, map, loginInfoPO);
        if (obj == null) {
            return 0L;
        }

        List<PersonGroup> personGroupList = InteVisitorJsonUtils.getObjArrList((JSONArray) obj, PersonGroup.class);
        for (PersonGroup personGroup : personGroupList) {
            if (DEFAULT_GROUP_NAME.equals(personGroup.groupName)) {
                return personGroup.getGroupId().longValue();
            }
        }

        return addGroup(loginInfoPO);

    }

    @Data
    static class PersonGroup {
        private Integer groupId; //人员分组ID
        private String groupType; //人员分组类型id
        private String groupName; //人员分组名称
        private Integer groupCode; //人员分组编码
        private Integer maxCount; //人员分组最大数量
        private String actualCount; //人员分组实际数量
        private String comment; //说明
    }
}
