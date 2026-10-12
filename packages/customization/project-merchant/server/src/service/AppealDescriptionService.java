package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.AppealDescriptionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AppealDescription;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;

/**
 * <p>
 * 诉求描述表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-04-28
 */
public interface AppealDescriptionService extends IService<AppealDescription> {

    int save(AppealDescriptionDTO dto, LoginUser loginUser);

    void update(AppealDescriptionDTO dto);

}
