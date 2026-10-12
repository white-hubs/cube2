package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 人员证书服务。
 */
public interface EmployeeCertService extends IService<EmployeeCert> {

    /**
     * 分页查询人员证书。
     *
     * @param param 查询参数
     *
     * @return 人员证书分页。
     */
    Pagination<EmployeeCertVO> pageCerts(EmployeeCertQueryParam param);

    /**
     * 导出员工证书 Excel，筛选条件与分页列表一致。
     *
     * @param param    查询参数
     * @param response HTTP 响应
     */
    void exportCerts(EmployeeCertQueryParam param, HttpServletResponse response) throws IOException;

    /**
     * 查询人员证书详情。
     *
     * @param id 人员证书ID
     *
     * @return 人员证书详情。
     */
    EmployeeCertVO detail(Long id);

    /**
     * 新增人员证书。
     *
     * @param param 保存参数
     *
     * @return 新增后的人员证书。
     */
    EmployeeCertVO create(EmployeeCertModifyParam param);

    /**
     * 编辑人员证书。
     *
     * @param id    人员证书ID
     * @param param 保存参数
     *
     * @return 编辑后的人员证书。
     */
    EmployeeCertVO updateCert(Long id, EmployeeCertModifyParam param);

    /**
     * 逻辑删除人员证书。
     *
     * @param id 人员证书ID
     */
    void deleteCert(Long id);

    /**
     * 查询指定人员持有证书。
     *
     * @param userId OUAA用户ID
     *
     * @return 人员证书列表。
     */
    List<EmployeeCertVO> listByUserId(Long userId);
}
