package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeEntityApplicationMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityApplicationPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 实体应用绑定关系
 * @date 2023/10/23 10:45
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityApplicationServiceImpl extends ServiceImpl<QrcodeEntityApplicationMapper, QrcodeEntityApplicationPO> implements QrcodeEntityApplicationService {
    private final ApplicationClient applicationClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveType(QrcodeEntityApplicationCommand command) {
        checkAndSetSelected(command);
        if (command.getId() != null) {
            QrcodeEntityApplicationPO byId = this.getById(command.getId());
            if (ObjectUtils.isNotNull(byId)) {
                byId.setEntityId(command.getEntityId());
                byId.setApplicationId(command.getApplicationId());
                byId.setSelected(command.getSelected());
                this.updateById(byId);
                return;
            }
        }
        this.save(ObjectUtils.copying(command, QrcodeEntityApplicationPO.class));
    }

    @Override
    public List<QrcodeEntityApplicationVO> entityApplicationList(Long entityTypeId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<ApplicationDTO> applications = applicationClient.getTenantApplications(tenant);
        if (ObjectUtils.isNotEmpty(applications)) {
            List<String> codes = applications.stream().map(ApplicationDTO::getCode).collect(Collectors.toList());
            return this.baseMapper.entityApplicationList(entityTypeId, codes);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<QrcodeEntityApplicationCommand> commands) {
        List<QrcodeEntityApplicationPO> updates = new ArrayList<>();
        List<QrcodeEntityApplicationPO> adds = new ArrayList<>();
        for (QrcodeEntityApplicationCommand command : commands) {
            checkAndSetSelected(command);
            if (command.getId() != null) {
                QrcodeEntityApplicationPO byId = this.getById(command.getId());
                if (ObjectUtils.isNotNull(byId)) {
                    byId.setEntityId(command.getEntityId());
                    byId.setApplicationId(command.getApplicationId());
                    byId.setSelected(command.getSelected());
                    updates.add(byId);
                    continue;
                }
            }
            adds.add(ObjectUtils.copying(command, QrcodeEntityApplicationPO.class));
        }
        this.updateBatchById(updates);
        this.saveBatch(adds);
    }

    private void checkAndSetSelected(QrcodeEntityApplicationCommand command) {
        List<QrcodeEntityApplicationPO> list = this.lambdaQuery().eq(QrcodeEntityApplicationPO::getEntityId, command.getEntityId())
                .eq(QrcodeEntityApplicationPO::getApplicationId, command.getApplicationId())
                .ne(command.getId() != null, QrcodeEntityApplicationPO::getId, command.getId()).list();
        Assert.isConflict(ObjectUtils.isNotEmpty(list), "实体与应用绑定关系已存在");
        command.setSelected(command.getSelected() != null && command.getSelected());
    }
}
