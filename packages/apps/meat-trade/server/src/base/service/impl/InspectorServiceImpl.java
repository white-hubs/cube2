package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.InspectorMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectorServiceImpl extends ServiceImpl<InspectorMapper, Inspector> implements InspectorService {

    private final SpotCheckService spotCheckService;

    @Override
    @LogRecord(success = "新增抽检员", fail = "新增屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "INSPECTOR_CONFIG", subTypeName = "抽检员设置",
               operationType = OperationType.ADD,
               extra = "{{#param}}",
               bizNo = ""
    )
    public void save(@NonNull InspectorCreateParam param) {
        Assert.isConflict(Objects.nonNull(this.getByName(param.getName())), "存在相同名称抽检员");
        Inspector inspector = ObjectUtils.copying(param, Inspector.class);
        inspector.setAddTime(LocalDateTime.now());
        this.save(inspector);
    }

    @Override
    @LogRecord(success = "编辑抽检员", fail = "编辑屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "INSPECTOR_CONFIG", subTypeName = "抽检员设置",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = "{{#param.id}}"
    )
    public void update(@NonNull InspectorUpdateParam param) {
        Inspector inspector = this.getById(param.getId());
        Assert.isNotFound(inspector, "不存在抽检员名称");
        Inspector inspectorByName = this.getByName(param.getName());
        Assert.isConflict(Objects.nonNull(inspectorByName) && !inspectorByName.getId().equals(param.getId()), "存在相同名字抽检员");
        ObjectUtils.copying(param, inspector);
        this.updateById(inspector);
    }

    @Override
    public Page<Inspector> page(@NonNull InspectorQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Inspector::getName, queryParam.getKeyword())
                    )
                .orderByDesc(BasicEntity::getCreateTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    @LogRecord(success = "删除抽检员", fail = "删除屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "INSPECTOR_CONFIG", subTypeName = "抽检员设置",
               operationType = OperationType.DELETE,
               bizNo = "{{#id}}"
    )
    public void remove(@NonNull Long id) {
        List<SpotCheck> spotChecks = this.spotCheckService.listByInspectors(id, SessionContextHolder.getRequester().getTenant());
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(spotChecks), "食安抽检信息存在该抽检员,无法删除!");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Inspector getByName(@NonNull String name) {
        return this.lambdaQuery().eq(Inspector::getName, name).one();
    }
}
