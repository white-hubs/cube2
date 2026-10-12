package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductModelVersion;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductModelVersionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物模型版本Mapper v3版本
 * 操作iot_md_product_model_version_v3表
 */
@Mapper
public interface V3MdProductModelVersionMapper extends BaseMapper<MdProductModelVersion> {

    /**
     * 根据产品ID查询最新版本号
     *
     * @param prodId 产品ID
     * @return 最新版本号
     */
    Integer selectMaxVersionNumber(@Param("prodId") Long prodId);

    /**
     * 根据产品ID查询当前激活版本
     *
     * @param prodId 产品ID
     * @return 版本实体
     */
    MdProductModelVersion selectActiveVersion(@Param("prodId") Long prodId);

    /**
     * 分页查询版本列表
     *
     * @param page 分页对象
     * @param prodId 产品ID
     * @return 版本VO列表
     */
    IPage<ProductModelVersionVO> selectVersionPage(IPage<ProductModelVersionVO> page, @Param("prodId") Long prodId);

    /**
     * 查询版本详情
     *
     * @param versionId 版本ID
     * @return 版本VO
     */
    ProductModelVersionVO selectVersionDetail(@Param("versionId") Long versionId);

    /**
     * 停用产品的所有激活版本
     *
     * @param prodId 产品ID
     * @return 影响行数
     */
    int deactivateAllVersions(@Param("prodId") Long prodId);

    /**
     * 激活指定版本
     *
     * @param versionId 版本ID
     * @return 影响行数
     */
    int activateVersion(@Param("versionId") Long versionId);
}
