package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysUserFeedback;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysUserFeedbackVO;

import java.util.ArrayList;
import java.util.List;

public final class SysUserFeedbackConverts {

    private SysUserFeedbackConverts() {
    }

    public static SysUserFeedbackVO toVO(SysUserFeedback entity) {
        SysUserFeedbackVO vo = new SysUserFeedbackVO();
        vo.setId(entity.getId());
        vo.setUserName(entity.getUserName());
        vo.setContactWay(entity.getContactWay());
        vo.setCompanyName(entity.getCompanyName());
        vo.setApplicationScenario(entity.getApplicationScenario());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    public static List<SysUserFeedbackVO> toVOList(List<SysUserFeedback> entities) {
        List<SysUserFeedbackVO> result = new ArrayList<>();
        for (SysUserFeedback entity : entities) {
            result.add(toVO(entity));
        }
        return result;
    }

    public static SysUserFeedback toEntity(SysUserFeedbackParam param) {
        SysUserFeedback entity = new SysUserFeedback();
        entity.setUserName(param.getUserName());
        entity.setContactWay(param.getContactWay());
        entity.setCompanyName(param.getCompanyName());
        entity.setApplicationScenario(param.getApplicationScenario());
        return entity;
    }

}
