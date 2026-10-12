package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.dao.PersonRollCallTaskMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallTaskEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallTaskService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallTaskServiceImpl extends ServiceImpl<PersonRollCallTaskMapper, PersonRollCallTaskEntity> implements PersonRollCallTaskService {

    private final PersonRollCallTaskMapper personRollCallTaskMapper;

    @Override
    public Pagination<PersonRollCallTaskPageVO> getPage(PersonRollCallTaskPageParam param) {
        Page<PersonRollCallTaskPageVO> recordPage = personRollCallTaskMapper.getPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(recordPage);
    }

    @Override
    public void insertOrUpdateTask(List<PersonRollCallTaskEntity> dataList) {
        personRollCallTaskMapper.insertOrUpdateTask(dataList);
    }

    @Override
    public List<PersonRollCallTaskVO> getList(PersonRollCallListParam param) {
        return personRollCallTaskMapper.getList(param);
    }
}
