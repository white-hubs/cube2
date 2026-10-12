package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerRelationship;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
public interface WholesalerRelationshipService extends IService<WholesalerRelationship> {

    /**
     * 管理档位和业务之间的关系，将关系塞进中间表里头去
     * @param stallIds  多个档位ID集合
     * @param businessId 当前的业务id（协议ID或者问卷调查ID）
     * @param businessType 当前的业务type(协议type或者问卷调查type)
     */
    void saveBatch(List<String> stallIds,String businessId,String businessType);

    /**
     *
     * 根据业务ID和类型，定为到具体的业务记录，然后删除掉其和档位的对应关系
     * @param businessId 业务ID
     * @param businessType 业务类型
     */
    void removeBatch(String businessId,String businessType);
}
