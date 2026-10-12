package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.AppealDescriptionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AppealDescription;
import com.guhecloud.cube.deliver.project.merchant.mapper.AppealDescriptionMapper;
import com.guhecloud.cube.deliver.project.merchant.service.AppealDescriptionService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 诉求描述表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-04-28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppealDescriptionServiceImpl extends ServiceImpl<AppealDescriptionMapper, AppealDescription> implements AppealDescriptionService {

    private final AppealDescriptionMapper mapper;

    @Override
    public int save(AppealDescriptionDTO dto, LoginUser loginUser) {
        AppealDescription appealDescription = dto.convert();
        appealDescription.setCreateBy(loginUser.getUsername());
        appealDescription.setCode(BizCodeUtils.generate("SQMS"));
        return mapper.insert(appealDescription);
    }

    @Override
    public void update(AppealDescriptionDTO dto) {
        mapper.updateById(dto.convert());
    }
}
