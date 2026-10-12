package com.guhecloud.cube.base.visualization.server.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.util.SelectSheetWriteHandler;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.visualization.server.dao.ModelSceneMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelScene;
import com.guhecloud.cube.base.visualization.server.enumeration.VisualizationFileScene;
import com.guhecloud.cube.base.visualization.server.param.ModelSceneParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.ModelSceneService;
import com.guhecloud.cube.base.visualization.server.vo.ModelSceneRelateVO;
import com.guhecloud.cube.base.visualization.server.vo.ModelSceneVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.SceneTypeClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SceneTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.SceneTypeQueryParam;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 15:16
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelSceneServiceImpl extends ServiceImpl<ModelSceneMapper, ModelScene> implements ModelSceneService {
    private final AreaClient areaClient;
    private final SceneTypeClient sceneTypeClient;
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;

    @Override
    public List<ModelSceneVO> queryModelSceneByProjectId(VisualizationBaseQueryParam param) {
        List<ModelScene> modelSceneList = this.lambdaQuery()
                .eq(ModelScene::getProjectId, param.getProjectId())
                .like(Objects.nonNull(param.getAreaId()), ModelScene::getAreaId, param.getAreaId())
                .like(StringUtils.isNotBlank(param.getKeyword()), ModelScene::getName, param.getKeyword())
                .list();
        if (CollectionUtils.isEmpty(modelSceneList)) {
            return new ArrayList<>();
        }
        Set<Long> areaIds = new HashSet<>();
        Set<Long> sceneTypeIds = new HashSet<>();
        Set<Long> fileIds = new HashSet<>();
        for (ModelScene modelScene : modelSceneList) {
            if (Objects.nonNull(modelScene.getAreaId())) {
                areaIds.add(modelScene.getAreaId());
            }
            if (Objects.nonNull(modelScene.getSceneTypeId())) {
                sceneTypeIds.add(modelScene.getSceneTypeId());
            }
            if (Objects.nonNull(modelScene.getModelFile())) {
                fileIds.add(modelScene.getModelFile());
            }
            if (Objects.nonNull(modelScene.getSceneImg())) {
                fileIds.add(modelScene.getSceneImg());
            }
        }
        Map<Long, String> areaMap;
        if (CollectionUtils.isNotEmpty(areaIds)) {
            areaMap = Optional.ofNullable(areaClient.getByIds(areaIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        } else {
            areaMap = new HashMap<>();
        }
        Map<Long, String> sceneMap;
        if (CollectionUtils.isNotEmpty(sceneTypeIds)) {
            SceneTypeQueryParam sceneTypeQueryParam = new SceneTypeQueryParam();
            sceneTypeQueryParam.setIds(new ArrayList<>(sceneTypeIds));
            sceneMap = Optional.ofNullable(sceneTypeClient.listSceneTypes(sceneTypeQueryParam))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(SceneTypeDTO::getId, SceneTypeDTO::getName));
        } else {
            sceneMap = new HashMap<>();
        }
        Map<Long, FileVO> fileVOMap;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileVOMap = Optional.ofNullable(fileClient.getByIds(new ArrayList<>(fileIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));

        } else {
            fileVOMap = new HashMap<>();
        }

        return modelSceneList.stream().map(t -> ModelSceneVO.builder()
                                                   .id(t.getId())
                                                   .projectId(t.getProjectId())
                                                   .name(t.getName())
                                                   .areaId(t.getAreaId())
                                                   .areaName(areaMap.get(t.getAreaId()))
                                                   .modelStructure(t.getModelStructure())
                                                   .modelUrl(t.getModelUrl())
                                                   .sceneTypeId(t.getSceneTypeId())
                                                   .sceneTypeName(sceneMap.get(t.getSceneTypeId()))
                                                   .modelFile(fileVOMap.get(t.getModelFile()))
                                                   .sceneUrl(t.getSceneUrl())
                                                   .sceneImg(fileVOMap.get(t.getSceneImg()))
                                                   .build()
                                          ).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateModelScene(ModelSceneParam modelSceneParam) {
        this.checkParam(modelSceneParam);

        //文件持久化
        List<Long> fileIds = new ArrayList<>();
        if (Objects.nonNull(modelSceneParam.getModelFile())) {
            fileIds.add(modelSceneParam.getModelFile());
        }
        if (Objects.nonNull(modelSceneParam.getSceneImg())) {
            fileIds.add(modelSceneParam.getSceneImg());
        }
        if (CollectionUtils.isNotEmpty(fileIds)) {
            FileInduceParam param = new FileInduceParam();
            param.setFileIdList(fileIds);
            param.setBusinessKey(modelSceneParam.getProjectId());
            param.setSceneCode(VisualizationFileScene.VISUALIZATION_MODEL_FILE.getCode());
            fileOperateClient.fileInduceSingleBody(param);
        }
        if (Objects.nonNull(modelSceneParam.getId())) {
            ModelScene modelScene = this.getById(modelSceneParam.getId());
            Assert.isNotFound(Objects.isNull(modelScene), "模型场景配置不存在");
            convertModelScene(modelSceneParam, modelScene);
            return this.updateById(modelScene);
        } else {
            ModelScene modelScene = convertModelScene(modelSceneParam, null);
            modelScene.setId(IdWorker.getId());
            return this.save(modelScene);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoRelateAreaAndModel(MultipartFile file, ModelSceneParam param) {
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("visualization:可视化模型场景关联失败{}", e);
            throw new ServiceException("文件读取失败");
        }

        List<AreaDTO> areaList = areaClient.listAreas();
        Map<String, Long> areaMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(areaList)) {
            for (AreaDTO area : areaList) {
                List<Long> parentIds = Arrays.stream(area.getSequence().split("-")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> pathAreaNames = new ArrayList<>();
                parentIds.forEach(parentId -> {
                    Optional<AreaDTO> optionalArea = areaList.stream()
                            .filter(parentArea -> parentArea.getId().equals(parentId)).findFirst();
                    optionalArea.ifPresent(parentArea ->
                                                   pathAreaNames.add(parentArea.getName())
                                          );
                });
                area.setName(String.join("-", pathAreaNames));
                areaMap.put(area.getName(), area.getId());
            }
        }
        // 读取第一个sheet的数据
        List<ModelSceneRelateVO> data = reader.sheet(0).head(ModelSceneRelateVO.class).doReadSync();
        Assert.isParameterError(CollectionUtils.isEmpty(data), "导入数据不能为空");
        Map<Long, ModelScene> modelSceneMap = Optional.ofNullable(this.lambdaQuery()
                                                                          .eq(ModelScene::getProjectId, param.getProjectId())
                                                                          .list()).orElse(new ArrayList<>())
                .stream()
                .filter(t -> Objects.nonNull(t.getAreaId()))
                .collect(Collectors.toMap(ModelScene::getAreaId, Function.identity()));
        List<ModelScene> insertList = new ArrayList<>();
        List<ModelScene> updateList = new ArrayList<>();

        for (ModelSceneRelateVO datum : data) {
            if (!areaMap.containsKey(datum.getAreaName())) {
                continue;
            }
            Long areaId = areaMap.get(datum.getAreaName());
            ModelScene modelScene = modelSceneMap.get(areaId);
            if (Objects.nonNull(modelScene)) {
                modelScene.setModelStructure(datum.getModelStructure());
                updateList.add(modelScene);
            } else {
                ModelScene newModeScene = new ModelScene();
                newModeScene.setModelUrl(param.getModelUrl());
                newModeScene.setAreaId(areaId);
                newModeScene.setModelStructure(datum.getModelStructure());
                newModeScene.setProjectId(param.getProjectId());
                insertList.add(newModeScene);
            }

        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
        }

        if (CollectionUtils.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
    }

    @Override
    public void autoRelateTemplateWrite(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下拉框内容
        String fileNameEncode;
        try {
            fileNameEncode = URLEncoder.encode("模型区域关联模板", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        // 区域列表
        List<AreaDTO> areaList = areaClient.listAreas();
        if (ObjectUtils.isNotEmpty(areaList)) {
            for (AreaDTO area : areaList) {
                List<Long> parentIds = Arrays.stream(area.getSequence().split("-")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> pathAreaNames = new ArrayList<>();
                parentIds.forEach(parentId -> {
                    Optional<AreaDTO> optionalArea = areaList.stream()
                            .filter(parentArea -> parentArea.getId().equals(parentId)).findFirst();
                    optionalArea.ifPresent(parentArea ->
                                                   pathAreaNames.add(parentArea.getName())
                                          );
                });
                area.setName(String.join("-", pathAreaNames));
            }
            List<String> areaNameList = areaList.stream().map(AreaDTO::getName).collect(Collectors.toList());
            dropdownData.put(0, areaNameList);
        }
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData, 21))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(ModelSceneRelateVO.class)
                    .sheet()
                    .doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 参数校验
     *
     * @param modelSceneParam
     */
    private void checkParam(ModelSceneParam modelSceneParam) {
        Assert.isParameterError(Objects.isNull(modelSceneParam.getProjectId()), "项目id不能为空");
    }

    private ModelScene convertModelScene(ModelSceneParam modelSceneParam, ModelScene modelScene) {
        modelScene = Objects.isNull(modelScene) ? new ModelScene() : modelScene;
        modelScene.setProjectId(modelSceneParam.getProjectId());
        modelScene.setName(modelSceneParam.getName());
        modelScene.setAreaId(modelSceneParam.getAreaId());
        modelScene.setModelStructure(modelSceneParam.getModelStructure());
        modelScene.setModelUrl(modelSceneParam.getModelUrl());
        modelScene.setSceneTypeId(modelSceneParam.getSceneTypeId());
        modelScene.setModelFile(modelSceneParam.getModelFile());
        modelScene.setSceneUrl(modelSceneParam.getSceneUrl());
        modelScene.setSceneImg(modelSceneParam.getSceneImg());

        return modelScene;
    }

}
