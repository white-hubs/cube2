package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityApplicationPO;

import java.util.List;

/**
 * @author wql
 * @describe 实体与应用绑定关系
 * @date 2023/10/23 10:43
 */
public interface QrcodeEntityApplicationService extends IService<QrcodeEntityApplicationPO> {
    void saveType(QrcodeEntityApplicationCommand command);

    List<QrcodeEntityApplicationVO> entityApplicationList(Long entityTypeId);

    void batchSave(List<QrcodeEntityApplicationCommand> commands);
}
