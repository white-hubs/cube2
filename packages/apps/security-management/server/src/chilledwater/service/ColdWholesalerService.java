package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWholesaler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AssociationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface ColdWholesalerService extends IService<ColdWholesaler> {
    void addWholesalers();

    Pagination<WholesalerVo> getWholesalers(WholesalerQueryParam param);

    void association(AssociationParam param);

    void associationCancellation(Long id);

    List<DataDeviceVo> getWholesalerDevices();

    List<DataDeviceVo> getWholesalerDevicesByUpdate(String deviceCode);

}
