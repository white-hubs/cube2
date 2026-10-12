package com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.PersonTypeRel;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.mapper.PersonTypeRelMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonTypeRelService;
import org.springframework.stereotype.Service;

/**
 * 安全人员类型关系服务实现。
 */
@Service("safetyPersonnelPersonTypeRelService")
public class PersonTypeRelServiceImpl extends ServiceImpl<PersonTypeRelMapper, PersonTypeRel> implements PersonTypeRelService {
}
