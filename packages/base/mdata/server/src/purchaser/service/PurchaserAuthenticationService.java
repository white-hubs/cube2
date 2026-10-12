package com.guhecloud.cube.biz.mdata.server.purchaser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.PurchaserAuthentication;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationQueryParam;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/24
 */
public interface PurchaserAuthenticationService extends IService<PurchaserAuthentication> {

    void addPurchaserAuthentication(PurchaserAuthenticationModifyParam param);

    void addPurchaserAuthenticationForPC(Long purchaserId, PurchaserAuthenticationModifyParam param);

    void auditPurchaserAuthentication(Long id, PurchaserAuthenticationEnum status, String remark);

    void resetPurchaserAuthentication(Long id);

    PurchaserAuthentication getByPurchaser(Long purchaserId, Integer tenant);

    PurchaserAuthentication getById(Serializable id);

    List<PurchaserAuthentication> getLastByPurchaser(Long purchaserId, Integer tenant);

    List<PurchaserAuthentication> listByTenant(Integer tenant);

    Page<PurchaserAuthentication> pageAuthentication(PurchaserAuthenticationQueryParam queryParam);
}
