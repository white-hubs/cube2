package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.BlacklistParam;

/**
 * 英特灵达
 * 人员service
 */
public interface InteVisitorPersonService {

    Integer addPerson(BlacklistParam param);

    void removePerson(Integer personCode);
}
