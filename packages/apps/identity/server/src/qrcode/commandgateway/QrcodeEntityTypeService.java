package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityTypeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.EntityStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityTypeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 10:42
 */
public interface QrcodeEntityTypeService extends IService<QrcodeEntityTypePO> {
    void saveType(QrcodeEntityTypeCommand typeCommand);

    List<QrcodeEntityTypeVO> typeList(String keyword);

    Pagination<EntityStyleVO> entityTypeStyle(QrcodeQueryParam param);

    boolean removeById(Long id);
}
