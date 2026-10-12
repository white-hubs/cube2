package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityStylePO;

/**
 * @author wql
 * @describe 实体关联二维码样式
 * @date 2024/1/30 10:22
 */
public interface QrcodeEntityStyleService extends IService<QrcodeEntityStylePO> {
    void bindEntityTypeStyle(Long entityType, Long styleId);

    void unbindEntityTypeStyle(Long entityType, Long styleId);
}
