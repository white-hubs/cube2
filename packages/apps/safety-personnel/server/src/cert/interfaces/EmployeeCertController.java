package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 员工证书管理接口。
 */
@RestController("safetyPersonnelEmployeeCertController")
@RequestMapping
@RequiredArgsConstructor
public class EmployeeCertController {

    private final EmployeeCertService employeeCertService;

    /**
     * 分页查询员工证书。
     *
     * @param param 查询参数
     *
     * @return 员工证书分页。
     */
    @GetMapping("/certificates")
    public Pagination<EmployeeCertVO> page(EmployeeCertQueryParam param) {
        return employeeCertService.pageCerts(param);
    }

    /**
     * 导出员工证书 Excel，筛选条件与分页列表一致。
     *
     * @param param    查询参数
     * @param response HTTP 响应
     */
    @PostMapping("/certificates/export")
    public void export(@RequestBody EmployeeCertQueryParam param, HttpServletResponse response) throws IOException {
        employeeCertService.exportCerts(param, response);
    }

    /**
     * 查询员工证书详情。
     *
     * @param id 员工证书ID
     *
     * @return 员工证书详情。
     */
    @GetMapping("/certificates/{id}")
    public EmployeeCertVO detail(@PathVariable("id") Long id) {
        return employeeCertService.detail(id);
    }

    /**
     * 新增员工证书。
     *
     * @param param 证书新增参数
     *
     * @return 新增后的员工证书。
     */
    @PostMapping("/certificates")
    public EmployeeCertVO create(@RequestBody @Valid EmployeeCertModifyParam param) {
        return employeeCertService.create(param);
    }

    /**
     * 编辑员工证书，复审状态也通过此接口手动维护。
     *
     * @param id    员工证书ID
     * @param param 证书编辑参数
     *
     * @return 编辑后的员工证书。
     */
    @PutMapping("/certificates/{id}")
    public EmployeeCertVO update(@PathVariable("id") Long id, @RequestBody @Valid EmployeeCertModifyParam param) {
        return employeeCertService.updateCert(id, param);
    }

    /**
     * 逻辑删除员工证书。
     *
     * @param id 员工证书ID
     */
    @DeleteMapping("/certificates/{id}")
    public void delete(@PathVariable("id") Long id) {
        employeeCertService.deleteCert(id);
    }
}
