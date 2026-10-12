package com.guhecloud.cube.biz.mdata.server.purchaser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserInnerModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.vo.PurchaserAuthenticationVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/24
 */
public interface PurchaserService extends IService<Purchaser> {

    void addPurchaser(PurchaserModifyParam param);

    void updatePurchaserName(Long userId, String userName);

    Purchaser getPurchaser(Long userId, Integer tenant);

    List<Purchaser> listByIds(Collection<? extends Serializable> ids);

    List<Purchaser> listByUserId(Serializable userId);

    Long addInnerPurchaser(PurchaserInnerModifyParam param);

    Page<PurchaserAuthenticationVO> pageForPurchaser(PurchaserAuthenticationQueryParam param);

    Page<PurchaserAuthenticationVO> pageForPurchaserVerify(PurchaserAuthenticationQueryParam param);
}
