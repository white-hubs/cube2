package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardTypeReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardToTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator.InspectStandardValidator;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardCommandServiceImpl extends ServiceImpl<InspectStandardMapper, InspectStandardPO> implements InspectStandardCommandService {

    private final InspectStandardValidator validator;

    private final InspectStandardItemCommandService standardItemCommandService;
    private final InspectStandardTypeReferCommandService standardTypeReferCommandService;

    @Override
    public void save(InspectStandardCommandModel command) {
        // 验证
        validator.verifyIsExistByName(command.getName());

        InspectStandardPO standardPO = ObjectUtils.copying(command, InspectStandardPO.class);
        standardPO.setCode(BizCodeUtils.generateNoRandom("IS"));
        this.save(standardPO);

        standardItemCommandService.saveOrUpdate(standardPO.getId(), command.getStandardItems());
        standardTypeReferCommandService.saveOrUpdate(standardPO.getId(), command.getStandardTypeRefers());
    }

    @Override
    public void saveToType(InspectStandardToTypeCommandModel param) {
        Map<String, String> oldMap = new HashMap<>();
        List<InspectStandardTypeReferPO> oldList = standardTypeReferCommandService.lambdaQuery()
                .eq(InspectStandardTypeReferPO::getTargetCategory, param.getTargetCategory())
                .eq(InspectStandardTypeReferPO::getTargetType, param.getTargetType())
                .list();
        Optional.ofNullable(oldList).ifPresent(list -> oldList.forEach(p -> oldMap.put(
                p.getTargetCategory() + p.getTargetType() + p.getStandardId(), p.getDefaultStandard())));

        standardTypeReferCommandService.lambdaUpdate()
                .eq(InspectStandardTypeReferPO::getTargetCategory, param.getTargetCategory())
                .eq(InspectStandardTypeReferPO::getTargetType, param.getTargetType())
                .remove();

        List<Long> standardIds = param.getStandardIds();
        if (CollectionUtils.isNotEmpty(standardIds)) {
            List<InspectStandardTypeReferPO> collect = standardIds.stream().map(standardId -> InspectStandardTypeReferPO.builder()
                                                                                        .targetCategory(param.getTargetCategory())
                                                                                        .targetType(param.getTargetType())
                                                                                        .standardId(standardId)
                                                                                        .defaultStandard(
                                                                                                StringUtils.isNotBlank(oldMap.get(param.getTargetCategory() + param.getTargetType() + standardId)) ? oldMap.get(
                                                                                                        param.getTargetCategory() + param.getTargetType() + standardId)
                                                                                                                                                                                                   : String.valueOf(BigDecimal.ZERO.intValue()))
                                                                                        .build()
                                                                               ).collect(Collectors.toList());
            standardTypeReferCommandService.saveBatch(collect);
        }
    }

    @Override
    public void enabled(Long id, Boolean enable) {
        this.lambdaUpdate()
                .eq(InspectStandardPO::getId, id)
                .set(enable != null, InspectStandardPO::getEnable, enable)
                .update();
    }

    @Override
    public void update(Long id, InspectStandardCommandModel command) {
        InspectStandardPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检标准!");
        validator.verifyIsExistUpdateByName(id, command.getName());
        // 更新
        ObjectUtils.copying(command, po);
        this.updateById(po);
        standardItemCommandService.saveOrUpdate(id, command.getStandardItems());
        standardTypeReferCommandService.saveOrUpdate(id, command.getStandardTypeRefers());
    }

    @Override
    public void remove(Long id) {
        // 关联标准项不删除，对 关联的目标对象类型 逻辑删除
        standardTypeReferCommandService.removeByStandardId(id);
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
