package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.impl;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.BlacklistMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteVisitorGroupService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteVisitorPersonService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils.InteVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.BlacklistParam;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 英特灵达
 * 人员服务
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InteVisitorPersonServiceImpl implements InteVisitorPersonService {
    private final InteLoginService inteLoginService;
    private final InteVisitorGroupService inteVisitorGroupService;
    private final FileClient fileClient;
    private final BlacklistMapper blacklistMapper;
    private static final String ADD_PERSON_URL = "/openapi/integrated/person/insert";
    private static final String REMOVE_PERSON_URL = "/openapi/integrated/person/delete-by-code";

    /**
     * 新增人员
     *
     * @param param 人员信息
     *
     * @return 人员code
     */
    @Override
    public Integer addPerson(BlacklistParam param) {
        List<SystemLoginInfoPO> loginInfoList = inteLoginService.loginToken();
        Integer personCode = blacklistMapper.getThirdUserCode();
        int gender = 0;
        if ("男".equals(param.getSex())) {
            gender = 1;
        } else if ("女".equals(param.getSex())) {
            gender = 2;
        }
        List<PersonPicture> personPictureList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getPictures())) {
            String[] fileIds = param.getPictures().split(",");
            for (String fileId : fileIds) {
                PersonPicture picture = new PersonPicture();
                picture.setPicture(fileClient.getFileBase64(Long.valueOf(fileId)));
                picture.setIsDefault(1);
                picture.setPersonId(1);
                personPictureList.add(picture);
            }
        }
        if (loginInfoList != null && !loginInfoList.isEmpty()) {
            for (SystemLoginInfoPO loginInfoPO : loginInfoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("personCode", personCode);
                map.put("name", param.getName());
                map.put("groupId", inteVisitorGroupService.getDefaultGroupId(loginInfoPO));
                map.put("gender", gender);
                map.put("phoneNo", param.getPhone());
                map.put("personPicList", personPictureList);
                InteVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + ADD_PERSON_URL, map, loginInfoPO);
            }
        }
        return personCode;
    }

    /**
     * 删除人员
     *
     * @param personCode 人员code
     */
    @Override
    public void removePerson(Integer personCode) {
        List<SystemLoginInfoPO> loginInfoPOList = inteLoginService.loginToken();
        if (loginInfoPOList != null) {
            for (SystemLoginInfoPO loginInfoPO : loginInfoPOList) {
                Map<String, Object> map = new HashMap<>();
                map.put("personCode", personCode);
                InteVisitorHttpUtils.sendThirdPost(
                        loginInfoPO.getBaseUrl() + REMOVE_PERSON_URL + "?personCode=" + personCode, map, loginInfoPO);
            }
        }
    }

    @Data
    static class PersonPicture {
        private Integer personId; //人员ID
        private String picture; //人员图片
        private Integer isDefault; //是否人脸主图0：否， 1：是
    }
}
