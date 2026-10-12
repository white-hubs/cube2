package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;

/**
 * @author wql
 * @describe 应用
 * @date 2023/10/23 11:37
 */
public interface QrcodeApplicationService extends IService<QrcodeApplicationPO> {
    void saveApplication(QrcodeApplicationCommand command);

    void deleteApplication(Long id);
}
