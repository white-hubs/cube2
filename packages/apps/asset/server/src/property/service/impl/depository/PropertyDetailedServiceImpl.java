package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.common.util.DateLocalUtil;
import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotVO;
import com.guhecloud.cube.property.management.client.property.enumeration.MessageBusinessTypeEnum;
import com.guhecloud.cube.property.management.client.property.param.property.OveragePageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedAllParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedDuplicatesParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedWorkflowParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAll;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAllPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedAllExcelVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTotalVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyStatisticsVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.CommonConstant;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.FileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly.AssemblyClassifyMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.AssemblyClassify;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.DepositoryDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTaskRelation;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.AssemblyClassifyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.CheckTaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.MessageModuleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertySourceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.param.PropertyDetailedExcelTemplateParam;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.AssemblyClassifyService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.assembly.ClassifyServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.MessageUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资产明细;(property_detailed)表服务实现类
 *
 * @author :lijie
 * @date : 2023-3-8
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyDetailedServiceImpl extends ServiceImpl<PropertyDetailedMapper, PropertyDetailed> implements PropertyDetailedService {
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final UserClientService userClientService;
    private final FileClient fileClient;
    private final AssemblyClassifyService assemblyClassifyService;
    private final AssemblyClassifyMapper assemblyClassifyMapper;
    private final PropertyCheckTaskRelationMapper checkTaskRelationMapper;
    private final PropertyCheckTaskMapper checkTaskMapper;
    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final UserClient userClient;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final MessageUtil messageUtil;
    private final ClassifyServiceImpl classifyService;
    private final FileOperateClient fileOperateClient;

    @Override
    public PropertyDetailedVO queryById(Long id) {
        PropertyDetailed detailed = this.baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(detailed)) {
            throw new ServiceException("资产信息不存在");
        }
        PropertyDetailedVO vo = ConvertUtils.convert(PropertyDetailedVO.class, detailed);
        vo.setReceiveUserName(userClientService.getUser(vo.getReceiveUserId()));
        vo.setAdminName(userClientService.getUser(detailed.getAdminId()));
        if (ObjectUtils.isNotEmpty(detailed.getAreaId())) {
            List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(detailed.getAreaId(), Long::valueOf));
            vo.setArea(area);
        }
        if (ObjectUtils.isNotEmpty(detailed.getTypeId())) {
            AssemblyClassifyVO treeVO = assemblyClassifyService.queryById(detailed.getTypeId());
            vo.setTypeName(Optional.ofNullable(treeVO).orElse(new AssemblyClassifyVO()).getName());
        }
        // 附件图片
        if (ObjectUtils.isNotEmpty(detailed.getFile())) {
            List<Long> fileIds = Arrays.stream(detailed.getFile().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            List<FileVO> file = fileClient.getByIds(fileIds);
            vo.setFile(ObjectUtils.isNotEmpty(file) ? file : new ArrayList<>());
        }
        if (ObjectUtils.isNotEmpty(detailed.getImgs())) {
            List<Long> fileIds = Arrays.stream(detailed.getImgs().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            List<FileVO> file = fileClient.getByIds(fileIds);
            vo.setImgs(ObjectUtils.isNotEmpty(file) ? file : new ArrayList<>());
        }
        if (ObjectUtils.isNotEmpty(vo.getSourceId())) {
            vo.setSourceName(Optional.ofNullable(assemblyClassifyMapper.selectById(vo.getSourceId())).orElse(new AssemblyClassify())
                                     .getName());
        }
        return vo;
    }

    @Override
    public PropertyDetailedVO queryByBimCode(String bimCode) {
        PropertyDetailed detailed = this.baseMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                                      .eq(PropertyDetailed::getDeviceNumber, bimCode));
        if (ObjectUtils.isEmpty(detailed)) {
            throw new ServiceException("资产信息不存在");
        }
        return this.queryById(detailed.getId());
    }

    @Override
    public Boolean getDuplicates(List<PropertyDetailedDuplicatesParam> params) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("第");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < params.size(); i++) {
            if (ObjectUtils.isNotEmpty(map.get(params.get(i).getSerialNumber()))) {
                int x = i + 1;
                buffer.append(x);
                buffer.append("行编码重复");
                throw new ServiceException(buffer.toString());
            }
            map.put(params.get(i).getSerialNumber(), "编码");
            PropertyDetailed detailed = baseMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                                     .eq(PropertyDetailed::getSerialNumber, params.get(i).getSerialNumber())
                                                                     .last("limit 1"));
            if (ObjectUtils.isNotEmpty(detailed)) {
                int x = i + 1;
                buffer.append(x);
                buffer.append("行编码重复");
                throw new ServiceException(buffer.toString());
            }
        }
        return true;
    }

    @Override
    public PropertyDetailedVO queryByTaskId(Long id, String taskId) {
        PropertyCheckTaskRelation taskRelation = checkTaskRelationMapper.selectOne(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                                           .eq(PropertyCheckTaskRelation::getPropertyId, id)
                                                                                           .eq(PropertyCheckTaskRelation::getTaskId, taskId));
        Assert.isNotFound(ObjectUtils.isEmpty(taskRelation), "无效扫码");
        Assert.isConflict(!CheckTaskStatusEnum.INCOMPLETE.name().equals(taskRelation.getCheckStatus()), "该资产已被盘点");

        return this.queryById(id);
    }

    @Override
    public List<PropertyDetailIdTypeDTO> getPropertyDetailedIdTypes(PropertyDetailedPageParam param) {
        var wrapper = new LambdaQueryWrapper<PropertyDetailed>();
        wrapper.select(PropertyDetailed::getId, PropertyDetailed::getName, PropertyDetailed::getTypeId)
                .like(StringUtils.isNotBlank(param.getKeyword()), PropertyDetailed::getName, param.getKeyword())
                .in(!CollectionUtils.isEmpty(param.getIds()), PropertyDetailed::getId, param.getIds())
                .eq(ObjectUtils.isNotNull(param.getDeptId()), PropertyDetailed::getMaintenanceDepId, param.getDeptId());
        var list = this.baseMapper.selectList(wrapper);
        return list.stream().map(po -> new PropertyDetailIdTypeDTO(po.getId(), po.getName(), po.getTypeId(), null))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<PropertyDetailedPageVO> getPropertyDetailedPage(PropertyDetailedPageParam param) {
        Page<PropertyDetailedPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<String> statusList = PropertyStatusEnum.getStatusByBusinessType(param.getBusinessType(), param.getStatus());
        if (ObjectUtils.isNotEmpty(param.getAreaIds())) {
            param.setAreaId(String.join(",", param.getAreaIds()));
        }
        if (ObjectUtils.isNotEmpty(param.getTypeId())) {
            param.setTypeIds(Arrays.stream(param.getTypeId().split(","))
                                     .map(s -> Long.parseLong(s.trim()))
                                     .collect(Collectors.toList()));
        }
        page = this.baseMapper.getPropertyDetailedPage(page, param, statusList);
        this.getPropertyDetailedData(page.getRecords(), null);
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public void getPropertyDetailedExcel(HttpServletRequest request, HttpServletResponse response, PropertyDetailedExcelParam param) {
        List<String> statusList = PropertyStatusEnum.getStatusByBusinessType(param.getBusinessType(), param.getStatus());
        if (ObjectUtils.isNotEmpty(param.getAreaIds())) {
            param.setAreaId(String.join(",", param.getAreaIds()));
        }
        List<PropertyDetailedPageVO> data = this.baseMapper.getPropertyDetailedAll(param, statusList);
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无资产数据,无法导出");
        this.getPropertyDetailedData(data, "excel");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("serialNumber", "资产编号");
        writer.addHeaderAlias("financeNumber", "财务编号");
        writer.addHeaderAlias("name", "资产名称");
        writer.addHeaderAlias("typeName", "资产类别");
        writer.addHeaderAlias("norms", "规格型号");
        writer.addHeaderAlias("unit", "计量单位");
        writer.addHeaderAlias("price", "属性值");
        writer.addHeaderAlias("sourceName", "来源");
        writer.addHeaderAlias("purchaseDate", "购入日期");
        writer.addHeaderAlias("startTime", "入库日期");
        writer.addHeaderAlias("maintenanceDate", "维保到期时间");
        writer.addHeaderAlias("maintenanceTime", "剩余维保时间（月）");
        writer.addHeaderAlias("usageDate", "资产逾龄时间");
        writer.addHeaderAlias("usageTime", "剩余使用期限");
        writer.addHeaderAlias("statusName", "资产状态");
        writer.addHeaderAlias("adminName", "管理员");
        writer.addHeaderAlias("areaName", "区域");
        writer.addHeaderAlias("receiveDepName", "使用部门");
        writer.addHeaderAlias("maintenanceDepName", "维护部门");
        writer.addHeaderAlias("createByName", "创建人");
        writer.addHeaderAlias("createTime", "创建时间");
        List<PropertyDetailedAllExcelVO> detailedAll = ConvertUtils.toList(PropertyDetailedAllExcelVO.class, data);
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(detailedAll, true);
        writer.setColumnWidth(-1, 25);
        com.guhecloud.platform.cloud.util.ExcelUtils.downloadExcel(request, response, "资产清单", writer);
    }

    @Override
    public List<SafetyManageCollectPropertyVO> getPropertyDetailedAll(PropertyDetailedAllParam propertyDetailed) {
        Map<String, List<String>> typeMap = getAllPropertyType();
        List<String> status = PropertyStatusEnum.getStatusByBusinessType(null, null);
        List<SafetyManageCollectPropertyVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(typeMap)) {
            typeMap.entrySet().forEach(entry -> {
                if (ObjectUtils.isNotEmpty(entry.getValue())) {
                    getPropertyVos(propertyDetailed, vos, entry, status);
                }
            });
        }
        return vos;
    }

    private void getPropertyVos(PropertyDetailedAllParam propertyDetailed, List<SafetyManageCollectPropertyVO> vos,
                                Map.Entry<String, List<String>> entry, List<String> status
                               ) {
        List<PropertyDetailed> list = baseMapper.getSafetyManagementData(propertyDetailed.getPeriod(), entry.getValue(), status);
        if (ObjectUtils.isNotEmpty(list)) {
            Set<Long> userIdSet = new HashSet<>();
            userIdSet.addAll(list.stream().map(PropertyDetailed::getAdminId).collect(Collectors.toSet()));
            userIdSet.addAll(list.stream().map(PropertyDetailed::getReceiveUserId).collect(Collectors.toSet()));
            Map<Long, String> userMap = getUserNameMap(userIdSet);
            Set<String> set = list.stream().map(PropertyDetailed::getAreaId).collect(Collectors.toSet());
            Map<Long, String> areaMap = getAreaNameMap(set);
            list.parallelStream().forEach(po -> {
                vos.add(SafetyManageCollectPropertyVO.builder()
                                .id(po.getId())
                                .propertyName(po.getName())
                                .propertyType(entry.getKey())
                                .putInTime(po.getPurchaseDate())
                                .expirationTime(po.getMaintenanceDate())
                                .status(po.getStatus())
                                .managerId(po.getAdminId())
                                .managerName(userMap.get(po.getAdminId()))
                                .areaId(po.getAreaId())
                                .areaName(getAreaName(po.getAreaId(), areaMap))
                                .userId(po.getReceiveUserId())
                                .username(userMap.get(po.getReceiveUserId()))
                                .tenant(po.getTenant())
                                .deleted(po.getDeleted())
                                .createBy(po.getCreateBy())
                                .createTime(po.getCreateTime())
                                .updateBy(po.getUpdateBy())
                                .updateTime(po.getUpdateTime())
                                .build());
            });
        }
    }

    /**
     * 查询所有需要被采集的资产类型
     *
     * @return 资产类型名称列表
     */
    public Map<String, List<String>> getAllPropertyType() {
        Map<String, List<String>> maps = new HashMap<>();
        List<AssemblyClassify> flammableEquipment = classifyService.getAssemblyClassify(CommonConstant.FLAMMABLE_EQUIPMENT,
                                                                                        AssemblyClassifyTypeEnum.ASSET_TYPE.getCode(), null);
        maps.put(CommonConstant.FLAMMABLE_EQUIPMENT, flammableEquipment.stream().map(AssemblyClassify::getName)
                .collect(Collectors.toList()));
        List<AssemblyClassify> electroMobile = classifyService.getAssemblyClassify(CommonConstant.ELECTRO_MOBILE,
                                                                                   AssemblyClassifyTypeEnum.ASSET_TYPE.getCode(), null);
        maps.put(CommonConstant.ELECTRO_MOBILE, electroMobile.stream().map(AssemblyClassify::getName).collect(Collectors.toList()));
        List<AssemblyClassify> firefightingEquipment = classifyService.getAssemblyClassify(CommonConstant.FIREFIGHTING_EQUIPMENT,
                                                                                           AssemblyClassifyTypeEnum.ASSET_TYPE.getCode(), null);
        maps.put(CommonConstant.FIREFIGHTING_EQUIPMENT, firefightingEquipment.stream().map(AssemblyClassify::getName)
                .collect(Collectors.toList()));
        return maps;
    }

    @Override
    public Pagination<PropertyDetailedPageVO> getRiskSourceDetailedPage(PropertyDetailedPageParam param) {
        Page<PropertyDetailedPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<String> statusList = PropertyStatusEnum.getStatusByBusinessType(param.getBusinessType(), param.getStatus());
        if (ObjectUtils.isNotEmpty(param.getAreaIds())) {
            param.setAreaId(String.join(",", param.getAreaIds()));
        }
        param.setTypeIds(getSubTypeIds(param.getTypeId()));
        page = this.baseMapper.getPropertyDetailedPage(page, param, statusList);
        this.getPropertyDetailedData(page.getRecords(), null);
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    private List<Long> getSubTypeIds(String typeId) {
        List<Long> lists = new ArrayList<>();
        if (StringUtils.isNotBlank(typeId)) {
            lists = Arrays.stream(typeId.split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
        }
        if (ObjectUtils.isNotEmpty(lists)) {
            List<Long> subLists = new ArrayList<>();
            for (Long type : lists) {
                List<AssemblyClassify> subClassify = classifyService.getAssemblyClassify("", AssemblyClassifyTypeEnum.ASSET_TYPE.getCode(), type);
                if (ObjectUtils.isNotEmpty(subClassify)) {
                    subLists.addAll(subClassify.stream().map(AssemblyClassify::getId).toList());
                }
            }
            lists.addAll(subLists);
        }
        return lists.stream().distinct().collect(Collectors.toList());
    }

    private String getAreaName(String areaId, Map<Long, String> areaMap) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(areaId)) {
            for (String str : areaId.split(",")) {
                String areaName = areaMap.get(Long.parseLong(str));
                if (StringUtils.isNotBlank(areaName)) {
                    sb.append(areaName).append("-");
                }
            }
        }
        return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
    }

    private Map<Long, String> getAreaNameMap(Set<String> set) {
        Set<Long> areaId = new HashSet<>();
        if (ObjectUtils.isNotEmpty(set)) {
            for (String str : set) {
                if (StringUtils.isNotBlank(str)) {
                    for (String s : str.split(",")) {
                        if (StringUtils.isNotBlank(s)) {
                            areaId.add(Long.parseLong(s));
                        }
                    }
                }
            }
        }
        Map<Long, String> areaMap = new HashMap<>();
        List<AreaDTO> areaDTOList = areaClient.getByIds(areaId);
        if (ObjectUtils.isNotEmpty(areaDTOList)) {
            areaMap = areaDTOList.stream().collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        }
        return areaMap;
    }

    private Map<Long, String> getUserNameMap(Set<Long> userIdSet) {
        List<UserDTO> users = userClientService.getUserByIds(new ArrayList<>(userIdSet));
        return users.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
    }

    @Override
    public PropertyStatisticsVO getPropertyStatistics(PropertyDetailedPageParam param) {
        return baseMapper.getPropertyStatistics(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(List<PropertyDetailedWorkflowParam> params, DepositoryDetailed depository) {
        Assert.isParameterError(
                ObjectUtils.isEmpty(depository) || StringUtils.isEmpty(depository.getDepositoryNumbers()), "入库单号不能为空");
        List<PropertyDetailed> propertyDetaileds = new ArrayList<>();
        for (PropertyDetailedWorkflowParam ex : params) {
            PropertyDetailed propertyDetailed = this.getPropertyDetailed(ex, depository.getDepositoryNumbers());
            propertyDetailed.setSourceId(depository.getSourceId());
            propertyDetaileds.add(propertyDetailed);
        }
        log.info("资产开始入库，入库编号：{}", depository.getDepositoryNumbers());
        return this.saveBatch(propertyDetaileds);
    }

    @Override
    public Boolean insert(PropertyDetailedParam params) {
        Assert.isParameterError(ObjectUtils.isEmpty(params.getTaskId()), "任务id不能为空");
        params.setAuthority(PropertySourceEnum.WIN.name());
        params.setDepositoryNumbers(PropertySourceEnum.WIN.name() + IdUtils.next());
        PropertyCheckTaskRelation taskRelation = checkTaskRelationMapper.selectOne(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                                           .eq(PropertyCheckTaskRelation::getTaskId, params.getTaskId())
                                                                                           .last("limit 1"));
        Assert.isNotFound(ObjectUtils.isEmpty(taskRelation), "该任务不存在");
        // 写入资产信息
        PropertyDetailed propertyDetailed = ConvertUtils.convert(PropertyDetailed.class, params);
        propertyDetailed.setStatus(PropertyStatusEnum.LEAVE_UNUSED.name());
        propertyDetailed.setLevel(1);
        if (ObjectUtils.isNotEmpty(params.getImgs())) {
            propertyDetailed.setImgs(StringUtils.join(",", params.getImgs()));
        } else {
            propertyDetailed.setImgs(null);
        }
        if (ObjectUtils.isNotEmpty(params.getAreaIds())) {
            propertyDetailed.setAreaId(params.getAreaIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        if (ObjectUtils.isNotEmpty(propertyDetailed.getPurchaseDate())) {
            if (ObjectUtils.isNotEmpty(propertyDetailed.getMaintenanceTime())) {
                propertyDetailed.setMaintenanceDate(DateUtils.offsetMonth(propertyDetailed.getPurchaseDate(), propertyDetailed.getMaintenanceTime()));
            }
            if (ObjectUtils.isNotEmpty(propertyDetailed.getUsageTime())) {
                propertyDetailed.setUsageDate(DateUtils.offsetMonth(propertyDetailed.getPurchaseDate(), propertyDetailed.getUsageTime()));
            }
        }
        propertyDetailedMapper.insert(propertyDetailed);
        // 加入任务
        PropertyCheckTaskRelation relation = new PropertyCheckTaskRelation();
        relation.setPlanId(taskRelation.getPlanId());
        relation.setTaskId(params.getTaskId());
        relation.setPropertyId(propertyDetailed.getId());
        relation.setCheckStatus(CheckTaskStatusEnum.WIN.name());
        // 盘赢+1
        PropertyCheckTask task = checkTaskMapper.selectById(params.getTaskId());
        task.setCheckWinNumber(task.getCheckWinNumber() + 1);
        checkTaskMapper.updateById(task);
        return checkTaskRelationMapper.insert(relation) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(List<PropertyDetailed> params) {
        if (ObjectUtils.isNotEmpty(params) && params.size() > 1) {
            return this.saveBatch(params);
        }
        return true;
    }

    @Override
    public Boolean update(PropertyDetailedParam param) {
        if (ObjectUtils.isEmpty(param.getId())) {
            throw new ServiceException("资产id不能为空");
        }
        this.existCode(param);
        PropertyDetailed propertyDetailed = ConvertUtils.convert(PropertyDetailed.class, param);
        PropertyDetailed detailed = baseMapper.selectById(param.getId());
        propertyDetailed.setDeleted(detailed.getDeleted());
        propertyDetailed.setTenant(detailed.getTenant());
        EntityAuditor.populateCreateAudit(propertyDetailed, detailed.getCreateBy(), detailed.getCreatorId(), detailed.getCreatorType(), detailed.getCreateTime());
        propertyDetailed.setLevel(detailed.getLevel());
        propertyDetailed.setAuthority(PropertySourceEnum.DEPOSITORY.name());
        if (ObjectUtils.isNotEmpty(param.getStartTime()) && ObjectUtils.isNotEmpty(param.getMaintenanceTime())) {
            propertyDetailed.setMaintenanceDate(DateUtils.offsetMonth(param.getStartTime(), param.getMaintenanceTime()));
        } else {
            propertyDetailed.setMaintenanceDate(null);
        }
        if (ObjectUtils.isNotEmpty(param.getStartTime()) && ObjectUtils.isNotEmpty(param.getUsageTime())) {
            propertyDetailed.setUsageDate(DateUtils.offsetMonth(param.getStartTime(), param.getUsageTime()));
        } else {
            propertyDetailed.setUsageDate(null);
        }
        if (ObjectUtils.isNotEmpty(param.getAreaIds())) {
            propertyDetailed.setAreaId(param.getAreaIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        if (ObjectUtils.isNotEmpty(propertyDetailed.getMaintenanceDepId())) {
            OrganizationDTO organization = organizationClient.getOrganizationsById(propertyDetailed.getMaintenanceDepId());
            propertyDetailed.setMaintenanceDepName(Optional.ofNullable(organization).orElse(new OrganizationDTO()).getName());
        }
        Set<Long> files = new HashSet<>();
        if (ObjectUtils.isNotEmpty(param.getImgs())) {
            files.addAll(param.getImgs());
        }
        if (ObjectUtils.isNotEmpty(param.getFile())) {
            files.addAll(param.getFile());
        }
        propertyDetailed.setImgs(ObjectUtils.isNotEmpty(param.getImgs()) ? StringUtils.join(",", param.getImgs()) : null);
        propertyDetailed.setFile(ObjectUtils.isNotEmpty(param.getFile()) ? StringUtils.join(",", param.getFile()) : null);
        Boolean bool = baseMapper.updateById(propertyDetailed) > 0;
        if (!files.isEmpty()) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setSceneCode(FileSceneCodeEnum.PROPERTY_MANAGEMENT_PROPERTY.getCode());
            fileInduceParam.setFileIdList(new ArrayList<>(files));
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        return bool;
    }

    @Override
    public Boolean updateCode(PropertyDetailedParam param) {
        if (ObjectUtils.isEmpty(param.getId())) {
            throw new ServiceException("资产id不能为空");
        }
        PropertyDetailed propertyDetailed = baseMapper.selectById(param.getId());
        if (ObjectUtils.isEmpty(propertyDetailed)) {
            throw new ServiceException("资产信息不存在");
        }
        if (ObjectUtils.isNotEmpty(param.getFinanceNumber())) {
            propertyDetailed.setFinanceNumber(param.getFinanceNumber());
        } else if (ObjectUtils.isNotEmpty(param.getSerialNumber())) {
            if (!param.getSerialNumber().equals(propertyDetailed.getSerialNumber())) {
                this.existCode(param);
            }
        }
        propertyDetailed.setSerialNumber(param.getSerialNumber());
        return baseMapper.updateById(propertyDetailed) > 0;
    }

    /**
     * 盘点编码是否重复
     */
    private void existCode(PropertyDetailedParam param) {
        Long detailed = baseMapper.selectCount(new LambdaQueryWrapper<PropertyDetailed>()
                                                       .eq(PropertyDetailed::getSerialNumber, param.getSerialNumber()));
        if (ObjectUtils.isNotEmpty(detailed)) {
            if (detailed > 1) {
                throw new ServiceException("编码重复");
            } else if (detailed == 1) {
                PropertyDetailed data = baseMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                                     .eq(PropertyDetailed::getSerialNumber, param.getSerialNumber()));
                if (!param.getId().equals(data.getId())) {
                    throw new ServiceException("编码重复");
                }
            }
        }
        this.existBimCode(param.getId(), param.getDeviceNumber());
    }

    /**
     * 判断模型编码是否重复
     */
    private void existBimCode(String bimCode) {
        this.existBimCode(null, bimCode);
    }

    private void existBimCode(Long id, String bimCode) {
        if (ObjectUtils.isNotEmpty(bimCode)) {
            Long bimCodeNum = baseMapper.selectCount(new LambdaQueryWrapper<PropertyDetailed>()
                                                             .eq(PropertyDetailed::getDeviceNumber, bimCode));
            if (ObjectUtils.isNotEmpty(bimCode)) {
                Assert.isConflict(bimCodeNum > 1, "模型编码不能重复");
                if (ObjectUtils.isEmpty(id)) {
                    Assert.isConflict(bimCodeNum > 0, "模型编码不能重复");
                } else if (bimCodeNum == 1) {
                    PropertyDetailed data = baseMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                                         .eq(PropertyDetailed::getDeviceNumber, bimCode));
                    if (!id.equals(data.getId())) {
                        throw new ServiceException("模型编码不能重复");
                    }
                }
            }
        }
    }

    @Override
    public Boolean update(PropertyStatusEnum statusEnum, List<Long> ids) {
        return baseMapper.updateAllByList(ids, statusEnum.name()) > 0;
    }

    @Override
    public Boolean update(PropertyStatusEnum statusEnum, Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return this.update(statusEnum, ids);
    }

    @Override
    public Boolean deleteById(Long id) {
        return this.lambdaUpdate().eq(PropertyDetailed::getId, id)
                .set(PropertyDetailed::getStatus, PropertyStatusEnum.DELETE.name())
                .update();
    }

    @Override
    public Boolean deleteByDepositoryNumbers(String depositoryNumbers) {
        log.info("开始删除入库资产信息");
        UpdateWrapper<PropertyDetailed> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("deleted", true).eq("depository_numbers", depositoryNumbers);
        return baseMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public PropertyByDepAll getPropertyByDepAllPage(OrganizationQueryParam param) {
        PropertyByDepAll depAll = baseMapper.getPropertyByDepAllMoney(param);
        List<PropertyByDepAllPageVO> property = baseMapper.getPropertyByDepAllPage(param);
        if (ObjectUtils.isNotEmpty(depAll)) {
            depAll.setDepAll(property);
        }
        return depAll;
    }

    @Override
    public void getPropertyByDepAllExcel(OrganizationQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        PropertyByDepAll all = this.getPropertyByDepAllPage(param);
        List<PropertyByDepAllPageVO> datas = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(all)) {
            datas = all.getDepAll();
            PropertyByDepAllPageVO extend = new PropertyByDepAllPageVO();
            extend.setDepName("合计");
            extend.setTotalPrice(all.getTotalPrice());
            extend.setTotalNumber(Integer.parseInt(all.getTotalNumber()));
            datas.add(extend);
        }
        if (ObjectUtils.isEmpty(datas)) {
            PropertyByDepAllPageVO extend = new PropertyByDepAllPageVO();
            datas.add(extend);
        }
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("depName", "部门名称");
        writer.addHeaderAlias("totalNumber", "资产数量");
        writer.addHeaderAlias("totalPrice", "金额");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "部门资产汇总";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 维保到期、逾龄资产
     *
     * @param param 筛选条件
     *
     * @return 维保到期、逾龄资产
     */
    public Pagination<PropertyDetailedPageVO> getOveragePage(OveragePageParam param) {
        Page<PropertyDetailedPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        if (ObjectUtils.isEmpty(param.getMaintenanceData())) {
            param.setMaintenanceData(DateUtils.formatDate(LocalDateTime.now()));
        }
        page = this.baseMapper.getOveragePage(page, param);
        Pagination<PropertyDetailedPageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        pagination.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
                ex.setArea(area);
                ex.setAdminName(userClientService.getUser(ex.getAdminId()));
                ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserId()));
                ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
                if (ObjectUtils.isNotEmpty(ex.getImgsStr())) {
                    List<Long> fileIdds = Arrays.stream(ex.getImgsStr().split(","))
                            .map(s -> Long.parseLong(s.trim()))
                            .collect(Collectors.toList());
                    List<FileVO> file = fileClient.getByIds(fileIdds);
                    if (ObjectUtils.isNotEmpty(file)) {
                        ex.setImgs(file);
                    }
                }
            }
        });
        return pagination;
    }

    @Override
    public void getOverageExcel(OveragePageParam param, HttpServletRequest request, HttpServletResponse response) {
        String fileName;
        String total = param.getQueryType().equals("maintenance") ? "维保到期资产总数：0         总金额：0" : "逾龄资产总数：0  总金额：0";
        param.setPageNumber(1);
        param.setPageSize(10000);
        Pagination<PropertyDetailedPageVO> page = this.getOveragePage(param);
        List<PropertyDetailedPageVO> datas = page.getRecords();
        if (ObjectUtils.isEmpty(datas)) {
            datas = new ArrayList<>();
            PropertyDetailedPageVO extend = new PropertyDetailedPageVO();
            datas.add(extend);
        } else {
            PropertyDetailedTotalVO totalData = this.getOverageAmount(param);
            total = (param.getQueryType().equals("maintenance") ? "维保到期资产总数" : "逾龄资产总数") +
                    totalData.getTotalNumber() +
                    "         " +
                    "总金额" +
                    totalData.getTotalPrice();
            datas.forEach(ex -> {
                ex.setStatus(PropertyStatusEnum.getRemarks(ex.getStatus()));
                if (ObjectUtils.isNotEmpty(ex.getArea())) {
                    List<String> areaName = ex.getArea().stream().map(AreaDTO::getName).collect(Collectors.toList());
                    ex.setAreaId(String.join(",", areaName));
                }
            });
        }
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        if (param.getQueryType().equals("maintenance")) {
            fileName = "维保到期资产";
            writer.addHeaderAlias("maintenanceTime", "维保到期时间");
        } else {
            fileName = "逾龄资产统计";
            writer.addHeaderAlias("usageDate", "逾龄时间");
        }
        writer.addHeaderAlias("name", "资产名称");
        writer.addHeaderAlias("serialNumber", "资产编码");
        writer.addHeaderAlias("typeName", "资产类型");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("norms", "规格型号");
        writer.addHeaderAlias("unit", "计量单位");
        writer.addHeaderAlias("price", "金额");
        writer.addHeaderAlias("receiveUserName", "使用人");
        writer.addHeaderAlias("receiveDepName", "使用部门");
        writer.addHeaderAlias("purchaseDate", "购入日期");
        writer.addHeaderAlias("area", "区域");
        writer.addHeaderAlias("storageLocation", "存放地点");
        writer.addHeaderAlias("adminName", "管理员");
        writer.addHeaderAlias("sourceName", "来源");
        writer.setOnlyAlias(true);
        writer.merge(14, total).getHeadCellStyle().setAlignment(HorizontalAlignment.LEFT);
        //默认配置
        writer.write(datas, true);
        DataFormat dataFormat2 = writer.getWorkbook().createDataFormat();
        CellStyle cellStyle2 = writer.getStyleSet().getCellStyleForDate();
        short format2 = dataFormat2.getFormat("yyyy-MM-dd");
        cellStyle2.setDataFormat(format2);
        writer.setStyle(cellStyle2, 1, 11);
        writer.setColumnWidth(-1, 25);
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    @Override
    public PropertyDetailedTotalVO getOverageAmount(OveragePageParam param) {
        if (ObjectUtils.isEmpty(param.getMaintenanceData())) {
            param.setMaintenanceData(DateUtils.formatDate(LocalDateTime.now()));
        }
        return baseMapper.getOverageAmount(param);
    }

    @Override
    public Boolean OverageJob() {
        Boolean bool = true;
        // 查询资产预警条件
        MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram
                .builder()
                .businessType(MessageBusinessTypeEnum.PROPERTY.name())
                .sceneCodes(MessageModuleTypeEnum.getNameList())
                .build();
        List<MessageSendConfig> conditions = assemblyNoticeClient.getSendMsgConfig(queryPram);
        for (MessageSendConfig ex : conditions) {
            Map<String, String> map = new HashMap<>();
            List<PropertyDetailed> detailedList = new ArrayList<>();
            StringBuffer propertyNmae = new StringBuffer();
            Long propertyTotal = 0L;
            // 获取维保到期提醒时间
            DateTimeSlotVO maintenanceDate =
                    MessageModuleTypeEnum.MAINTENANCE_OVERDUE_ABOUT.name().equals(ex.getSceneCode()) ? DateLocalUtil.getToday(ex.getValue())
                                                                                                     : DateLocalUtil.getToday(-ex.getValue());
            // 获取使用到期提醒时间
            DateTimeSlotVO usageDate =
                    MessageModuleTypeEnum.OPERATE_OVERDUE_ABOUT.name().equals(ex.getSceneCode()) ? DateLocalUtil.getToday(ex.getValue())
                                                                                                 : DateLocalUtil.getToday(-ex.getValue());
            // 维保到期
            if (MessageModuleTypeEnum.MAINTENANCE_OVERDUE_ABOUT.name().equals(ex.getSceneCode()) ||
                MessageModuleTypeEnum.MAINTENANCE_OVERDUE.name().equals(ex.getSceneCode())) {
                propertyTotal = baseMapper.selectCount(new LambdaQueryWrapper<PropertyDetailed>()
                                                               .between(PropertyDetailed::getMaintenanceDate, maintenanceDate.getStartTime(), maintenanceDate.getEndTime()));
                if (ObjectUtils.isNotEmpty(propertyTotal) && propertyTotal > 0) {
                    detailedList = baseMapper.selectList(new LambdaQueryWrapper<PropertyDetailed>()
                                                                 .between(PropertyDetailed::getMaintenanceDate, maintenanceDate.getStartTime(), maintenanceDate.getEndTime())
                                                                 .last("limit 2"));
                    for (PropertyDetailed detailed : detailedList) {
                        if (ObjectUtils.isNotEmpty(propertyNmae)) {
                            propertyNmae.append("、");
                        }
                        propertyNmae.append(detailed.getName());
                    }
                }
            } else if (MessageModuleTypeEnum.OPERATE_OVERDUE_ABOUT.name().equals(ex.getSceneCode()) ||
                       MessageModuleTypeEnum.OPERATE_OVERDUE.name().equals(ex.getSceneCode())) {
                propertyTotal = baseMapper.selectCount(new LambdaQueryWrapper<PropertyDetailed>()
                                                               .between(PropertyDetailed::getUsageDate, usageDate.getStartTime(), usageDate.getEndTime()));
                if (ObjectUtils.isNotEmpty(propertyTotal) && propertyTotal > 0) {
                    detailedList = baseMapper.selectList(new LambdaQueryWrapper<PropertyDetailed>()
                                                                 .between(PropertyDetailed::getUsageDate, usageDate.getStartTime(), usageDate.getEndTime()));
                    for (PropertyDetailed detailed : detailedList) {
                        if (ObjectUtils.isNotEmpty(propertyNmae)) {
                            propertyNmae.append("、");
                        }
                        propertyNmae.append(detailed.getName());
                    }
                }
            }
            if (ObjectUtils.isNotEmpty(propertyTotal) && propertyTotal > 0) {
                map.put("propertyNmae", propertyNmae.toString());
                map.put("propertyTotal", propertyTotal.toString());
                map.put("number", ex.getValue().toString());
                messageUtil.messageSend(ex, map);
            }
        }
        return bool;
    }

    @Override
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileNameEncode;
        fileNameEncode = URLEncoder.encode("资产导入模板", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLS.getValue());
        // 下拉框内容
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        List<String> assetType = assemblyClassifyMapper.getAllByType("assetType");
        List<String> assetSource = assemblyClassifyMapper.getAllByType("assetSource");
        if (ObjectUtils.isNotEmpty(assetType)) {
            dropdownData.put(3, assetType);
        }
        if (ObjectUtils.isNotEmpty(assetSource)) {
            dropdownData.put(10, assetSource);
        }
        OrganizationQueryParam orgParam = new OrganizationQueryParam();
        orgParam.setEnabled(true);
        List<OrganizationDTO> orgList = organizationClient.getOrganizations(orgParam);
        if (ObjectUtils.isNotEmpty(orgList)) {
            dropdownData.put(13, orgList.stream().map(OrganizationDTO::getName).collect(Collectors.toList()));
            dropdownData.put(14, orgList.stream().map(OrganizationDTO::getName).collect(Collectors.toList()));
        }
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData, 21))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(PropertyDetailedExcelTemplateParam.class)
                    .sheet("资产导入模板")
                    .doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void importTemplate(MultipartFile file) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        ExcelReader writer = null;
        try {
            writer = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("文件异常，导入失败");
        }
        writer.addHeaderAlias("*资产编号", "serialNumber");
        writer.addHeaderAlias("*资产名称", "name");
        writer.addHeaderAlias("财务编码", "financeNumber");
        writer.addHeaderAlias("*资产类别", "typeName");
        writer.addHeaderAlias("购入日期", "purchaseDate");
        writer.addHeaderAlias("*使用期限（月）", "usageTime");
        writer.addHeaderAlias("维保期限（月）", "maintenanceTime");
        writer.addHeaderAlias("规格型号", "norms");
        writer.addHeaderAlias("计量单位", "unit");
        writer.addHeaderAlias("属性值", "price");
        writer.addHeaderAlias("来源", "sourceName");
        writer.addHeaderAlias("*入库日期", "startTime");
        writer.addHeaderAlias("管理员", "adminName");
        writer.addHeaderAlias("维护部门", "maintenanceDepName");
        writer.addHeaderAlias("使用部门", "receiveDepName");
        writer.addHeaderAlias("使用人", "receiveUserName");
        writer.addHeaderAlias("模型编码", "deviceNumber");
        List<PropertyDetailedExcelTemplateParam> datas = writer.readAll(PropertyDetailedExcelTemplateParam.class);
        Assert.isParameterError(CollectionUtils.isEmpty(datas), "无法添加空的excel");
        List<PropertyDetailed> data = this.addTemplate(datas);
        data.forEach(ex -> {
            this.existBimCode(ex.getDeviceNumber());
            this.baseMapper.insert(ex);
        });
    }

    @Override
    public List<Integer> getAllTenant() {
        return this.baseMapper.getAllTenant();
    }

    private List<PropertyDetailed> addTemplate(List<PropertyDetailedExcelTemplateParam> datas) {
        List<PropertyDetailed> data = new ArrayList<>();
        datas.forEach(ex -> {
            Assert.isParameterError(StringUtils.isBlank(ex.getSerialNumber()), "资产编号不能为空");
            Assert.isParameterError(StringUtils.isBlank(ex.getName()), "资产名称不能为空");
            Assert.isParameterError(StringUtils.isBlank(ex.getTypeName()), "资产类别不能为空");
            Assert.isParameterError(Objects.isNull(ex.getStartTime()), "入库日期不能为空");
            Assert.isParameterError(Objects.isNull(ex.getUsageTime()), "使用期限（月）不能为空");
            PropertyDetailed detailed = new PropertyDetailed();
            detailed.setSerialNumber(ex.getSerialNumber());
            detailed.setName(ex.getName());
            detailed.setUsageTime(ex.getUsageTime());
            detailed.setMaintenanceTime(ex.getMaintenanceTime());
            detailed.setDepositoryNumbers("DR");
            detailed.setNorms(ex.getNorms());
            detailed.setUnit(ex.getUnit());
            detailed.setPrice(ex.getPrice());
            detailed.setPurchaseDate(ex.getPurchaseDate());
            detailed.setStartTime(ex.getStartTime());
            detailed.setFinanceNumber(ex.getFinanceNumber());
            detailed.setDeviceNumber(ex.getDeviceNumber());
            if (ObjectUtils.isNotEmpty(ex.getTypeName())) {
                AssemblyClassify classify = assemblyClassifyMapper.selectOne(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                     .eq(AssemblyClassify::getName, ex.getTypeName())
                                                                                     .eq(AssemblyClassify::getType, "assetType")
                                                                                     .last("limit 1"));
                Assert.isParameterError(Objects.isNull(classify), "资产类别不存在，请下载新的模板");
                if (ObjectUtils.isNotEmpty(classify)) {
                    detailed.setTypeId(classify.getId());
                    detailed.setTypeName(ex.getTypeName());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getSourceName())) {
                AssemblyClassify classify = assemblyClassifyMapper.selectOne(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                     .eq(AssemblyClassify::getName, ex.getSourceName())
                                                                                     .eq(AssemblyClassify::getType, "assetSource")
                                                                                     .last("limit 1"));
                if (ObjectUtils.isNotEmpty(classify)) {
                    detailed.setSourceId(classify.getId());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getAdminName())) {
                List<UserDTO> user = userClient.listUsers(ex.getAdminName());
                if (ObjectUtils.isNotEmpty(user)) {
                    user.forEach(userDTO -> {
                        if (userDTO.getName().equals(ex.getAdminName())) {
                            detailed.setAdminId(userDTO.getId());
                        }
                    });
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getMaintenanceDepName())) {
                OrganizationDTO organization = organizationClient.getOrganizationsByName(ex.getMaintenanceDepName());
                if (ObjectUtils.isNotEmpty(organization)) {
                    detailed.setMaintenanceDepId(organization.getId());
                    detailed.setMaintenanceDepName(organization.getName());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getReceiveDepName())) {
                OrganizationDTO organization = organizationClient.getOrganizationsByName(ex.getReceiveDepName());
                if (ObjectUtils.isNotEmpty(organization)) {
                    detailed.setReceiveDepId(organization.getId());
                    detailed.setReceiveDepName(organization.getName());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getReceiveUserName())) {
                List<UserDTO> user = userClient.listUsers(ex.getReceiveUserName());
                if (ObjectUtils.isNotEmpty(user)) {
                    user.forEach(userDTO -> {
                        if (userDTO.getName().equals(ex.getReceiveUserName())) {
                            detailed.setReceiveUserId(userDTO.getId());
                        }
                    });
                }
            }
            if (ObjectUtils.isNotEmpty(detailed.getPurchaseDate())) {
                if (ObjectUtils.isNotEmpty(detailed.getMaintenanceTime())) {
                    detailed.setMaintenanceDate(DateUtils.offsetMonth(detailed.getPurchaseDate(), detailed.getMaintenanceTime()));
                }
                if (ObjectUtils.isNotEmpty(detailed.getUsageTime())) {
                    detailed.setUsageDate(DateUtils.offsetMonth(detailed.getPurchaseDate(), detailed.getUsageTime()));
                }
            }
            detailed.setAuthority(PropertySourceEnum.DEPOSITORY.name());
            detailed.setStatus(ObjectUtils.isNotEmpty(detailed.getReceiveDepId()) ? PropertyStatusEnum.MAKE_USE_OF.name()
                                                                                  : PropertyStatusEnum.LEAVE_UNUSED.name());
            detailed.setLevel(1);
            data.add(detailed);
        });
        return data;
    }

    /**
     * 封装资产信息
     *
     * @param ex                资产信息
     * @param depositoryNumbers 入库单号
     *
     * @return 资产信息
     */
    private PropertyDetailed getPropertyDetailed(PropertyDetailedWorkflowParam ex, String depositoryNumbers) {
        PropertyDetailed propertyDetailed = ConvertUtils.convert(PropertyDetailed.class, ex);
        if (ObjectUtils.isNotEmpty(ex.getStartTime()) && ObjectUtils.isNotEmpty(ex.getMaintenanceTime())) {
            propertyDetailed.setMaintenanceDate(DateUtils.offsetMonth(ex.getStartTime(), ex.getMaintenanceTime()));
        }
        if (ObjectUtils.isNotEmpty(ex.getStartTime()) && ObjectUtils.isNotEmpty(ex.getUsageTime())) {
            propertyDetailed.setUsageDate(DateUtils.offsetMonth(ex.getStartTime(), ex.getUsageTime()));
        }
        if (ObjectUtils.isNotEmpty(ex.getAdminId()) && !ex.getAdminId().isEmpty()) {
            var admin = JsonUtils.deserialize(ex.getAdminId().getFirst(), new TypeReference<Map<String, Object>>() {});
            if (admin != null) {
                propertyDetailed.setAdminId(Long.parseLong(admin.get("id").toString()));
            }
        }
        if (ObjectUtils.isNotEmpty(ex.getType())) {
            propertyDetailed.setTypeName(ex.getType().getName());
            propertyDetailed.setTypeId(ex.getType().getId());
        }
        propertyDetailed.setImgs(ObjectUtils.isNotEmpty(ex.getImgs()) ? StringUtils.join(",", ex.getImgs()) : null);
        propertyDetailed.setFile(ObjectUtils.isNotEmpty(ex.getFile()) ? StringUtils.join(",", ex.getFile()) : null);
        propertyDetailed.setAuthority(StringUtils.isNotEmpty(ex.getAuthority()) ? ex.getAuthority() : PropertySourceEnum.DEPOSITORY.name());
        propertyDetailed.setId(IdUtils.next());
        propertyDetailed.setLevel(1);
        propertyDetailed.setDepositoryNumbers(depositoryNumbers);
        propertyDetailed.setStatus(PropertyStatusEnum.UNDER.name());
        return propertyDetailed;
    }

    /**
     * 资产清单数据组装
     */
    public void getPropertyDetailedData(List<PropertyDetailedPageVO> data, String type) {
        for (PropertyDetailedPageVO ex : data) {
            ex.setStatusName(PropertyStatusEnum.getRemarks(ex.getStatus()));
            ex.setAdminName(userClientService.getUser(ex.getAdminId()));
            ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserId()));
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
            ex.setPriceTotal(ObjectUtils.isNotEmpty(ex.getNumber()) && ObjectUtils.isNotEmpty(ex.getPrice())
                             ? BigDecimal.valueOf(ex.getNumber()).multiply(ex.getPrice())
                             : BigDecimal.ZERO);
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
                ex.setArea(area);
                if (ObjectUtils.isNotEmpty(area)) {
                    StringBuffer areaName = new StringBuffer();
                    for (AreaDTO obj : area) {
                        if (ObjectUtils.isNotEmpty(areaName)) {
                            areaName.append("/");
                        }
                        areaName.append(obj.getName());
                    }
                    ex.setAreaName(areaName.toString());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getSourceId())) {
                String sourceName = "";//redisTemplate.opsForValue().get("property:" + ex.getSourceId());
                if (ObjectUtils.isEmpty(sourceName)) {
                    AssemblyClassifyVO classify = assemblyClassifyService.queryById(ex.getSourceId());
                    if (ObjectUtils.isNotEmpty(classify)) {
                        sourceName = classify.getName();
                    }
                }
                ex.setSourceName(sourceName);
            }
            if (ObjectUtils.isNotEmpty(ex.getMaintenanceDate())) {
                // 计算剩余维保时间
                long maintenanceTime = DateUtils.compare(ex.getMaintenanceDate(), LocalDateTime.now()) < 1 ? 0
                                                                                                           : DateUtils.between(LocalDateTime.now(), ex.getMaintenanceDate(), ChronoUnit.MONTHS);
                if (maintenanceTime < 1) {
                    maintenanceTime = 0L;
                }
                ex.setMaintenanceTime(maintenanceTime);
            }
            if (ObjectUtils.isNotEmpty(ex.getUsageDate())) {
                // 计算剩余使用期限
                long usageTime = DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1 ? 0
                                                                                               : DateUtils.between(LocalDateTime.now(), ex.getUsageDate(), ChronoUnit.MONTHS);
                if (usageTime < 1) {
                    usageTime = 0L;
                }
                ex.setUsageTime(usageTime);
            }
            if (ObjectUtils.isEmpty(type)) {
                if (ObjectUtils.isNotEmpty(ex.getFileStr())) {
                    List<Long> fileIdds = Arrays.stream(ex.getFileStr().split(","))
                            .map(s -> Long.parseLong(s.trim()))
                            .collect(Collectors.toList());
                    List<FileVO> file = fileClient.getByIds(fileIdds);
                    if (ObjectUtils.isNotEmpty(file)) {
                        ex.setFile(file);
                    }
                }
                if (ObjectUtils.isNotEmpty(ex.getImgsStr())) {
                    List<Long> fileIdds = Arrays.stream(ex.getImgsStr().split(","))
                            .map(s -> Long.parseLong(s.trim()))
                            .collect(Collectors.toList());
                    List<FileVO> file = fileClient.getByIds(fileIdds);
                    if (ObjectUtils.isNotEmpty(file)) {
                        ex.setImgs(file);
                    }
                }
            }
        }
    }

    /**
     * 校验资产编码是否重复
     *
     * @param serialNumber 资产编码
     *
     * @return true 表示不重复
     */
    @Override
    public Boolean checkPropertyCode(String serialNumber) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                 .eq(PropertyDetailed::getSerialNumber, serialNumber)
                                                 .last(" limit 1 ")
                                        ) == null;
    }

    /**
     * 校验资产编码是否重复
     *
     * @param param checkSerialNumberList 资产编码集合
     *
     * @return 异常编码集合
     */
    @Override
    public List<String> checkPropertyCodeBath(OveragePageParam param) {
        List<String> errors = new ArrayList<>();
        if (param == null || param.getCheckSerialNumberList() == null || param.getCheckSerialNumberList().isEmpty()) {
            return errors;
        }
        Map<String, Long> map = new HashMap<>();
        List<String> serialNumberList = new ArrayList<>();
        param.getCheckSerialNumberList().forEach(p -> {
            serialNumberList.add(p.getValue());
            if (map.containsKey(p.getValue())) {
                errors.add(p.getValue());
            }
            map.put(p.getValue(), p.getKey());
        });
        // 入参自身重复
        if (!errors.isEmpty()) {
            return errors;
        }

        List<PropertyDetailed> propertyDetailedList = this.baseMapper.selectList(Wrappers.<PropertyDetailed>lambdaQuery()
                                                                                         .select(PropertyDetailed::getId, PropertyDetailed::getSerialNumber)
                                                                                         .in(PropertyDetailed::getSerialNumber, serialNumberList)
                                                                                );
        if (propertyDetailedList == null || propertyDetailedList.isEmpty()) {
            return errors;
        }

        return propertyDetailedList.stream()
                .filter(p -> map.containsKey(p.getSerialNumber()) && !Objects.equals(map.get(p.getSerialNumber()), p.getId()))
                .map(PropertyDetailed::getSerialNumber).collect(Collectors.toList());
    }

    @Override
    public List<PropertyDetailedVO> queryPropertyDetailBatch(List<Long> idList) {
        List<PropertyDetailed> propertyDetaileds = this.baseMapper.selectList(Wrappers.<PropertyDetailed>lambdaQuery()
                                                                                      .in(PropertyDetailed::getId, idList));
        if (CollectionUtils.isEmpty(propertyDetaileds)) {
            return new ArrayList<>();
        }

        Set<Long> userIds = new HashSet<>();
        Set<Long> assemblyIds = new HashSet<>();
        Set<Long> areaIds = new HashSet<>();
        for (PropertyDetailed detailed : propertyDetaileds) {
            if (Objects.nonNull(detailed.getReceiveUserId())) {
                userIds.add(detailed.getReceiveUserId());
            }
            if (Objects.nonNull(detailed.getAdminId())) {
                userIds.add(detailed.getAdminId());
            }
            if (Objects.nonNull(detailed.getSourceId())) {
                assemblyIds.add(detailed.getSourceId());
            }
            if (Objects.nonNull(detailed.getTypeId())) {
                assemblyIds.add(detailed.getTypeId());
            }
            if (StringUtils.isNotBlank(detailed.getAreaId())) {
                areaIds.addAll(StringUtils.splitAndTranslate(detailed.getAreaId(), Long::valueOf));
            }

        }
        Map<Long, String> userMap = userClientService.getUserByIds(new ArrayList<>(userIds))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getUsername));
        Map<Long, String> assemblyClassMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(assemblyIds)) {

            assemblyClassMap = Optional.ofNullable(assemblyClassifyService.lambdaQuery()
                                                           .in(AssemblyClassify::getId, assemblyIds)
                                                           .list()).orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(AssemblyClassify::getId, AssemblyClassify::getName));
        }
        List<AreaDTO> area = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(areaIds)) {
            area = areaClient.getByIds(areaIds);
        }
        List<PropertyDetailedVO> result = new ArrayList<>();
        for (PropertyDetailed detailed : propertyDetaileds) {
            PropertyDetailedVO vo = ConvertUtils.convert(PropertyDetailedVO.class, detailed);
            vo.setReceiveUserName(userMap.get(vo.getReceiveUserId()));
            vo.setAdminName(userMap.get(detailed.getAdminId()));
            vo.setTypeName(assemblyClassMap.get(detailed.getTypeId()));
            vo.setSourceName(assemblyClassMap.get(detailed.getSourceId()));
            if (StringUtils.isNotBlank(detailed.getAreaId())) {
                List<AreaDTO> areaDTOList = area.stream()
                        .filter(t -> StringUtils.splitAndTranslate(detailed.getAreaId(), Long::valueOf).contains(t.getId()))
                        .collect(Collectors.toList());
                vo.setArea(areaDTOList);
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public Map<Long, Long> countByAreaIds(List<Long> areaIds) {
        Map<Long, Long> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(areaIds)) {
            return resultMap;
        }

        // 初始化结果Map
        for (Long areaId : areaIds) {
            resultMap.put(areaId, 0L);
        }

        // 批量查询所有资产（只查询areaId字段，提高效率）
        List<PropertyDetailed> allProperties = this.list(
                Wrappers.<PropertyDetailed>lambdaQuery()
                        .select(PropertyDetailed::getAreaId)
                        .isNotNull(PropertyDetailed::getAreaId)
                        .ne(PropertyDetailed::getAreaId, "")
                                                        );

        // 统计每个区域的资产数
        // areaId用逗号分隔（如"1,2,3"）
        for (PropertyDetailed property : allProperties) {
            if (StringUtils.isBlank(property.getAreaId())) {
                continue;
            }

            // 解析逗号分隔的区域ID
            String[] areaIdArray = property.getAreaId().split(",");
            Set<Long> propertyAreaIds = new HashSet<>();
            for (String areaIdStr : areaIdArray) {
                if (StringUtils.isNotBlank(areaIdStr)) {
                    try {
                        propertyAreaIds.add(Long.parseLong(areaIdStr.trim()));
                    } catch (NumberFormatException e) {
                        log.warn("解析资产区域ID失败: {}", areaIdStr, e);
                    }
                }
            }

            // 统计匹配的区域
            for (Long areaId : areaIds) {
                if (propertyAreaIds.contains(areaId)) {
                    resultMap.put(areaId, resultMap.getOrDefault(areaId, 0L) + 1);
                }
            }
        }

        return resultMap;
    }
}
