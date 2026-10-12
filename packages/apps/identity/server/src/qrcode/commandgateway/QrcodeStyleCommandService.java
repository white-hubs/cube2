package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeStyleCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;

/**
 * @author bq
 * @date 2022/7/20
 * @description
 */
public interface QrcodeStyleCommandService extends IService<QrcodeStylePO> {

    void save(QrcodeStyleCommand command);

    void update(Long id, QrcodeStyleCommand command);

    void remove(Long id);

    void removeBatch(String ids);

}
