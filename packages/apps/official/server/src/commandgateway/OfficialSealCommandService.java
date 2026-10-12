package com.guhecloud.cube.srv.gen.official.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.OfficialSealCommand;
import com.guhecloud.cube.srv.gen.official.management.server.dao.OfficialSealMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.OfficialSealPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author appet
 * @date 2023/02/21
 * @description 印章管理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialSealCommandService extends ServiceImpl<OfficialSealMapper, OfficialSealPO> {

    /**
     * 新增印章
     *
     * @param command
     *
     * @return
     */
    public Long addOfficialSeal(OfficialSealCommand command) {
        OfficialSealPO sealPO = ObjectUtils.copying(command, OfficialSealPO.class);
        this.save(sealPO);
        return sealPO.getId();
    }

    /**
     * 修改印章
     *
     * @param id
     * @param command
     *
     * @return
     */
    public void updateOfficialSeal(Long id, OfficialSealCommand command) {
        OfficialSealPO sealPO = this.getById(id);
        Assert.isNotFound(sealPO, "未找到要修改的印章");
        ObjectUtils.copying(command, sealPO);
        this.updateById(sealPO);
    }

    /**
     * 删除印章
     *
     * @param id
     */
    public void deleteOfficialSeal(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
