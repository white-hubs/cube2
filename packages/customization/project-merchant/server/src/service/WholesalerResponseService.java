package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-12
 */
public interface WholesalerResponseService extends IService<WholesalerResponse> {
    /**
     * 将回答的内容存放在中间表中
     * @param response 回答关联的相关ID
     */
     void saveOrUpdateResponse(WholesalerResponse response);
}
