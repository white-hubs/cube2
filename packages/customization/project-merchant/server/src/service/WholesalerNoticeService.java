package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerNoticeDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerNoticeSupDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerNotice;

import java.util.List;

/**
 * <p>
 * 商户相关功能通知市场方（目前合同、认证） 服务类
 * </p>
 *
 * @author bq
 * @since 2022-03-09
 */
public interface WholesalerNoticeService extends IService<WholesalerNotice> {

    WholesalerNoticeSupDTO getList(String projectId, String businessType);

    void save(WholesalerNoticeSupDTO dto);

    List<WholesalerNoticeDTO> getNoticeIds(String businessType);
}
