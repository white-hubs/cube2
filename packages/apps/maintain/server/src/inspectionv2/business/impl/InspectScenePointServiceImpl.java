package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointDeviceBaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectScenePointMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointExcelTemplate;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectScenePointParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 15:54
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectScenePointServiceImpl extends ServiceImpl<InspectScenePointMapper, InspectScenePointPO> implements InspectScenePointService {
    private final AreaClientService areaClientService;
    private final InspectScenePointDeviceService inspectScenePointDeviceService;

    /**
     * @Author wql
     * @Description 新增现场点
     * @Date 17:15 2023/6/5
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoint(InspectScenePointParam point) {
        InspectScenePointPO copying = ObjectUtils.copying(point, InspectScenePointPO.class);
        Assert.isConflict(!checkNameAndCode(copying), "该现场点编码重复");
        this.save(copying);
        inspectScenePointDeviceService.replaceScenePointDevices(copying.getId(),
                                                                Optional.ofNullable(point.getDeviceIdList())
                                                                        .orElse(Collections.emptyList()));
        //添加现场点需要同步往所有对象表中添加一条数据
        log.info("新增现场点:{}", copying.getId());
    }

    /**
     * @Author wql
     * @Description 检查现场点名称和编码是否重复
     * @Date 9:28 2023/6/6
     **/
    private boolean checkNameAndCode(InspectScenePointPO point) {
        List<InspectScenePointPO> list = this.lambdaQuery()
                .eq(InspectScenePointPO::getCodeNum, point.getCodeNum())
                .ne(ObjectUtils.isNotEmpty(point.getId()), InspectScenePointPO::getId, point.getId())
                .list();
        return ObjectUtils.isEmpty(list);
    }

    /**
     * @Author wql
     * @Description 现场点列表
     * @Date 18:14 2023/6/5
     **/
    @Override
    public Pagination<ScenePointVO> scenePointList(InspectScenePointQueryParam param) {
        Page<InspectScenePointPO> page = this.lambdaQuery()
                .in(ListUtils.isNotEmpty(param.getIds()), InspectScenePointPO::getId, param.getIds())
                .eq(ObjectUtils.isNotNull(param.getTypeId()), InspectScenePointPO::getTypeId, param.getTypeId())
                .eq(ObjectUtils.isNotNull(param.getReceiveDepId()), InspectScenePointPO::getDepId, param.getReceiveDepId())
                .like(StringUtils.isNotBlank(param.getPointName()), InspectScenePointPO::getName, param.getPointName())
                .like(StringUtils.isNotBlank(param.getAreaId()), InspectScenePointPO::getAreaId, param.getAreaId())
                .orderByDesc(InspectScenePointPO::getCreateTime)
                .orderByDesc(InspectScenePointPO::getId)
                .page(PagingUtils.convert(param));
        List<ScenePointVO> list = new ArrayList<>();
        Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).forEach(point -> {
            ScenePointVO vo = ObjectUtils.copying(point, ScenePointVO.class);
            vo.setAreaName(areaClientService.getAreaNamesByIds(point.getAreaId()));
            list.add(vo);
        });
        fillDeviceIds(list);
        return PagingUtils.convert(page, list);
    }

    /**
     * @Author wql
     * @Description 更新现场点
     * @Date 9:22 2023/6/6
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePoint(InspectScenePointParam point) {
        Assert.isParameterError(point.getId() == null, "id不能为空");
        InspectScenePointPO oldPO = this.getById(point.getId());
        Assert.isParameterError(ObjectUtils.isNull(oldPO), "现场点不存在");
        InspectScenePointPO copying = ObjectUtils.copying(point, InspectScenePointPO.class);
        Assert.isConflict(!checkNameAndCode(copying), "该现场点编码重复");
        copying.setDeleted(false);
        EntityAuditor.populateCreateAudit(copying, oldPO.getCreateBy(), oldPO.getCreatorId(), oldPO.getCreatorType(), oldPO.getCreateTime());
        this.updateById(copying);
        if (point.getDeviceIdList() != null) {
            inspectScenePointDeviceService.replaceScenePointDevices(point.getId(), point.getDeviceIdList());
        }
    }

    /**
     * @Author wql
     * @Description 删除现场点
     * @Date 9:35 2023/6/6
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePoint(Long pointId) {
        inspectScenePointDeviceService.removeByScenePointIds(Collections.singletonList(pointId));
        this.lambdaUpdate().set(InspectScenePointPO::getDeleted, true).eq(InspectScenePointPO::getId, pointId).update();
        log.info("删除现场点:{}", pointId);
    }

    @Override
    public ScenePointVO getScenePointDetail(Long objId) {
        InspectScenePointPO point = this.getById(objId);
        ScenePointVO vo = ObjectUtils.copying(point, ScenePointVO.class);
        vo.setAreaName(areaClientService.getAreaNamesByIds(point.getAreaId()));
        fillDeviceIds(Collections.singletonList(vo));
        return vo;
    }

    @Override
    public List<ScenePointIdTypeDTO> getScenePointIdTypes(InspectScenePointQueryParam param) {
        var poList = this.lambdaQuery()
                .select(InspectScenePointPO::getId, InspectScenePointPO::getName, InspectScenePointPO::getTypeId)
                .like(StringUtils.isNotBlank(param.getPointName()), InspectScenePointPO::getName, param.getPointName())
                .in(!CollectionUtils.isEmpty(param.getIds()), InspectScenePointPO::getId, param.getIds())
                .eq(ObjectUtils.isNotNull(param.getReceiveDepId()), InspectScenePointPO::getDepId, param.getReceiveDepId())
                .list();
        return poList.stream()
                .map(po -> new ScenePointIdTypeDTO(po.getId(), po.getName(),
                                                   Objects.nonNull(po.getTypeId()) ? po.getTypeId().toString() : ""))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScenePointVO> scenePointListAll(InspectScenePointQueryParam param) {
        var poList = this.lambdaQuery()
                .eq(ObjectUtils.isNotNull(param.getTypeId()), InspectScenePointPO::getTypeId, param.getTypeId())
                .eq(ObjectUtils.isNotNull(param.getReceiveDepId()), InspectScenePointPO::getDepId, param.getReceiveDepId())
                .like(StringUtils.isNotBlank(param.getPointName()), InspectScenePointPO::getName, param.getPointName())
                .like(StringUtils.isNotBlank(param.getAreaId()), InspectScenePointPO::getAreaId, param.getAreaId())
                .orderByDesc(InspectScenePointPO::getCreateTime)
                .orderByDesc(InspectScenePointPO::getId).list();
        if (CollectionUtils.isEmpty(poList)) {
            return Collections.emptyList();
        }
        Set<Long> areaIds = new HashSet<>();
        for (InspectScenePointPO inspectScenePointPO : poList) {
            if (StringUtils.isNotBlank(inspectScenePointPO.getAreaId())) {
                areaIds.addAll(Arrays.stream(inspectScenePointPO.getAreaId().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
        }
        var areaNameMap = areaClientService.getAreaNamesByIdsMap(areaIds);

        List<ScenePointVO> voList = new ArrayList<>();
        Optional.ofNullable(poList).orElse(Collections.emptyList()).forEach(point -> {
            ScenePointVO vo = ObjectUtils.copying(point, ScenePointVO.class);
            vo.setAreaName(areaClientService.convertAreaName(areaNameMap, point.getAreaId()));
            voList.add(vo);
        });
        fillDeviceIds(voList);
        return voList;
    }

    @Override
    public ScenePointVO scenePointDetailByName(String name) {
        InspectScenePointPO point = this.getOne(new LambdaQueryWrapper<InspectScenePointPO>()
                                                        .eq(InspectScenePointPO::getName, name)
                                                        .last("limit 1"));
        ScenePointVO vo = ObjectUtils.copying(point, ScenePointVO.class);
        vo.setAreaName(areaClientService.getAreaNamesByIds(point.getAreaId()));
        fillDeviceIds(Collections.singletonList(vo));
        return vo;
    }

    private void fillDeviceIds(List<ScenePointVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        List<Long> ids = voList.stream().map(ScenePointVO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return;
        }
        Map<Long, List<ScenePointDeviceBaseVO>> deviceMap = inspectScenePointDeviceService.mapDevicesByScenePointIds(ids);
        voList.forEach(vo -> {
            if (vo.getId() != null) {
                List<ScenePointDeviceBaseVO> deviceList = deviceMap.getOrDefault(vo.getId(), Collections.emptyList());
                vo.setDeviceList(deviceList);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        List<Long> pointIds = list.stream()
                .filter(Objects::nonNull)
                .map(o -> o instanceof Long ? (Long) o : ((Number) o).longValue())
                .collect(Collectors.toList());
        inspectScenePointDeviceService.removeByScenePointIds(pointIds);
        return super.removeByIds(list);
    }

    /**
     * excel批量导入现场点
     *
     * @param param       现场点数据
     * @param providerMap 下拉数据
     *
     * @return 错误数据
     */
    @Override
    public List<String> excelSave(List<ScenePointExcelTemplate> param, Map<String, ExcelColumnSelectionProvider> providerMap) {
        List<String> errMsg = new LinkedList<>();
        int i = BigDecimal.ZERO.intValue();
        for (ScenePointExcelTemplate batchImportExcelTemplate : param) {
            i++;
            try {
                InspectScenePointPO point = this.convertToModel(batchImportExcelTemplate, providerMap);
                String msg = this.checkPoint(point, i);
                if (StringUtils.isNotBlank(msg)) {
                    errMsg.add(msg);
                    continue;
                }
                this.save(point);
            } catch (DuplicateKeyException e) {
                errMsg.add("第" + i + "行，导入失败:现场点已经存在");
            }
        }
        return errMsg;
    }

    /**
     * 校验现场点
     *
     * @param point 现场点
     *
     * @return 错误信息
     */
    private String checkPoint(InspectScenePointPO point, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isBlank(point.getCodeNum())) {
            stringBuilder.append("第").append(i).append("行，现场点编码不能为空。");
        } else {
            InspectScenePointPO oldPO = this.baseMapper.selectOne(Wrappers.<InspectScenePointPO>lambdaQuery()
                                                                          .select(InspectScenePointPO::getId)
                                                                          .eq(InspectScenePointPO::getCodeNum, point.getCodeNum())
                                                                 );
            if (oldPO != null) {
                stringBuilder.append("第").append(i).append("行，现场点编码").append(point.getCodeNum()).append("已存在。");
            }
        }
        if (StringUtils.isBlank(point.getName())) {
            stringBuilder.append("第").append(i).append("行，现场点名称不能为空。");
        }
        if (point.getTypeId() == null) {
            stringBuilder.append("第").append(i).append("行，现场点类型不能为空。");
        }
        if (point.getDepId() == null) {
            stringBuilder.append("第").append(i).append("行，管理部门不能为空。");
        }
        if (point.getAreaId() == null) {
            stringBuilder.append("第").append(i).append("行，所属区域不能为空。");
        }
        if (StringUtils.isNotBlank(stringBuilder)) {
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * excel对象转现场点
     *
     * @param excelTemplate excel
     * @param providerMap   下拉信息
     *
     * @return 现场点
     */
    private InspectScenePointPO convertToModel(ScenePointExcelTemplate excelTemplate,
                                               Map<String, ExcelColumnSelectionProvider> providerMap
                                              ) {
        InspectScenePointPO point = new InspectScenePointPO();
        point.setId(IdWorker.getId());
        point.setCodeNum(excelTemplate.getCodeNum());
        point.setName(excelTemplate.getName());
        point.setLocation(excelTemplate.getLocation());

        //提供了下拉字典才进行设值
        if (Objects.nonNull(providerMap) && !providerMap.isEmpty()) {
            //部门转换
            ExcelColumnSelectionItem item = findMatchedItem(excelTemplate.getDepId(), ScenePointExcelTemplate.DEPARTMENT_TYPE_KEY, providerMap);
            point.setDepId(StringUtils.isNotBlank(item.getValue()) ? Long.parseLong(item.getValue()) : null);
            point.setDepName(item.getName());

            //区域转换
            item = findMatchedItem(excelTemplate.getAreaId(), ScenePointExcelTemplate.AREA_TYPE_KEY, providerMap);
            point.setAreaId(item.getValue());

            //现场点类型
            item = findMatchedItem(excelTemplate.getTypeId(), ScenePointExcelTemplate.POINT_TYPE_KEY, providerMap);
            point.setTypeId(StringUtils.isNotBlank(item.getValue()) ? Long.parseLong(item.getValue()) : null);//Excel只能选择一个
            point.setTypeName(item.getName());
        }
        return point;
    }

    /**
     * 根据提供的val在providerMap中查找item信息
     *
     * @param value       对比值
     * @param providerMap map
     *
     * @return item
     */
    private ExcelColumnSelectionItem findMatchedItem(String value, String type, Map<String, ExcelColumnSelectionProvider> providerMap) {
        //提取下拉信息并进行匹配
        ExcelColumnSelectionProvider excelColumnSelectionProvider = providerMap.get(type);
        for (ExcelColumnSelectionItem selectionItem : excelColumnSelectionProvider.selectionItems()) {
            if (selectionItem.getValue().equals(value)) {
                return selectionItem;
            }
        }

        throw new IllegalArgumentException("未找到匹配的信息");
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

        // 批量查询所有现场点（只查询areaId字段，提高效率）
        List<InspectScenePointPO> allScenePoints = this.list(
                Wrappers.<InspectScenePointPO>lambdaQuery()
                        .select(InspectScenePointPO::getAreaId)
                        .isNotNull(InspectScenePointPO::getAreaId)
                        .ne(InspectScenePointPO::getAreaId, "")
                                                            );

        // 统计每个区域的现场点数
        // areaId用逗号分隔（如"1,2,3"）
        for (InspectScenePointPO scenePoint : allScenePoints) {
            if (StringUtils.isBlank(scenePoint.getAreaId())) {
                continue;
            }

            // 解析逗号分隔的区域ID
            String[] areaIdArray = scenePoint.getAreaId().split(",");
            Set<Long> scenePointAreaIds = new HashSet<>();
            for (String areaIdStr : areaIdArray) {
                if (StringUtils.isNotBlank(areaIdStr)) {
                    try {
                        scenePointAreaIds.add(Long.parseLong(areaIdStr.trim()));
                    } catch (NumberFormatException e) {
                        log.warn("解析现场点区域ID失败: {}", areaIdStr, e);
                    }
                }
            }

            // 统计匹配的区域
            for (Long areaId : areaIds) {
                if (scenePointAreaIds.contains(areaId)) {
                    resultMap.put(areaId, resultMap.getOrDefault(areaId, 0L) + 1);
                }
            }
        }

        return resultMap;
    }
}
