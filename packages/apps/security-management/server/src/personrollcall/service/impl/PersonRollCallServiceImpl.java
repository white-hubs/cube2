package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.dao.PersonRollCallMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallServiceImpl extends ServiceImpl<PersonRollCallMapper, PersonRollCallEntity> implements PersonRollCallService {

    private final PersonRollCallMapper personRollCallMapper;

    @Override
    public Pagination<PersonRollCallPageVO> getPage(PersonRollCallPageParam param) {
        Page<PersonRollCallPageVO> recordPage = personRollCallMapper.getPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(recordPage);
    }

    @Override
    public void insertOrUpdateMeetingApplyPerson(List<PersonRollCallEntity> dataList) {
        personRollCallMapper.insertOrUpdatePersonRollCall(dataList);
    }
}
