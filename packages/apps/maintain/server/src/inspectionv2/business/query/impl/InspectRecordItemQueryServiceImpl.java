package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordItemQueryServiceImpl extends ServiceImpl<InspectRecordItemMapper, InspectRecordItemPO> implements InspectRecordItemQueryService {

    private final FileClient fileClient;

    @Override
    public List<InspectRecordItemVO> getListByRecordId(Long recordId) {

        List<InspectRecordItemPO> pos = this.lambdaQuery()
                .eq(InspectRecordItemPO::getRecordId, recordId)
                .list();
        List<InspectRecordItemVO> vos = ObjectUtils.copying(pos, InspectRecordItemVO.class);
        if (!vos.isEmpty()) {
            // 扩展文件信息
            vos.forEach(this::extendFileVOS);
        }

        return vos;
    }

    private void extendFileVOS(InspectRecordItemVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询巡检记录项文件异常", e);
        }

    }

    /**
     * 巡检记录项列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @Override
    public List<InspectRecordItemListVO> inspectRecordItemList(InspectRecordItemQueryParam param) {
        LambdaQueryWrapper<InspectRecordItemPO> wrapper = Wrappers.lambdaQuery();
        if (param != null && param.getRecordId() != null) {
            wrapper.eq(InspectRecordItemPO::getRecordId, param.getRecordId());
        }
        if (param != null && param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            wrapper.ge(InspectRecordItemPO::getCreateTime, param.getPeriod().getBegin())
                    .le(InspectRecordItemPO::getCreateTime, param.getPeriod().getEnd());
        }
        List<InspectRecordItemPO> poList = this.list(wrapper);
        return ConvertUtils.toList(InspectRecordItemListVO.class, poList);
    }

    @Override
    public List<InspectRecordItemDataMode> getInspectRecordItemSafetyData(Period period) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
        try {
            List<InspectRecordItemPO> poList = this.baseMapper.getRecordItemCollectData(SessionContextHolder.getRequester()
                                                                                                .getTenant(), period);
            if (CollectionUtils.isEmpty(poList)) {
                return Collections.emptyList();
            }
            return ObjectUtils.copying(poList, InspectRecordItemDataMode.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    @Override
    public List<InspectRecordItemStatisticsVO> getInspectRecordItemsStatistics(InspectRecordItemQueryParam param) {
        try {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
            return this.baseMapper.getInspectRecordItemsStatistics(param);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    /**
     * 批量获取巡检记录的记录项
     *
     * @param recordIdList 记录id集合
     *
     * @return map key:记录id，value:记录项集合
     */
    @Override
    public Map<Long, List<InspectRecordItemVO>> getListByRecordIdList(List<Long> recordIdList) {
        Map<Long, List<InspectRecordItemVO>> result = new HashMap<>();
        // 查询批量数据
        List<InspectRecordItemVO> recordItemVOList = this.baseMapper.getListByRecordIdList(recordIdList, null);
        if (!CollectionUtils.isEmpty(recordItemVOList)) {
            /* 扩展文件信息 */
            BusinessKeysParam businessKeysParam = new BusinessKeysParam();
            List<Long> fileBusinessKeyList = new ArrayList<>();
            recordItemVOList.forEach(recordItemVO -> fileBusinessKeyList.add(recordItemVO.getId()));
            businessKeysParam.setBusinessKeys(fileBusinessKeyList);
            Map<Long, List<FileVO>> fileItemIdMap = fileClient.getByBusinessKeys(businessKeysParam);
            if (fileItemIdMap != null && !fileItemIdMap.isEmpty()) {
                recordItemVOList.forEach(recordItemVO -> recordItemVO.setFileVOS(fileItemIdMap.get(recordItemVO.getId())));
            }
            return recordItemVOList.stream().collect(Collectors.groupingBy(InspectRecordItemVO::getRecordId));
        }

        return result;
    }

    @Override
    public Map<Long, List<InspectRecordItemVO>> getErrorItemByRecordIdList(List<Long> recordIds) {
        Map<Long, List<InspectRecordItemVO>> result = new HashMap<>();
        if (ListUtils.isEmpty(recordIds)) {
            return result;
        }
        // 查询批量数据
        List<InspectRecordItemVO> recordItemVOList = this.baseMapper.getListByRecordIdList(recordIds, 1);
        if (ListUtils.nonEmpty(recordItemVOList)) {
            recordItemListExtFile(recordItemVOList, fileClient);
            return recordItemVOList.stream().collect(Collectors.groupingBy(InspectRecordItemVO::getRecordId));
        }

        return result;
    }

    static void recordItemListExtFile(List<InspectRecordItemVO> recordItemVOList, FileClient fileClient) {
        BusinessKeysParam fileParam = new BusinessKeysParam();
        fileParam.setBusinessKeys(recordItemVOList.stream().map(InspectRecordItemVO::getId).collect(Collectors.toList()));
        Map<Long, List<FileVO>> fileMap = fileClient.getByBusinessKeys(fileParam);
        recordItemVOList.forEach(item -> item.setFileVOS(fileMap.get(item.getId())));
    }
}
