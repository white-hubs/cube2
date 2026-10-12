package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface PersonRollCallService extends IService<PersonRollCallEntity> {

    /**
     * 分页查询人员点到明细列表
     */
    Pagination<PersonRollCallPageVO> getPage(PersonRollCallPageParam param);

    void insertOrUpdateMeetingApplyPerson(List<PersonRollCallEntity> dataList);
}
