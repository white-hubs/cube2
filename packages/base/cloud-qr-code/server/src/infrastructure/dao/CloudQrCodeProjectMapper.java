package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQRCodeProjectPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 云二维码项目Mapper
 */
@Mapper
public interface CloudQrCodeProjectMapper extends BaseMapper<CloudQRCodeProjectPO> {
}
