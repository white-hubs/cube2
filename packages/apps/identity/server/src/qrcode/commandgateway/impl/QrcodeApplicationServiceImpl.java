package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeApplicationMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityApplicationPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wql
 * @describe 应用管理
 * @date 2023/10/23 11:38
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeApplicationServiceImpl extends ServiceImpl<QrcodeApplicationMapper, QrcodeApplicationPO> implements QrcodeApplicationService {

    private final QrcodeEntityApplicationService qrcodeEntityApplicationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveApplication(QrcodeApplicationCommand command) {
        List<QrcodeApplicationPO> list = this.lambdaQuery().and(q -> {
                    q.eq(QrcodeApplicationPO::getApplicationCode, command.getApplicationCode()).or()
                            .eq(QrcodeApplicationPO::getApplicationName, command.getApplicationName());
                })
                .ne(command.getId() != null, QrcodeApplicationPO::getId, command.getId()).list();
        Assert.isConflict(ObjectUtils.isNotEmpty(list), "应用已存在!");
        this.saveOrUpdate(ObjectUtils.copying(command, QrcodeApplicationPO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApplication(Long id) {
        List<QrcodeEntityApplicationPO> list = qrcodeEntityApplicationService.lambdaQuery()
                .eq(QrcodeEntityApplicationPO::getApplicationId, id)
                .eq(QrcodeEntityApplicationPO::getSelected, true).list();
        Assert.isConflict(ObjectUtils.isNotEmpty(list), "该应用已绑定实体类型,请先解绑!");
        qrcodeEntityApplicationService.remove(new LambdaQueryWrapper<QrcodeEntityApplicationPO>()
                                                      .eq(QrcodeEntityApplicationPO::getApplicationId, id));
        this.removeById(id);
    }
}
