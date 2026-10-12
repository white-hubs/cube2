package com.guhecloud.cube.srv.gen.identity.server.qrcode.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.OldQrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/20
 */
@Mapper
public interface QrcodeMapper extends BaseMapper<QrcodePO> {
    /**
     * 统计待迁移的二维码总数
     *
     * @param type 绑定对象类型
     *
     * @return 总数
     */
    int countOldQrcodeNum(@Param("type") String type);

    /**
     * 查询待迁移二维码详情列表
     *
     * @param type  绑定对象类型
     * @param begin 偏移开始量
     *
     * @return 二维码详情列表
     */
    List<OldQrcodePO> getOldQrcodeList(@Param("type") String type, @Param("begin") int begin);

    /**
     * 根据二维码查询租户ID
     *
     * @param code
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectTenantByQrCode(@Param("code") String code);
}
