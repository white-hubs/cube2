package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFileTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileTypeServiceImpl extends ServiceImpl<EmergencyFileTypeMapper, EmergencyFileType> implements EmergencyFileTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull EmergencyFileTypeModifyParam param) {
        EmergencyFileType fileType = ObjectUtils.copying(param, EmergencyFileType.class);
        this.baseMapper.insert(fileType);
        return fileType.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull EmergencyFileTypeModifyParam param) {
        EmergencyFileType emergencyFileType = this.getById(id);
        Assert.isNotFound(emergencyFileType, "文件类型不存在或已被删除");
        emergencyFileType.setName(param.getName());
        emergencyFileType.setRemark(param.getRemark());
        this.baseMapper.updateById(emergencyFileType);
    }

    @Override
    public EmergencyFileType getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Page<EmergencyFileType> page(EmergencyFileTypeQueryParam query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query), Wrappers.<EmergencyFileType>lambdaQuery()
                                                  .like(StringUtils.isNotBlank(query.getKeyword()), EmergencyFileType::getName, query.getKeyword())
                                                  .orderByDesc(EmergencyFileType::getCreateTime)
                                         );
    }

    @Override
    public List<EmergencyFileType> list(EmergencyFileTypeQueryParam query) {
        return this.baseMapper.selectList(Wrappers.<EmergencyFileType>lambdaQuery()
                                                  .like(StringUtils.isNotBlank(query.getKeyword()), EmergencyFileType::getName, query.getKeyword())
                                                  .orderByDesc(EmergencyFileType::getCreateTime)
                                         );
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
