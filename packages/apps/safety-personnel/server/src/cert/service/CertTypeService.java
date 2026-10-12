package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertTypeVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 证书类型服务。
 */
public interface CertTypeService extends IService<CertType> {

    /**
     * 分页查询证书类型。
     *
     * @param param 查询参数
     *
     * @return 证书类型分页。
     */
    Pagination<CertTypeVO> pageTypes(CertTypeQueryParam param);

    /**
     * 查询证书类型详情。
     *
     * @param id 证书类型ID
     *
     * @return 证书类型详情。
     */
    CertTypeVO detail(Long id);

    /**
     * 新增证书类型。
     *
     * @param param 保存参数
     *
     * @return 新增后的证书类型。
     */
    CertTypeVO create(CertTypeModifyParam param);

    /**
     * 编辑证书类型。
     *
     * @param id    证书类型ID
     * @param param 保存参数
     *
     * @return 编辑后的证书类型。
     */
    CertTypeVO updateType(Long id, CertTypeModifyParam param);

    /**
     * 删除或禁用证书类型；已被证书引用时只禁用。
     *
     * @param id 证书类型ID
     */
    void deleteType(Long id);
}
