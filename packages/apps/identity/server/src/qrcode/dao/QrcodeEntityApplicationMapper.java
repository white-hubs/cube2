package com.guhecloud.cube.srv.gen.identity.server.qrcode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityApplicationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 实体应用绑定关系
 * @date 2023/10/23 10:40
 */
@Mapper
public interface QrcodeEntityApplicationMapper extends BaseMapper<QrcodeEntityApplicationPO> {
    /**
     * @Author wql
     * @Description 实体类型与应用绑定关系列表
     * @Date 11:30 2023/10/23
     **/
    List<QrcodeEntityApplicationVO> entityApplicationList(@Param("entityTypeId") Long entityTypeId, @Param("codes") List<String> codes);

    /**
     * @Author wql
     * @Description 根据实体类型获取绑定的应用信息
     * @Date 15:52 2023/10/27
     **/
    List<QrcodeApplicationPO> getEntityTypeApplication(@Param("entityType") String entityType);
}
