package com.guhecloud.cube.srv.gen.identity.server.qrcode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityStylePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wql
 * @describe 实体关联二维码样式
 * @date 2024/1/30 10:21
 */
@Mapper
public interface QrcodeEntityStyleMapper extends BaseMapper<QrcodeEntityStylePO> {

    Long getDefaultStyleIdByEntityType(@Param("entityType") String entityType);
}
