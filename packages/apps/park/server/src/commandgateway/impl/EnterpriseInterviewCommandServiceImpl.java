package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseInterviewCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.FileCommonApi;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseInterviewParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseInterviewMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseInterviewPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseInterviewCommandServiceImpl extends ServiceImpl<EnterpriseInterviewMapper, EnterpriseInterviewPO> implements EnterpriseInterviewCommandService {

    private final FileCommonApi fileCommonApi;

    private final EnterpriseMapper enterpriseMapper;

    @Override
    public void save(EnterpriseInterviewParam param) {

        EnterpriseInterviewPO po = ObjectUtils.copying(param, EnterpriseInterviewPO.class);
        EnterprisePO enterprisePO = enterpriseMapper.selectById(po.getEnterpriseId());
        po.setEnterpriseName(enterprisePO.getName());
        // 保存
        this.save(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());
    }

    @Override
    public void update(Long id, EnterpriseInterviewParam param) {
        EnterpriseInterviewPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的走访记录!");

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
