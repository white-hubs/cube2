package com.guhecloud.cube.srv.gen.identity.server.qrcode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityTypeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 10:39
 */
@Mapper
public interface QrcodeEntityTypeMapper extends BaseMapper<QrcodeEntityTypePO> {
    List<QrcodeEntityTypeVO> typeList(@Param("keyword") String keyword);
}
