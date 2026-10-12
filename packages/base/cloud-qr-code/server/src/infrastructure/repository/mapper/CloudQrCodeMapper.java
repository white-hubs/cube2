package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrCodePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CloudQrCodeMapper extends BaseMapper<CloudQrCodePO> {
}
