package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyWeedOutPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyAllotMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyWeedOutMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyWeedOutRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyWeedOut;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyWeedOutRelation;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.HandleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.AssemblyClassifyService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyHistoryService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyWeedOutService;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.FilesUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 清理报废服务实现类
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyWeedOutServiceImpl extends ServiceImpl<PropertyWeedOutMapper, PropertyWeedOut> implements PropertyWeedOutService {
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final PropertyAllotMapper propertyAllotMapper;
    private final PropertyWeedOutRelationMapper propertyWeedOutRelationMapper;
    private final UserClientService userClientService;
    private final AssemblyClassifyService assemblyClassifyService;
    private final FileRefClient fileRefClient;
    private final PropertyHistoryService propertyHistoryService;

    /**
     * 通过ID查询详情
     *
     * @param id 报废记录id
     *
     * @return 报废详情
     */
    @Override
    public PropertyWeedOutPageVO queryById(Long id) {
        PropertyWeedOut weedOut = this.baseMapper.selectById(id);
        PropertyWeedOutPageVO vo = ConvertUtils.convert(PropertyWeedOutPageVO.class, weedOut);
        vo.setPropertyDetailed(this.baseMapper.propertyDetailed(vo.getWeedOutNumbers(), null));
        vo.setWeedOutUserName(userClientService.getUser(vo.getWeedOutUserId()));
        AssemblyClassifyVO classify = assemblyClassifyService.queryById(vo.getCauseId());
        if (ObjectUtils.isNotEmpty(classify)) {
            vo.setCause(classify.getName());
        }
        for (PropertyDetailedVO ex : vo.getPropertyDetailed()) {
            AllotPageVO allot = propertyAllotMapper.queryByPropertyId(ex.getId());
            if (ObjectUtils.isNotEmpty(allot)) {
                ex.setReceiveDepName(allot.getReceiveDepName());
                ex.setReceiveUserName(userClientService.getUser(allot.getReceiveUserId()));
            }
        }
        return vo;
    }

    /**
     * 分页查询报废信息
     *
     * @param param 筛选条件
     *
     * @return 报废信息
     */
    @Override
    public Pagination<PropertyWeedOutPageVO> getPropertyWeedOutPage(PropertyWeedOutPageParam param) {
        Page<PropertyWeedOutPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.getPropertyWeedOutPage(page, param);
        Pagination<PropertyWeedOutPageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        for (PropertyWeedOutPageVO ex : pagination.getRecords()) {
            ex.setWeedOutUserName(userClientService.getUser(ex.getWeedOutUserId()));
            AssemblyClassifyVO classify = assemblyClassifyService.queryById(ex.getCauseId());
            if (ObjectUtils.isNotEmpty(classify)) {
                ex.setCause(classify.getName());
            }
            ex.setPropertyDetailed(baseMapper.propertyDetailed(ex.getWeedOutNumbers(), param.getKeyword()));
        }
        return pagination;
    }

    /**
     * 新增-
     *
     * @param param 信息
     *
     * @return 结果
     */
    @Override
    public Long insert(PropertyWeedOutParam param) {
        log.info("新增报废数据");
        PropertyWeedOut weedOut = this.getWeedOut(param);
        this.baseMapper.insert(weedOut);
        if (ObjectUtils.isNotEmpty(param.getFiles())) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(weedOut.getId(), param.getFiles());
            fileRefClient.saveBatch(files);
        }
        log.info("新增资产关联");
        for (Long ex : param.getPropertyIds()) {
            PropertyWeedOutRelation relation = new PropertyWeedOutRelation();
            relation.setPropertyId(ex);
            relation.setWeedOutId(weedOut.getId());
            relation.setWeedOutNumbers(weedOut.getWeedOutNumbers());
            propertyWeedOutRelationMapper.insert(relation);
            //            propertyHistoryService.insertByIds(param.getPropertyIds(), HandleTypeEnum.WEED_OUT, "报废了");
        }
        return weedOut.getId();
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Boolean update(WorkflowRelevance param) {
        PropertyWeedOut weedOut = baseMapper.selectById(param.getPropertyId());
        weedOut.setProcessInstanceId(param.getProcessInstanceId());
        return baseMapper.updateById(weedOut) > 0;
    }

    @Override
    public Boolean submit(ProcessCompletedMessage message) {
        PropertyWeedOut weedOut = baseMapper.selectOne(new LambdaQueryWrapper<PropertyWeedOut>()
                                                               .eq(PropertyWeedOut::getProcessInstanceId, message.getProcessInstanceId()));
        weedOut.setStatus(message.getResult().name());
        baseMapper.updateById(weedOut);
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            propertyDetailedMapper.updateByWeedOutNumbers(weedOut.getWeedOutNumbers());
            List<PropertyDetailedVO> detailedList = this.baseMapper.propertyDetailed(weedOut.getWeedOutNumbers(), null);
            List<Long> ids = detailedList.stream().map(PropertyDetailedVO::getId).collect(Collectors.toList());
            propertyHistoryService.insertByIds(ids, HandleTypeEnum.WEED_OUT, "报废了");
        }
        return true;
    }

    public PropertyWeedOut getWeedOut(PropertyWeedOutParam param) {
        PropertyWeedOut weedOut = new PropertyWeedOut();
        weedOut.setManageTime(param.getManageTime());
        weedOut.setCauseId(param.getCauseId());
        weedOut.setOverdue(param.getOverdue());
        weedOut.setRemarks(param.getRemarks());
        if (ObjectUtils.isNotEmpty(param.getWeedOutUserId()) && !param.getWeedOutUserId().isEmpty()) {
            Object userId = param.getWeedOutUserId().getFirst();
            weedOut.setWeedOutUserId(Long.parseLong(userId.toString()));
        }
        weedOut.setStatus(FlowResult.UNDER.name());
        weedOut.setWeedOutNumbers(OperateType.getNumbers(OperateType.WEED_OUT));
        return weedOut;
    }
}
