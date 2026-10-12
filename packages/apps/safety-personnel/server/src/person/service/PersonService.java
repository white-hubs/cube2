package com.guhecloud.cube.srv.gen.safety.personnel.server.person.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonLinkParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonPoolQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonPoolUserVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonProfileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 安全人员台账服务。
 */
public interface PersonService extends IService<Person> {

    /**
     * 分页查询安全人员。
     *
     * @param param 查询参数
     *
     * @return 安全人员分页。
     */
    Pagination<PersonVO> pagePersons(PersonQueryParam param);

    /**
     * 查询可关联人员池。
     *
     * @param param 查询参数
     *
     * @return 可关联OUAA用户分页。
     */
    Pagination<PersonPoolUserVO> pagePool(PersonPoolQueryParam param);

    /**
     * 批量关联安全人员。
     *
     * @param param 关联参数
     */
    void link(PersonLinkParam param);

    /**
     * 逻辑移除安全人员。
     *
     * @param id 安全人员ID
     */
    void removePerson(Long id);

    /**
     * 查询人员抽屉基础信息。
     *
     * @param id 安全人员ID
     *
     * @return 人员基础信息。
     */
    PersonProfileVO profile(Long id);

    /**
     * 查询人员持有证书。
     *
     * @param id 安全人员ID
     *
     * @return 人员证书列表。
     */
    List<EmployeeCertVO> certificates(Long id);

    /**
     * 导出当前筛选条件下的安全人员台账。
     *
     * @param param    与分页列表相同的筛选参数
     * @param response HTTP响应
     */
    void exportPersons(PersonQueryParam param, HttpServletResponse response) throws IOException;
}
