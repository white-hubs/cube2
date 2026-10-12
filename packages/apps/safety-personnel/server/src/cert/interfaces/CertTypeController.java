package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertTypeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertTypeVO;
import com.guhecloud.platform.base.model.Pagination;
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

/**
 * 证书类型配置接口。
 */
@RestController("safetyPersonnelCertTypeController")
@RequestMapping
@RequiredArgsConstructor
public class CertTypeController {

    private final CertTypeService certTypeService;

    /**
     * 分页查询证书类型。
     *
     * @param param 查询参数
     *
     * @return 证书类型分页。
     */
    @GetMapping("/cert/types")
    public Pagination<CertTypeVO> page(CertTypeQueryParam param) {
        return certTypeService.pageTypes(param);
    }

    /**
     * 查询证书类型详情。
     *
     * @param id 证书类型ID
     *
     * @return 证书类型详情。
     */
    @GetMapping("/cert/types/{id}")
    public CertTypeVO detail(@PathVariable("id") Long id) {
        return certTypeService.detail(id);
    }

    /**
     * 新增证书类型。
     *
     * @param param 类型新增参数
     *
     * @return 新增后的证书类型。
     */
    @PostMapping("/cert/types")
    public CertTypeVO create(@RequestBody @Valid CertTypeModifyParam param) {
        return certTypeService.create(param);
    }

    /**
     * 编辑证书类型。
     *
     * @param id    证书类型ID
     * @param param 类型编辑参数
     *
     * @return 编辑后的证书类型。
     */
    @PutMapping("/cert/types/{id}")
    public CertTypeVO update(@PathVariable("id") Long id, @RequestBody @Valid CertTypeModifyParam param) {
        return certTypeService.updateType(id, param);
    }

    /**
     * 删除证书类型；已被证书引用时不允许删除。
     *
     * @param id 证书类型ID
     */
    @DeleteMapping("/cert/types/{id}")
    public void delete(@PathVariable("id") Long id) {
        certTypeService.deleteType(id);
    }
}
