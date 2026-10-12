package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionDataType;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DataTypePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.DataTypeUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionMaterialService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DataTypeVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.DataTypePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
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
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionDataTypeServiceImpl implements BusinessDisinfectionDataTypeService {

    private final DisinfectionDataTypeService dataTypeService;
    private final DisinfectionMaterialService materialService;
    private final DisinfectionKeyPlaceService keyPlaceService;
    private final DisinfectionProjectService projectService;
    private final DisinfectionTaskService taskService;
    private final DisinfectionFileHelper disinfectionFileHelper;

    @Override
    public boolean insertDataType(DataTypeUpsertParam dataTypeUpsertParam) {
        //校验数据
        validateDataType(dataTypeUpsertParam);
        DisinfectionDataType disinfectionDataType = ObjectUtils.copying(dataTypeUpsertParam, DisinfectionDataType.class);
        this.induceFile(dataTypeUpsertParam.getIcon());
        return dataTypeService.save(disinfectionDataType);
    }

    private void validateDataType(DataTypeUpsertParam param) {
        Assert.parameterIsNull(param, "校验的数据不能为空！");
        String dataName = param.getDataName();
        List<DisinfectionDataType> disinfectionDataTypeList = dataTypeService.listInNames(Collections.singletonList(dataName), param.getDataType());
        Assert.isConflict(CollectionUtils.isNotEmpty(disinfectionDataTypeList), "类型名称已存在,请勿重复添加！");
    }

    @Override
    public boolean deleteDataType(long id) {
        return dataTypeService.removeById(id);
    }

    @Transactional
    @Override
    public boolean updateDataType(long id, DataTypeUpsertParam dataTypeUpsertParam) {

        //查询 数据
        DisinfectionDataType disinfectionDataType = dataTypeService.getById(id);
        Assert.parameterIsNull(disinfectionDataType, String.format("id:%s,删除的数据不存在，请联系管理员检查数据！", id));
        String dataName = disinfectionDataType.getDataName();
        if (!dataName.equals(dataTypeUpsertParam.getDataName())) {
            //如果修改有名称的修改，需要校验是否存在该名称
            validateDataType(dataTypeUpsertParam);
        }
        ObjectUtils.copying(dataTypeUpsertParam, disinfectionDataType);
        this.induceFile(dataTypeUpsertParam.getIcon());
        boolean updateDataType = dataTypeService.updateById(disinfectionDataType);
        updateRelationData(id, dataTypeUpsertParam, dataTypeUpsertParam.getDataType());
        return updateDataType;
    }

    private void induceFile(Long icon) {
        if (icon == null) {
            return;
        }
        disinfectionFileHelper.induceFile(DinsinfectionFileInduceSceneCodeEnum.DATA_TYPE, icon);
    }

    private void updateRelationData(long id, DataTypeUpsertParam typeUpsertParam, DisinfectionDataTypeEnum dataTypeEnum) {
        switch (dataTypeEnum) {
            case FILE:
                materialService.updateFileTypeName(id, typeUpsertParam.getDataName());
                log.info("修改关联数据，类型id:{},更新文件类型名称：{}", id, typeUpsertParam.getDataName());
                return;
            case PLACE:
                keyPlaceService.updatePlaceTypeName(id, typeUpsertParam.getDataName());
                log.info("修改关联数据，类型id:{},更新场所类型名称：{}", id, typeUpsertParam.getDataName());
                return;
            case TASK:
                taskService.updateTaskTypeName(id, typeUpsertParam.getDataName());
                log.info("修改关联数据，类型id:{},更新任务类型名称：{}", id, typeUpsertParam.getDataName());
                return;
            case PROJECT:
                projectService.updateProjectTypeName(id, typeUpsertParam.getDataName());
                log.info("修改关联数据，类型id:{},更新项目类型名称：{}", id, typeUpsertParam.getDataName());
                return;
            default:
        }
    }

    @Override
    public Pagination<DataTypePageVO> list(DataTypePageParam dataTypePageParam) {
        Page<DisinfectionDataType> page = new Page<>();
        page.setCurrent(dataTypePageParam.getPageNumber());
        page.setSize(dataTypePageParam.getPageSize());
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("create_time");
        orderItem.setAsc(false);
        page.setOrders(Collections.singletonList(orderItem));
        Page<DisinfectionDataType> dataTypePage = dataTypeService.lambdaQuery()
                .eq(DisinfectionDataType::getDataType, dataTypePageParam.getDataType())
                .like(StringUtils.isNotBlank(dataTypePageParam.getKeyword()), DisinfectionDataType::getDataName, dataTypePageParam.getKeyword())
                .page(page);
        if (Objects.isNull(dataTypePage) || CollectionUtils.isEmpty(dataTypePage.getRecords())) {
            return new Pagination<>();
        }
        Map<Long, FileVO> fileVOMap = dataTypeService.getFileVOMap(dataTypePage.getRecords());
        List<DataTypePageVO> typeVOList = dataTypePage.getRecords().
                stream().map(t -> {
                    DataTypePageVO dataTypePageVO = ObjectUtils.copying(t, DataTypePageVO.class);
                    dataTypePageVO.setIconFile(fileVOMap.get(t.getIcon()));
                    return dataTypePageVO;
                }).collect(Collectors.toList());
        return PagingUtils.convert(dataTypePage, typeVOList);
    }

    @Override
    public List<DataTypeVO> listAll(DisinfectionDataTypeEnum disinfectionDataTypeEnum, Long projectId) {
        List<DisinfectionDataType> dataTypeList = dataTypeService
                .lambdaQuery()
                .eq(DisinfectionDataType::getDataType, disinfectionDataTypeEnum)
                .list();
        if (ListUtils.isEmpty(dataTypeList)) {
            return Collections.emptyList();
        }
        if (DisinfectionDataTypeEnum.FILE.equals(disinfectionDataTypeEnum)) {
            return fileListInfo(dataTypeList, projectId);
        }
        return placeListInfo(dataTypeList, projectId);
    }

    private List<DataTypeVO> placeListInfo(@NonNull List<DisinfectionDataType> dataTypeList, Long projectId) {
        List<Long> dataTypeIds = ListUtils.convertItem(dataTypeList, DisinfectionDataType::getId);
        Map<Long, Long> countMap = Objects.isNull(projectId) ? keyPlaceService.getKeyPlaceCount(dataTypeIds)
                                                             : keyPlaceService.getProjectKeyPlaceCount(dataTypeIds, projectId);
        List<DataTypeVO> voList = ObjectUtils.copying(dataTypeList, DataTypeVO.class);
        //设置各个分类的数量
        voList.forEach(vo -> vo.setCount(countMap.getOrDefault(vo.getId(), 0L)));
        return voList;
    }

    private List<DataTypeVO> fileListInfo(@NonNull List<DisinfectionDataType> dataTypeList, Long projectId) {
        List<Long> dataTypeIds = ListUtils.convertItem(dataTypeList, DisinfectionDataType::getId);
        Map<Long, Long> countMap = Objects.isNull(projectId) ? materialService.getFileCount(dataTypeIds)
                                                             : materialService.getProjectFileCount(dataTypeIds, projectId);
        List<DataTypeVO> voList = ObjectUtils.copying(dataTypeList, DataTypeVO.class);
        //设置各个文件分类的文件数量
        voList.forEach(vo -> vo.setCount(countMap.getOrDefault(vo.getId(), 0L)));
        return voList;
    }
}
