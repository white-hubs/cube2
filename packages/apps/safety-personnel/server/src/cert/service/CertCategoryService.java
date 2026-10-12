package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertCategory;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertCategoryModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertCategoryTreeVO;

import java.util.List;

/**
 * 证书分类服务。
 */
public interface CertCategoryService extends IService<CertCategory> {

    /**
     * 查询证书分类树。
     *
     * @return 证书分类树列表。
     */
    List<CertCategoryTreeVO> tree();

    /**
     * 新增根证书分类。
     *
     * @param param 分类保存参数
     *
     * @return 新增后的分类节点。
     */
    CertCategoryTreeVO createRoot(CertCategoryModifyParam param);

    /**
     * 新增子证书分类。
     *
     * @param parentId 父级分类ID
     * @param param    分类保存参数
     *
     * @return 新增后的分类节点。
     */
    CertCategoryTreeVO createChild(Long parentId, CertCategoryModifyParam param);

    /**
     * 编辑证书分类。
     *
     * @param id    分类ID
     * @param param 分类保存参数
     *
     * @return 编辑后的分类节点。
     */
    CertCategoryTreeVO updateCategory(Long id, CertCategoryModifyParam param);

    /**
     * 逻辑删除证书分类。
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
}
