package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionArea;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.AreaPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.AreaUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.AreaInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.AreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionAreaServiceImpl implements BusinessDisinfectionAreaService {
    private final static String ALL_PROJECT_ID = "-";
    private final DisinfectionAreaService areaService;
    private final DisinfectionTaskService taskService;
    private final DisinfectionProjectService disinfectionProjectService;

    @Override
    public List<AreaInfoVO> listAll(long projectId, String keyword) {
        List<DisinfectionArea> areaList = areaService.listAreas(projectId, keyword);
        return ObjectUtils.copying(areaList, AreaInfoVO.class);
    }

    @Transactional
    @Override
    public boolean insertArea(AreaUpsertParam param, long projectId) {
        Assert.isParameterError(ListUtils.isEmpty(param.getAreaParamList()), "添加的区域为空");
        List<DisinfectionArea> areaList = convertToArea(param, projectId);
        return areaService.saveBatch(areaList);
    }

    @Transactional
    @Override
    public boolean updateArea(AreaUpsertParam param, long projectId) {
        //如果区域为空的情况下，则代表删除所有区域
        if (ListUtils.isEmpty(param.getAreaParamList())) {
            areaService.removeByProjectId(projectId);
            return true;
        }
        List<AreaUpsertParam.AreaParam> areaParamList = param.getAreaParamList();
        //获取待删除的区域，注意需要先删除，再插入，不然会导致插入的数据丢失！！！
        List<Long> waitDeleteAreaIds = getWaitDeleteArea(param, projectId);
        if (ListUtils.nonEmpty(waitDeleteAreaIds)) {
            boolean exists = taskService.existNotCompletedTasksByAreaId(waitDeleteAreaIds);
            Assert.isNotAcceptable(exists, "区域下有未完成任务，无法删除区域！");
            areaService.remove(new LambdaQueryWrapper<DisinfectionArea>().in(DisinfectionArea::getId, waitDeleteAreaIds));
        }

        //获取要新增的区域
        List<AreaUpsertParam.AreaParam> waitInsertArea = getWaitInsertArea(areaParamList);
        List<DisinfectionArea> insertAreas = ObjectUtils.copying(waitInsertArea, DisinfectionArea.class);
        //设置项目id
        insertAreas.forEach(insertArea -> insertArea.setProjectId(projectId));
        areaService.saveBatch(insertAreas);
        //获取待修改的区域
        List<AreaUpsertParam.AreaParam> waitUpdateArea = getWaitUpdateArea(areaParamList);
        //查询存在的区域
        List<DisinfectionArea> areaList = areaService.listByAreaIds(ListUtils.convertItem(waitUpdateArea, AreaUpsertParam.AreaParam::getId));
        Map<Long, AreaUpsertParam.AreaParam> areaParamMap = MapUtils.listToMap(waitUpdateArea, AreaUpsertParam.AreaParam::getId, areaParam -> areaParam);
        areaList.forEach(area -> {
            AreaUpsertParam.AreaParam areaParam = areaParamMap.get(area.getId());
            if (Objects.isNull(areaParam)) {
                return;
            }
            ObjectUtils.copying(areaParam, area);
        });

        return areaService.updateBatchById(areaList);
    }

    private List<Long> getWaitDeleteArea(AreaUpsertParam param, long projectId) {
        //查询项目已有的区域
        List<DisinfectionArea> areas = areaService.listByProjectId(projectId);
        List<Long> existAreaIds = ListUtils.convertItem(areas, DisinfectionArea::getId);
        //获取当前保留的区域id
        List<AreaUpsertParam.AreaParam> areaParamList = param.getAreaParamList();
        List<Long> reserveAreaIds = areaParamList
                .stream()
                .map(AreaUpsertParam.AreaParam::getId)
                .filter(Objects::nonNull).collect(Collectors.toList());
        //筛选出不需要的区域Id
        return existAreaIds.stream().filter(existAreaId -> !reserveAreaIds.contains(existAreaId)).collect(Collectors.toList());
    }

    private List<AreaUpsertParam.AreaParam> getWaitUpdateArea(List<AreaUpsertParam.AreaParam> areaParamList) {
        return areaParamList.stream().filter(areaParam -> Objects.nonNull(areaParam.getId())).collect(Collectors.toList());
    }

    private List<AreaUpsertParam.AreaParam> getWaitInsertArea(List<AreaUpsertParam.AreaParam> areaParamList) {

        return areaParamList.stream().filter(areaParam -> Objects.isNull(areaParam.getId())).collect(Collectors.toList());
    }

    @Override
    public boolean deleteArea(long areaId) {
        //查询区域是否存在关联未完成任务，如果存在相关任务无法删除
        boolean exist = taskService.existNotCompletedTasksByAreaId(Collections.singletonList(areaId));
        Assert.isNotAcceptable(exist, "删除的区域存在未完成任务，无法删除！");
        return areaService.removeById(areaId);
    }

    @Override
    public Pagination<AreaPageVO> pageArea(AreaPageParam param, String projectId) {
        LambdaQueryChainWrapper<DisinfectionArea> lambdaQuery = areaService.lambdaQuery();
        if (!ALL_PROJECT_ID.equals(projectId)) {
            lambdaQuery.eq(DisinfectionArea::getProjectId, Long.parseLong(projectId));
        }
        Page<DisinfectionArea> areaPage = lambdaQuery
                .page(PagingUtils.convert(param));
        Pagination<AreaPageVO> voPagination = PagingUtils.convert(areaPage, AreaPageVO.class);
        List<AreaPageVO> records = voPagination.getRecords();
        //查询区域对应的项目信息
        Map<Long, String> projectNamesMap = disinfectionProjectService.getProjectNamesMap(ListUtils.convertItem(records, AreaPageVO::getProjectId));
        //设置项目名称
        records.forEach(vo -> {
            vo.setProjectName(projectNamesMap.getOrDefault(vo.getProjectId(), ""));
        });
        return voPagination;
    }

    @Override
    public boolean singleUpdateArea(AreaUpsertParam.AreaParam param, Long projectId, Long id) {
        Assert.isParameterError(Objects.isNull(param) || Objects.isNull(projectId), "请求参数有误");
        param.setId(id);
        DisinfectionArea area = areaService.getById(id);
        Assert.parameterIsNull(area, "不存在区域信息");
        ObjectUtils.copying(param, area);
        return areaService.updateById(area);
    }

    private List<DisinfectionArea> convertToArea(AreaUpsertParam param, long projectId) {
        List<AreaUpsertParam.AreaParam> areaParamList = param.getAreaParamList();
        if (ListUtils.isEmpty(areaParamList)) {
            return Collections.emptyList();
        }
        return ListUtils.convertItem(areaParamList, areaParam -> {
            DisinfectionArea disinfectionArea = new DisinfectionArea();
            disinfectionArea.setProjectId(projectId);
            disinfectionArea.setAreaName(areaParam.getAreaName());
            disinfectionArea.setAreaDetail(areaParam.getAreaDetail());
            return disinfectionArea;
        });
    }
}
