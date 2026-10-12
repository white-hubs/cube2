package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseAppealCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.FileCommonApi;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseAppealParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseAppealMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseAppealPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.validator.EnterpriseAppealValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseAppealCommandServiceImpl extends ServiceImpl<EnterpriseAppealMapper, EnterpriseAppealPO> implements EnterpriseAppealCommandService {

    private final EnterpriseAppealValidator validator;

    private final FileCommonApi fileCommonApi;

    private final EnterpriseMapper enterpriseMapper;

    @Override
    public void save(EnterpriseAppealParam param) {
        // 校验名称重复
        validator.verifyIsExistByName(param.getName());

        EnterpriseAppealPO po = ObjectUtils.copying(param, EnterpriseAppealPO.class);
        EnterprisePO enterprisePO = enterpriseMapper.selectById(po.getEnterpriseId());
        if (Objects.nonNull(enterprisePO)) {
            po.setEnterpriseName(enterprisePO.getName());
        }
        // 保存
        this.save(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());
    }

    @Override
    public void update(Long id, EnterpriseAppealParam param) {
        EnterpriseAppealPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的企业!");

        validator.verifyIsExistUpdateByName(id, param.getName());

        // 更新
        ObjectUtils.copying(param, po);
        this.updateById(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());
    }

    @Override
    public void remove(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

}
