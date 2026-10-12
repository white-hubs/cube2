package com.guhecloud.cube.srv.gen.maintain.server.fault.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 故障类型验证器
 *
 * @author lzy
 * @date 2022/5/3
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultTypeValidator {

    private final FaultTypeMapper faultTypeMapper;

    /**
     * 验证故障类型名称是否相同
     *
     * @param name 名称
     */
    public void verifyIsExistFaultTypeName(String name) {
        FaultType faultType = this.faultTypeMapper.selectOne(Wrappers.<FaultType>lambdaQuery().eq(FaultType::getName, name));
        Assert.isConflict(faultType != null, "存在相同故障类型名称!");
    }

    /**
     * 验证故障类型编辑名称是否相同
     *
     * @param id   故障类型主键
     * @param name 名称
     */
    public void verifyIsExistUpdateFaultTypeByName(Long id, String name) {
        FaultType faultType = this.faultTypeMapper.selectOne(Wrappers.<FaultType>lambdaQuery().eq(FaultType::getName, name));
        Assert.isConflict(faultType != null && !Objects.equals(id, faultType.getId()),
                          "存在相同故障类型名称!");
    }
}
