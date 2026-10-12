package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerFirstPart;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerFirstPartDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
public interface WholesalerFirstPartService extends IService<WholesalerFirstPart> {

    String TYPE_FIRST_PART = "FIRST-PART";

    /**
     * 根据项目ID查看那个项目的甲方信息
     * @param projectId 项目ID
     * @return 甲方信息
     */
    WholesalerFirstPartDTO getFirstPart(String projectId);
    /**
     * 新增或者修改甲方信息
     * @param dto 甲方信息
     * @return 返回ID
     */
    Long saveOrUpdate(WholesalerFirstPartDTO dto);


}
