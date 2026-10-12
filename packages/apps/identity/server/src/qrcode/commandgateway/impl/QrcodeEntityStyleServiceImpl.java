package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityStyleService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeEntityStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityStylePO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 实体类型管理二维码样式
 * @date 2024/1/30 10:23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityStyleServiceImpl extends ServiceImpl<QrcodeEntityStyleMapper, QrcodeEntityStylePO> implements QrcodeEntityStyleService {

    @Override
    public void bindEntityTypeStyle(Long entityType, Long styleId) {
        //一个实体类型只能绑定一个二维码样式,先删除后新增
        this.remove(new LambdaQueryWrapper<QrcodeEntityStylePO>().eq(QrcodeEntityStylePO::getEntityType, entityType));
        QrcodeEntityStylePO stylePO = new QrcodeEntityStylePO();
        stylePO.setEntityType(entityType);
        stylePO.setStyleId(styleId);
        this.save(stylePO);
    }

    @Override
    public void unbindEntityTypeStyle(Long entityType, Long styleId) {
        this.remove(new LambdaQueryWrapper<QrcodeEntityStylePO>()
                            .eq(QrcodeEntityStylePO::getEntityType, entityType)
                            .eq(QrcodeEntityStylePO::getStyleId, styleId));
    }
}
