package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPerson;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.AreaOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.SupervisorOptionVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface KeyPersonService extends IService<KeyPerson> {
    Pagination<KeyPersonPageVO> getPage(KeyPersonPageParam param);

    List<KeyPersonOptionVO> listOptions(String keyword);

    KeyPersonDetailVO getDetail(Long id);

    Long create(KeyPersonSaveParam param);

    void update(Long id, KeyPersonSaveParam param);

    void delete(Long id);

    void withdraw(Long id);

    void activate(Long id);

    KeyPersonStatisticsVO getStatistics();

    List<AreaOptionVO> listAreaOptions(String keyword);

    List<SupervisorOptionVO> listSupervisorOptions(String keyword);
}
