package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallTaskEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface PersonRollCallTaskService extends IService<PersonRollCallTaskEntity> {

    /**
     * 分页查询点名任务记录列表
     */
    Pagination<PersonRollCallTaskPageVO> getPage(PersonRollCallTaskPageParam param);

    void insertOrUpdateTask(List<PersonRollCallTaskEntity> dataList);

    List<PersonRollCallTaskVO> getList(PersonRollCallListParam param);

}
