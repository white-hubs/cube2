package com.guhecloud.cube.srv.gen.official.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkCategoryCommand;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkCategoryMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author appet
 * @date 2023/02/21
 * @description 事项类型
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkCategoryCommandService extends ServiceImpl<FocusWorkCategoryMapper, FocusWorkCategoryPO> {

    /**
     * 新增类型
     *
     * @param command
     *
     * @return
     */
    public Long addFocusWorkCategory(FocusWorkCategoryCommand command) {
        FocusWorkCategoryPO categoryPO = ObjectUtils.copying(command, FocusWorkCategoryPO.class);
        this.save(categoryPO);
        return categoryPO.getId();
    }

    /**
     * 修改类型
     *
     * @param id
     * @param command
     *
     * @return
     */
    public void updateFocusWorkCategory(Long id, FocusWorkCategoryCommand command) {
        FocusWorkCategoryPO categoryPO = this.getById(id);
        Assert.isNotFound(categoryPO, "未找到要修改的印章");
        ObjectUtils.copying(command, categoryPO);
        this.updateById(categoryPO);
    }

    /**
     * 删除类型
     *
     * @param id
     */
    public void deleteFocusWorkCategory(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
