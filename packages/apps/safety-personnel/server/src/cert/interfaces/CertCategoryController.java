package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertCategoryModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertCategoryService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertCategoryTreeVO;
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

import java.util.List;

/**
 * 证书分类配置接口。
 */
@RestController("safetyPersonnelCertCategoryController")
@RequestMapping
@RequiredArgsConstructor
public class CertCategoryController {

    private final CertCategoryService certCategoryService;

    /**
     * 查询证书分类树。
     *
     * @return 证书分类树。
     */
    @GetMapping("/cert/categories/tree")
    public List<CertCategoryTreeVO> tree() {
        return certCategoryService.tree();
    }

    /**
     * 新增根证书分类。
     *
     * @param param 分类新增参数
     *
     * @return 新增后的分类节点。
     */
    @PostMapping("/cert/categories")
    public CertCategoryTreeVO createRoot(@RequestBody @Valid CertCategoryModifyParam param) {
        return certCategoryService.createRoot(param);
    }

    /**
     * 新增子证书分类。
     *
     * @param id    父级分类ID
     * @param param 分类新增参数
     *
     * @return 新增后的分类节点。
     */
    @PostMapping("/cert/categories/{id}/children")
    public CertCategoryTreeVO createChild(@PathVariable("id") Long id, @RequestBody @Valid CertCategoryModifyParam param) {
        return certCategoryService.createChild(id, param);
    }

    /**
     * 编辑证书分类。
     *
     * @param id    分类ID
     * @param param 分类编辑参数
     *
     * @return 编辑后的分类节点。
     */
    @PutMapping("/cert/categories/{id}")
    public CertCategoryTreeVO update(@PathVariable("id") Long id, @RequestBody @Valid CertCategoryModifyParam param) {
        return certCategoryService.updateCategory(id, param);
    }

    /**
     * 逻辑删除证书分类；存在子分类或证书类型时拒绝删除。
     *
     * @param id 分类ID
     */
    @DeleteMapping("/cert/categories/{id}")
    public void delete(@PathVariable("id") Long id) {
        certCategoryService.deleteCategory(id);
    }
}
