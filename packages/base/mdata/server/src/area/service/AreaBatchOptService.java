package com.guhecloud.cube.biz.mdata.server.area.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.util.SelectSheetWriteHandler;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.SpaceAreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaExtend;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaStatus;
import com.guhecloud.cube.biz.mdata.server.area.enumeration.VisualizationAreaTypeEnum;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaCacheInfo;
import com.guhecloud.cube.biz.mdata.server.stall.vo.BatchOptResult;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/21 10:11
 * @Description:
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaBatchOptService {

    private final static String EXCEL_EXPORT_NAME = "区域导出";
    private final static String SPACE_EXCEL_NAME = "空间";
    private final AreaService areaService;
    private final AreaStatusService areaStatusService;
    private final AreaExtendService areaExtendService;

    /**
     * 缓存存在的区域数据
     */
    public static final Map<String, AreaCacheInfo> areaByPathNameMap = new ConcurrentHashMap<>();
    public static final Map<Long, Area> areaByIdMap = new ConcurrentHashMap<>();

    /**
     * 数据导出
     */
    public void exportAreaData(AreaQueryParam queryParam, HttpServletResponse response) {
        List<AreaImportDTO> areaImportDTOS = areaService.areaDataExport(queryParam);
        Assert.isNotFound(CollectionUtils.isEmpty(areaImportDTOS), "未找到区域数据,无法导出");
        this.writeTemplate(areaImportDTOS, EXCEL_EXPORT_NAME, response);
    }

    /**
     * 写入模板
     *
     * @param response res
     *
     * @throws IOException io
     */
    public void writeTemplate(List<AreaImportDTO> list, String excelName, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下拉框内容
        String fileNameEncode = null;
        try {
            fileNameEncode = URLEncoder.encode(excelName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        List<String> areaType = Arrays.stream(VisualizationAreaTypeEnum.values())
                .map(VisualizationAreaTypeEnum::getDesc).collect(Collectors.toList());
        dropdownData.put(2, areaType);
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData, 21))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(AreaImportDTO.class)
                    .sheet(excelName)
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量导入数据
     *
     * @param file 导入文件
     *
     * @return errMsg
     */
    public BatchOptResult areaBatchImportFromTemplate(MultipartFile file) {
        List<AreaImportDTO> datas = this.readExcel(file, AreaImportDTO.class);
        this.cacheExistAreaData();
        List<String> errorList = areaService.areaDataImport(datas);
        //信息汇总
        BatchOptResult batchOptResult = new BatchOptResult();
        batchOptResult.setErrorMsg(errorList);
        batchOptResult.setSuccess(datas.size() - errorList.size());
        batchOptResult.setFail(errorList.size());
        return batchOptResult;
    }

    /**
     * 空间区域导出
     */
    public void exportSpaceAreaData(AreaQueryParam queryParam, HttpServletResponse response) {
        List<SpaceAreaImportDTO> importDTOS = areaService.getSpaceAreaDataExport(queryParam);
        Assert.isNotFound(CollectionUtils.isEmpty(importDTOS), "未找到空间数据,无法导出");
        this.spaceWriteTemplate(importDTOS, SPACE_EXCEL_NAME, response);
    }

    /**
     * 写入空间Excel数据
     *
     * @param response res
     *
     * @throws IOException io
     */
    public void spaceWriteTemplate(List<SpaceAreaImportDTO> list, String excelName, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下拉框内容
        String fileNameEncode = null;
        try {
            fileNameEncode = URLEncoder.encode(excelName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        List<String> statsuList = new ArrayList<>();
        List<AreaStatus> areaStatusList = areaStatusService.list(Wrappers.<AreaStatus>lambdaQuery()
                                                                         .select(AreaStatus::getId, AreaStatus::getName));
        if (!CollectionUtils.isEmpty(areaStatusList)) {
            areaStatusList.forEach(areaStatus -> statsuList.add(areaStatus.getName()));
        }
        dropdownData.put(1, statsuList);
        try {
            ExcelWriterBuilder builder = EasyExcelFactory.write(response.getOutputStream());
            if (!dropdownData.isEmpty()) {
                builder.registerWriteHandler(new SelectSheetWriteHandler(dropdownData, 21));
            }
            builder.excelType(ExcelTypeEnum.XLSX)
                    .head(SpaceAreaImportDTO.class)
                    .sheet(excelName)
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 空间数据导入
     *
     * @param file 导入文件
     *
     * @return errMsg
     */
    public BatchOptResult areaSpaceBatchImportFromTemplate(MultipartFile file) {
        List<SpaceAreaImportDTO> datas = this.readExcel(file, SpaceAreaImportDTO.class);
        List<AreaExtend> areaExtendList = new ArrayList<>();
        Set<String> errorSet = new HashSet<>();
        // 处理导入数据
        List<Area> areaInsertList = this.handlerExcelToAreaAndExt(datas, areaExtendList, errorSet);
        if (!areaInsertList.isEmpty()) {
            areaInsertList.forEach(areaService::save);
        }
        if (!areaExtendList.isEmpty()) {
            areaExtendService.saveBatch(areaExtendList);
        }
        //信息汇总
        BatchOptResult batchOptResult = new BatchOptResult();
        batchOptResult.setErrorMsg(new ArrayList<>(errorSet));
        batchOptResult.setSuccess(areaInsertList.size());
        batchOptResult.setFail(errorSet.size());
        return batchOptResult;
    }

    // 处理插入数据
    private List<Area> handlerExcelToAreaAndExt(List<SpaceAreaImportDTO> list, List<AreaExtend> areaExtendList, Set<String> errorSet) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        // 获取状态map Excel数据的状态名称转换id用 (状态名称, 状态id)
        Map<String, Long> areaStatusMap = new HashMap<>();
        List<AreaStatus> areaStatusList = areaStatusService.list();
        if (!CollectionUtils.isEmpty(areaStatusList)) {
            areaStatusList.forEach(areaStatus -> areaStatusMap.put(areaStatus.getName(), areaStatus.getId()));
        }
        // 已存在的code
        Map<String, Boolean> extCodeMap = new HashMap<>();
        /* 获取已有区域数据 key为全路径名称,value为id。 用来校验excel数据是否和已有数据重复*/
        Map<String, Long> parentOldNameMap = this.getOldAreaMap(extCodeMap);
        Map<String, Boolean> areaNewMap = new HashMap<>();
        // 遍历excel读取到的数据
        Map<String, Long> allNewNameMap = new HashMap<>();
        List<Area> areaList = new ArrayList<>();
        for (SpaceAreaImportDTO dto : list) {
            String areaName = dto.getAreaName();
            // 校验入参是否重复
            if (areaNewMap.containsKey(areaName)) {
                errorSet.add(dto.getAreaName() + "::重复");
                continue;
            }
            areaNewMap.put(areaName, true);
            // 校验和以前的数据是否重复
            if (parentOldNameMap.containsKey(areaName)) {
                errorSet.add(areaName + "::已存在");
                continue;
            }
            Long id = IdWorker.getId();
            Area area = new Area();
            area.setId(id);
            // 名称
            area.setName(areaName);
            // 状态
            String statusStr = dto.getStatusName();
            if (StringUtils.isNotBlank(statusStr) && areaStatusMap.containsKey(statusStr)) {
                area.setStatusId(areaStatusMap.get(statusStr));
            }
            // 面积
            String builtUpArea = dto.getBuiltUpArea();
            if (StringUtils.isNotBlank(builtUpArea) && builtUpArea.matches("-?\\d+(\\.\\d+)?")) {
                area.setBuiltUpArea(new BigDecimal(builtUpArea));
            }
            allNewNameMap.put(areaName, id);
            areaList.add(area);

            int index = areaName.lastIndexOf("/");
            if (index > 0 && index < areaName.length() - 1) {
                List<Area> parentList = new ArrayList<>();
                // 处理父级
                this.getParentAreaList(area, parentList, allNewNameMap, parentOldNameMap, errorSet);
                areaList.addAll(parentList);
            }

            // 介绍
            String introduce = dto.getIntroduce();
            if (StringUtils.isNotBlank(introduce)) {
                AreaExtend areaExtend = new AreaExtend();
                areaExtend.setId(id);
                areaExtend.setIntroduce(introduce);
                areaExtendList.add(areaExtend);
            }
        }
        // 处理名称和code
        Iterator<Area> areaIterator = areaList.iterator();
        while (areaIterator.hasNext()) {
            // 获取code
            String code = this.generateCode(0, extCodeMap);
            Area area = areaIterator.next();
            // 处理名称
            String areaName = area.getName();
            int index = areaName.lastIndexOf("/");
            if (index > 0 && index < areaName.length() - 1) {
                area.setName(areaName.substring(index + 1));
            }
            // 设置code
            if (StringUtils.isBlank(code)) {
                // 尝试再次获取
                code = this.generateCode(0, extCodeMap);
                if (StringUtils.isBlank(code)) {
                    // 还是为空就加入异常的列表
                    errorSet.add(area.getName());
                    areaIterator.remove();
                    continue;
                }
            }
            area.setCode(code);
        }
        return areaList;
    }

    /**
     * 获取区域编号信息
     *
     * @return 区域编号信息
     */
    private String generateCode(int loop, Map<String, Boolean> extCodeMap) {
        loop++;
        if (loop == 10) {
            return "";
        }
        while (true) {
            String code = "QY-" + String.format("%06d", new Random().nextInt(100000));
            if (extCodeMap.containsKey(code)) {
                generateCode(loop, extCodeMap);
            } else {
                extCodeMap.put(code, true);
                return code;
            }
        }
    }

    private void getParentAreaList(Area area, List<Area> parentList, Map<String, Long> allNewNameMap, Map<String, Long> parentOldNameMap,
                                   Set<String> errorSet
                                  ) {
        if (area == null || StringUtils.isBlank(area.getName())) {
            return;
        }
        String areaName = area.getName();
        int index = areaName.lastIndexOf("/");
        if (index > 0 && index < areaName.length() - 1) {
            String paraName = areaName.substring(0, index);
            if (parentOldNameMap.containsKey(paraName)) {
                // 父级已经在数据库存在
                errorSet.add(paraName + "::已存在");
                // 将数据库数据id复值parentId
                area.setParentId(parentOldNameMap.get(paraName));
                return;
            }

            Long parentId = allNewNameMap.get(paraName);
            if (parentId != null) {
                area.setParentId(parentId);
                return;
            }
            parentId = IdWorker.getId();
            Area parentArea = new Area();
            parentArea.setId(parentId);
            parentArea.setName(paraName);
            parentList.add(parentArea);
            allNewNameMap.put(paraName, parentId);
            area.setParentId(parentId);
            this.getParentAreaList(parentArea, parentList, allNewNameMap, parentOldNameMap, errorSet);
        }

    }

    private Map<String, Long> getOldAreaMap(Map<String, Boolean> extCodeMap) {
        /* 获取已有区域数据*/
        Map<String, Long> areaOldParentMap = new HashMap<>();
        Map<Long, Area> areaOldMap = new HashMap<>();
        List<Area> oldAreaList = areaService.list();
        if (CollectionUtils.isEmpty(oldAreaList)) {
            return areaOldParentMap;
        }
        oldAreaList.forEach(area -> {
            areaOldMap.put(area.getId(), area);
            extCodeMap.put(area.getCode(), Boolean.TRUE);
        });
        Map<Long, Area> finalAreaOldMap = areaOldMap;
        // 已经处理过的数据
        Map<Long, StringBuffer> handlerIdMap = new HashMap<>();
        oldAreaList.forEach(area -> {
            // 获取全路径名称(包含自己)
            StringBuffer sb = this.getParentName(0, new StringBuffer(), area.getId(), finalAreaOldMap, handlerIdMap);
            handlerIdMap.put(area.getId(), sb);
            areaOldParentMap.put(sb.length() > 0 ? sb.deleteCharAt(0).toString() : "", area.getId());
        });
        return areaOldParentMap;
    }

    /**
     * 拼接全路径名称
     *
     * @param loop         循环次数，避免死循环
     * @param parentName   拼接的名称
     * @param areaId       自身id
     * @param areaOldMap   数据库存在的区域map
     * @param handlerIdMap 已经处理过名称的数据
     *
     * @return 全路径名称
     */
    private StringBuffer getParentName(int loop, StringBuffer parentName, Long areaId, Map<Long, Area> areaOldMap,
                                       Map<Long, StringBuffer> handlerIdMap
                                      ) {
        if (loop > 10000) {
            return parentName;
        }
        loop++;
        if (areaId == null) {
            return parentName;
        }
        Area area = areaOldMap.get(areaId);
        if (area == null || StringUtils.isBlank(area.getName())) {
            // 如果自己都为空直接返回空
            return parentName;
        } else {
            // 先加上自己的名称
            parentName.insert(0, area.getName()).insert(0, "/");
            Area parentArea = areaOldMap.get(area.getParentId());
            // 判断父类(这里parentId为空或者父类不存在或者父类没有名称都视为没有父类)
            if (parentArea != null && StringUtils.isNotBlank(parentArea.getName())) {
                // 如果父级已经处理过直接拼接上级，然后返回
                if (handlerIdMap.containsKey(area.getParentId())) {
                    StringBuffer parentSb = handlerIdMap.get(parentArea.getId());
                    parentName.insert(0, parentSb).insert(0, "/");
                    return parentName;
                }
                // 递归查找并拼接父级名称
                this.getParentName(loop, parentName, parentArea.getId(), areaOldMap, handlerIdMap);
            }
            // 如果没有父类就返回自身
            return parentName;
        }
    }

    /**
     * 读取Excel文件
     *
     * @param file
     *
     * @return
     */
    private <T> List<T> readExcel(MultipartFile file, Class<T> clazz) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("区域导入读取excel失败{}", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        List<T> datas = reader.sheet(0).head(clazz).doReadSync();
        Assert.isParameterError(CollectionUtils.isEmpty(datas), "无法导入空的excel");
        return datas;
    }

    private void cacheExistAreaData() {
        this.clearCache();
        // 加载现有区域
        List<Area> allAreaList = areaService.lambdaQuery().list();

        //处理区域信息
        for (Area area : allAreaList) {
            List<Long> parentIds = Arrays.stream(area.getSequence().split("-")).filter(StringUtils::isNotBlank).map(Long::parseLong)
                    .collect(Collectors.toList());
            List<String> pathAreaNames = new ArrayList<>();
            parentIds.forEach(parentId -> {
                Optional<Area> optionalArea = allAreaList.stream().filter(parentArea -> parentArea.getId().equals(parentId)).findFirst();
                optionalArea.ifPresent(parentArea ->
                                               pathAreaNames.add(parentArea.getName())
                                      );
            });
            AreaCacheInfo areaInfo = new AreaCacheInfo();
            areaInfo.setId(area.getId());
            areaInfo.setCode(area.getCode());
            areaInfo.setName(area.getName());
            areaInfo.setSequence(area.getSequence());
            areaInfo.setPathId(area.getSequence().substring(0, area.getSequence().lastIndexOf("-")));
            areaInfo.setPathName(String.join("/", pathAreaNames));
            areaByIdMap.put(area.getId(), area);
            areaByPathNameMap.put(areaInfo.getPathName(), areaInfo);
        }
    }

    private void clearCache() {
        areaByIdMap.clear();
        areaByPathNameMap.clear();
    }

}
