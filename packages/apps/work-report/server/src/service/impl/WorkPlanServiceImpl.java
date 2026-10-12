package com.guhecloud.cube.srv.gen.reportmanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.WorkPlanMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.WorkPlanTypeMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlan;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlanType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration.ReportFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration.WorkPlanStatusEnum;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.WorkPlanService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanStatVO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：工作安排服务层
 *
 * @author Appet
 * @date 2024-01-04 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPlanServiceImpl extends ServiceImpl<WorkPlanMapper, WorkPlan> implements WorkPlanService {
    private final UserClient userClient;
    private final FileClient fileClient;
    private final WorkPlanTypeMapper workPlanTypeMapper;
    private final FileOperateClient fileOperateClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WorkPlanParam param) {
        WorkPlan workPlan = ObjectUtils.copying(param, WorkPlan.class);
        //扩充发布人
        this.extendWorkPlan(workPlan);
        this.baseMapper.insert(workPlan);
        return workPlan.getId();
    }

    private void extendWorkPlan(WorkPlan workPlan) {
        UserDTO user;
        if (workPlan.getPublisherId() != null) {
            user = userClient.getUser(workPlan.getPublisherId());
        } else {
            Long userId = SecurityContextHolder.getPrincipal().getId();
            user = userClient.getUser(userId);
        }
        if (user != null) {
            workPlan.setPublisherId(user.getId());
            workPlan.setPublisherName(user.getName());
            workPlan.setDeptId(user.getOrganization().getId());
            workPlan.setDeptName(user.getOrganization().getName());
            workPlan.setPhone(user.getMobile());
        }
        workPlan.setPublishTime(LocalDateTime.now());
        //前端传状态 默认发布
        if (StringUtils.isEmpty(workPlan.getStatus())) {
            workPlan.setStatus(WorkPlanStatusEnum.OVER.name());
        }
        if (StringUtils.isNotBlank(workPlan.getFileIds())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdStr(workPlan.getFileIds());
            fileInduceParam.setSceneCode(ReportFileSceneCodeEnum.WORK_PLAN.getCode());
            fileInduceParam.setBusinessKey(workPlan.getId());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, WorkPlanParam param) {
        WorkPlan workPlan = this.baseMapper.selectById(id);
        Assert.isNotFound(workPlan, "未查询到相关信息");
        if (!StringUtils.equals(workPlan.getFileIds(), param.getFileIds())) {
            FileParam fileParam = new FileParam();
            fileParam.setIds(Arrays.stream(workPlan.getFileIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
            fileOperateClient.physicalDeleteByIds(fileParam);
        }
        ObjectUtils.copying(param, workPlan);
        this.extendWorkPlan(workPlan);
        this.baseMapper.updateById(workPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public WorkPlanVO getById(Long id) {
        WorkPlan workPlan = this.baseMapper.selectById(id);
        WorkPlanVO workPlanVO = ObjectUtils.copying(workPlan, WorkPlanVO.class);
        //文件
        if (StringUtils.isNotEmpty(workPlanVO.getFileIds())) {
            List<Long> fileIds = Arrays.stream(workPlanVO.getFileIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<FileVO> fileList = fileClient.getByIds(fileIds);
            workPlanVO.setFileList(fileList);
        }
        //类型
        WorkPlanType workPlanType = workPlanTypeMapper.selectById(workPlan.getType());
        if (workPlanType != null) {
            workPlanVO.setTypeName(workPlanType.getName());
        }
        return workPlanVO;
    }

    @Override
    public Pagination<WorkPlanVO> getPage(WorkPlanQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<WorkPlan> page = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                        q.like(WorkPlan::getTitle, param.getKeyword()).or()
                                .like(WorkPlan::getContent, param.getKeyword()).or()
                                .like(WorkPlan::getPublisherName, param.getKeyword()))
                .eq(param.getPublisherId() != null, WorkPlan::getPublisherId, param.getPublisherId())
                .eq(param.getDeptId() != null, WorkPlan::getDeptId, param.getDeptId())
                .eq(param.getType() != null, WorkPlan::getType, param.getType())
                .eq(StringUtils.isNotEmpty(param.getStatus()), WorkPlan::getStatus, param.getStatus())
                .between(period !=
                         null, WorkPlan::getPublishTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(WorkPlan::getPublishTime)
                .page(PagingUtils.convert(param));
        Pagination<WorkPlanVO> pagination = PagingUtils.convert(page, WorkPlanVO.class);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            List<WorkPlanType> workPlanTypes = workPlanTypeMapper.selectList(null);
            Map<Long, String> typeMap = Optional.ofNullable(workPlanTypes).orElse(Collections.emptyList())
                    .stream().collect(Collectors.toMap(WorkPlanType::getId, t -> t.getName()));
            pagination.getRecords().stream().forEach(p -> {
                p.setTypeName(typeMap.get(p.getType()));
            });
        }
        return pagination;
    }

    @Override
    public List<WorkPlanVO> getList(WorkPlanQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        List<WorkPlan> list = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                        q.like(WorkPlan::getTitle, param.getKeyword()).or()
                                .like(WorkPlan::getContent, param.getKeyword()).or()
                                .like(WorkPlan::getPublisherName, param.getKeyword()))
                .eq(param.getPublisherId() != null, WorkPlan::getPublisherId, param.getPublisherId())
                .eq(param.getDeptId() != null, WorkPlan::getDeptId, param.getDeptId())
                .eq(param.getType() != null, WorkPlan::getType, param.getType())
                .eq(StringUtils.isNotEmpty(param.getStatus()), WorkPlan::getStatus, param.getStatus())
                .between(period !=
                         null, WorkPlan::getPublishTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(WorkPlan::getPublishTime)
                .list();
        List<WorkPlanVO> workPlanVOList = ObjectUtils.copying(list, WorkPlanVO.class);
        if (!CollectionUtils.isEmpty(workPlanVOList)) {
            List<WorkPlanType> workPlanTypes = workPlanTypeMapper.selectList(null);
            Map<Long, String> typeMap = Optional.ofNullable(workPlanTypes).orElse(Collections.emptyList())
                    .stream().collect(Collectors.toMap(WorkPlanType::getId, t -> t.getName()));
            workPlanVOList.stream().forEach(p -> {
                p.setTypeName(typeMap.get(p.getType()));
            });
        }
        return workPlanVOList;
    }

    @Override
    public List<WorkPlanStatVO> getWorkPlanStatByPerson(WorkPlanQueryParam param) {
        return this.baseMapper.getWorkPlanStatByPerson(param);
    }

    @Override
    public List<WorkPlanStatVO> getWorkPlanStatByDept(WorkPlanQueryParam param) {
        return this.baseMapper.getWorkPlanStatByDept(param);
    }
}