package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.TimeCycleTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.AddressResolvingHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ProjectPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.ProjectUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionMaterialService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectAreaCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.ProjectPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionProjectServiceImpl implements BusinessDisinfectionProjectService {
    private final DisinfectionProjectService disinfectionProjectService;
    private final DisinfectionGroupService groupService;
    private final DisinfectionAreaService areaService;
    private final DisinfectionKeyPlaceService keyPlaceService;
    private final DisinfectionPlanService planService;
    private final DisinfectionMaterialService materialService;
    private final DisinfectionTaskService taskService;
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionDataTypeService disinfectionDataTypeService;
    private final DisinfectionFileHelper disinfectionFileHelper;

    @Override
    public boolean insertProject(ProjectUpsertParam param) {
        //校验项目数据合法性
        validateProject(param);
        DisinfectionProject disinfectionProject = ObjectUtils.copying(param, DisinfectionProject.class);
        //设置项目编号
        disinfectionProject.setProjectCode(String.valueOf(IdWorker.getId()));
        //设置消杀项目状态
        setProjectStatus(disinfectionProject);
        if (Objects.nonNull(param.getImage())) {
            disinfectionFileHelper.induceFile(DinsinfectionFileInduceSceneCodeEnum.PROJECT, param.getImage());
        }
        //设置项目类型名称
        disinfectionProject.setProjectTypeName(disinfectionDataTypeService.getTypeName(disinfectionProject.getProjectTypeId()));
        disinfectionProject.setDistrict(AddressResolvingHelper.getAddressDistrict(param.getDisinfectionLocationName()));
        return disinfectionProjectService.save(disinfectionProject);
    }

    @Transactional
    @Override
    public boolean delProject(long projectId) {
        //有计划或者，任务不允许删除
        boolean hasPlan = planService.hasPlan(projectId);
        boolean hasTask = taskService.hasTask(projectId);
        Assert.isNotAcceptable(hasTask || hasPlan, "该项目存在关联的计划或者任务，无法删除！");
        //删除项目
        boolean removeProject = disinfectionProjectService.removeById(projectId);
        log.info("项目id:{},删除结果{}", projectId, removeProject);
        //删除项目下的小组
        groupService.removeByProjectId(projectId);
        log.info("项目id:{},删除项目下的小组", projectId);
        //删除项目下区域
        areaService.removeByProjectId(projectId);
        log.info("项目id:{},删除项目下的区域", projectId);
        //删除项目下的重点场所
        keyPlaceService.removeByProjectId(projectId);
        log.info("项目id:{},删除项目下的重点场所", projectId);
        //删除项目下的资料
        materialService.removeByProjectId(projectId);
        log.info("项目id:{},删除项目下的资料", projectId);
        return removeProject;
    }

    @Transactional
    @Override
    public boolean updateProject(long id, ProjectUpsertParam param) {
        //查询项目是否存在
        DisinfectionProject disinfectionProject = disinfectionProjectService.getById(id);
        Assert.parameterIsNull(disinfectionProject, "项目不存在");
        if (!disinfectionProject.getProjectName().equals(param.getProjectName())) {
            //如果有名称的修改，需要校验是否存在该名称
            validateProject(param);
        }
        ObjectUtils.copying(param, disinfectionProject);
        //设置项目状态
        setProjectStatus(disinfectionProject);
        if (Objects.nonNull(param.getImage())) {
            disinfectionFileHelper.induceFile(DinsinfectionFileInduceSceneCodeEnum.PROJECT, param.getImage());
        }
        //设置项目名称
        disinfectionProject.setProjectTypeName(disinfectionDataTypeService.getTypeName(disinfectionProject.getProjectTypeId()));
        disinfectionProject.setDistrict(AddressResolvingHelper.getAddressDistrict(param.getDisinfectionLocationName()));
        return disinfectionProjectService.updateById(disinfectionProject);
    }

    @Override
    public Pagination<ProjectPageVO> pageProject(ProjectPageParam param) {
        LambdaQueryWrapper<DisinfectionProject> projectLambdaQueryWrapper = buildPageQueryWrapper(param);
        Page<DisinfectionProject> projectPage = disinfectionProjectService.page(PagingUtils.convert(param), projectLambdaQueryWrapper);
        Pagination<ProjectPageVO> voPagination = PagingUtils.convert(projectPage, ProjectPageVO.class);
        //设置负责人名称
        List<ProjectPageVO> records = voPagination.getRecords();
        if (ListUtils.nonEmpty(records)) {
            List<Long> leaderIds = ListUtils.convertItem(records, ProjectPageVO::getLeaderId);
            Map<Long, String> leaderNameMap = disinfectionUserHelper.getUserNameMap(leaderIds);
            records
                    .forEach(record -> record.setLeaderName(leaderNameMap.getOrDefault(record.getLeaderId(), "")));
        }
        return voPagination;
    }

    @Override
    public ProjectInfoVO detailProject(long projectId) {
        DisinfectionProject project = disinfectionProjectService.getById(projectId);
        ProjectInfoVO vo = ObjectUtils.copying(project, ProjectInfoVO.class);
        //设置项目负责人名称
        vo.setLeaderName(disinfectionUserHelper.getUserName(project.getLeaderId()));
        if (Objects.nonNull(project.getImage())) {
            vo.setProjectImage(disinfectionFileHelper.getFileById(project.getImage()));
        }
        return vo;
    }

    @Override
    public List<ProjectAreaCountVO> projectAreaCount() {
        //查询所有项目
        List<DisinfectionProject> projects = disinfectionProjectService.list();
        if (ListUtils.isEmpty(projects)) {
            return Collections.emptyList();
        }
        //查询项目区域个数
        Map<Long, Long> projectAreaCountMap = areaService.countMapByProjectIds(ListUtils.convertItem(projects, DisinfectionProject::getId));
        return ListUtils.convertItem(projects, project -> {
            ProjectAreaCountVO projectAreaCountVO = new ProjectAreaCountVO();
            projectAreaCountVO.setProjectId(project.getId());
            projectAreaCountVO.setProjectName(project.getProjectName());
            projectAreaCountVO.setCount(projectAreaCountMap.getOrDefault(project.getId(), 0L));
            return projectAreaCountVO;
        });
    }

    @Override
    public List<ProjectPageVO> listAll(ProjectPageParam param) {
        List<DisinfectionProject> disinfectionProjects = disinfectionProjectService.list(buildPageQueryWrapper(param)
                                                                                        );
        if (CollectionUtils.isEmpty(disinfectionProjects)) {
            return new ArrayList<>();
        }
        List<Long> leaderIds = new ArrayList<>();
        List<Long> fileIds = new ArrayList<>();
        for (DisinfectionProject disinfectionProject : disinfectionProjects) {
            if (Objects.nonNull(disinfectionProject.getLeaderId())) {
                leaderIds.add(disinfectionProject.getLeaderId());
            }
            if (Objects.nonNull(disinfectionProject.getImage())) {
                fileIds.add(disinfectionProject.getImage());
            }
        }
        Map<Long, FileVO> fileVOMap;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileVOMap = Optional.ofNullable(disinfectionFileHelper.getFiles(fileIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));
        } else {
            fileVOMap = new HashMap<>();
        }
        Map<Long, String> leaderNameMap = disinfectionUserHelper.getUserNameMap(leaderIds);
        return disinfectionProjects.stream().map(t -> {
            ProjectPageVO projectPageVO = ObjectUtils.copying(t, ProjectPageVO.class);
            projectPageVO.setLeaderName(leaderNameMap.getOrDefault(t.getLeaderId(), ""));
            projectPageVO.setProjectImage(fileVOMap.get(t.getImage()));
            return projectPageVO;
        }).collect(Collectors.toList());
    }

    private LambdaQueryWrapper<DisinfectionProject> buildPageQueryWrapper(ProjectPageParam param) {
        LambdaQueryWrapper<DisinfectionProject> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            //如果关键词不为空，则模糊匹配项目名，业主单位，负责人名称
            queryWrapper.and(q -> {
                q.like(DisinfectionProject::getProjectName, param.getKeyword())
                        .or()
                        .like(DisinfectionProject::getCompany, param.getKeyword());
                //根据名称模糊查找满足的用户信息
                List<Long> userIds = disinfectionUserHelper.getUserIdsByName(param.getKeyword());
                if (ListUtils.nonEmpty(userIds)) {
                    //需要请求ouaa查询满足条件的userId
                    q.or()
                            .in(DisinfectionProject::getLeaderId, userIds);
                }

            });
        }
        if (Objects.nonNull(param.getStatus())) {
            queryWrapper.eq(DisinfectionProject::getStatus, param.getStatus());
        }
        Pair<Integer, Integer> enumByValue = TimeCycleTypeEnum.getEnumByValue(param.getTypeEnum());
        queryWrapper.eq(StringUtils.isNotBlank(param.getArea()), DisinfectionProject::getDistrict, param.getArea())
                .apply(Objects.nonNull(enumByValue.getLeft()), "TIMESTAMPDIFF( MONTH, begin_time, end_time ) > {0}", enumByValue.getLeft())
                .apply(Objects.nonNull(enumByValue.getRight()), "TIMESTAMPDIFF( MONTH, begin_time, end_time ) <= {0}", enumByValue.getRight());
        return queryWrapper;
    }

    private void setProjectStatus(DisinfectionProject disinfectionProject) {
        LocalDateTime beginTime = disinfectionProject.getBeginTime();
        LocalDateTime endTime = disinfectionProject.getEndTime();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //表当前时间和开始时间比较，如果当前时间在开始时间之后,在结束时间之前，则设置消杀项目状态为进行中
        if (now.isAfter(beginTime) && now.isBefore(endTime)) {
            disinfectionProject.setStatus(DisinfectionProjectStatusEnum.PROCESSING);
        }
        //如果在开始时间之前，则设置消杀项目状态为未开始
        if (now.isBefore(beginTime)) {
            disinfectionProject.setStatus(DisinfectionProjectStatusEnum.WAITING);
        }

        //如果在结束时间之后，则设置消杀项目状态为已结束
        if (now.isAfter(endTime)) {
            disinfectionProject.setStatus(DisinfectionProjectStatusEnum.COMPLETED);
        }
    }

    private void validateProject(ProjectUpsertParam param) {
        Assert.parameterIsNull(param, "校验的项目数据不能为空！");
        String projectName = param.getProjectName();
        List<DisinfectionProject> projectList = disinfectionProjectService.listInNames(Collections.singletonList(projectName));
        Assert.isConflict(CollectionUtils.isNotEmpty(projectList), "项目名称已存在,请勿重复添加！");
    }
}
