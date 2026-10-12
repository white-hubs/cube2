package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultCaseCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultCaseCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultCaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author lzy
 * @date 2022/5/6
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultCaseCommandServiceImpl extends ServiceImpl<FaultCaseMapper, FaultCase> implements FaultCaseCommandService {

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@Nonnull FaultCaseCommand command) {
        // 验证是否存在相同案例名称
        FaultCase faultCase = this.baseMapper.selectOne(Wrappers.<FaultCase>lambdaQuery().eq(FaultCase::getName, command.getName()));
        Assert.isConflict(Objects.nonNull(faultCase), "存在相同故障案例名称!");

        // 新增故障案例信息
        faultCase = ObjectUtils.copying(command, FaultCase.class);
        this.baseMapper.insert(faultCase);

        // 新增故障案例文件
        if (!CollectionUtils.isEmpty(command.getFileIds())) {
            this.buildFiles(faultCase.getId(), command.getFileIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@Nonnull Long id, @NonNull FaultCaseCommand command) {
        // 获取故障案例信息
        FaultCase faultCase = this.baseMapper.selectById(id);
        Assert.isNotFound(faultCase, "不存在故障案例信息!");

        // 验证
        FaultCase faultCaseValidator = this.baseMapper.selectOne(Wrappers.<FaultCase>lambdaQuery()
                                                                         .eq(FaultCase::getName, command.getName()));
        Assert.isConflict(Objects.nonNull(faultCaseValidator) && !Objects.equals(id, faultCaseValidator.getId()),
                          "存在相同故障案例名称!");

        // 编辑
        ObjectUtils.copying(command, faultCase);
        this.baseMapper.updateById(faultCase);
        fileRefClient.removeByBusinessKeyAndScene(faultCase.getId(), MaintainFileSceneCodeEnum.FAULT_ORDER_CASE.getCode());
        // 新增故障案例文件
        if (!CollectionUtils.isEmpty(command.getFileIds())) {
            this.buildFiles(faultCase.getId(), command.getFileIds());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    private void buildFiles(Long id, Collection<Long> fileIds) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return;
        }
        FileRefCreateParam param = new FileRefCreateParam();
        param.setBusinessKey(id);
        param.setFileIdList(new ArrayList<>(fileIds));
        param.setSceneCode(MaintainFileSceneCodeEnum.FAULT_ORDER_CASE.getCode());
        this.fileRefClient.refInduce(param);
    }
}
