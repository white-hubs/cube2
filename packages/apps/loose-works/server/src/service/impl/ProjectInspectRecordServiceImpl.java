package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectInspectRecordMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectScheduleMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectInspectRecordParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectInspectRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectInspectRecordPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectInspectRecordService;
import com.guhecloud.cube.srv.gen.maintain.client.InspectRecordClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.loose.works.service.impl.ProjectScheduleServiceImpl.SMALLENFINEERING;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/11 17:11
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectInspectRecordServiceImpl extends ServiceImpl<ProjectInspectRecordMapper, ProjectInspectRecordPO> implements ProjectInspectRecordService {

    private final ProjectScheduleMapper projectScheduleMapper;
    private final FileRefClient fileRefClient;
    private final UserClient userClient;
    private final FileClient fileClient;
    private final InspectRecordClient inspectRecordClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitProjectInspectRecord(ProjectInspectRecordParam param) {
        Assert.isParameterError(Objects.isNull(param.getProjectScheduleId()), "工程监管ID不能为空");
        ProjectSchedulePO projectSchedulePO = projectScheduleMapper.selectById(param.getProjectScheduleId());
        Assert.isNotFound(Objects.isNull(projectSchedulePO), "所选工程不存在");
        ProjectInspectRecordPO build = ProjectInspectRecordPO.builder()
                .id(IdWorker.getId())
                .name(projectSchedulePO.getProjectName() + "临时巡查")
                .projectScheduleId(param.getProjectScheduleId())
                .result(param.getResult())
                .userId(SecurityContextHolder.getPrincipal().getId())
                .remarks(param.getRemarks())
                .build();
        this.save(build);
        if (CollectionUtils.isNotEmpty(param.getImage())) {
            FileRefCreateParam fileInduceParam = new FileRefCreateParam();
            fileInduceParam.setSceneCode(SMALLENFINEERING);
            fileInduceParam.setFileIdList(param.getImage());
            fileInduceParam.setBusinessKey(build.getId());
            fileRefClient.refInduce(fileInduceParam);
        }
        return build.getId();
    }

    @Override
    public Pagination<ProjectInspectRecordVO> getPageProjectInspectRecord(BaseQuery baseQuery) {
        LocalDateTime begin = Objects.nonNull(baseQuery.getPeriod()) ? baseQuery.getPeriod().getBegin() : null;
        LocalDateTime end = Objects.nonNull(baseQuery.getPeriod()) ? baseQuery.getPeriod().getEnd() : null;
        Page<ProjectInspectRecordPO> inspectRecordPOPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(baseQuery.getKeyword()), ProjectInspectRecordPO::getName, baseQuery.getKeyword())
                .between(Objects.nonNull(baseQuery.getPeriod()), ProjectInspectRecordPO::getCreateTime, begin, end)
                .orderByDesc(ProjectInspectRecordPO::getCreateTime)
                .page(PagingUtils.convert(baseQuery));
        if (Objects.isNull(inspectRecordPOPage) || CollectionUtils.isEmpty(inspectRecordPOPage.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> projectIds = new HashSet<>();
        for (ProjectInspectRecordPO record : inspectRecordPOPage.getRecords()) {
            if (Objects.nonNull(record.getUserId())) {
                userIds.add(record.getUserId());
            }
            if (Objects.nonNull(record.getProjectScheduleId())) {
                projectIds.add(record.getProjectScheduleId());
            }
        }
        Map<String, UserDTO> userMap;
        if (CollectionUtils.isNotEmpty(userIds)) {
            UserMapQuery userMapQuery = new UserMapQuery();
            userMapQuery.setUserIds(userIds);
            userMap = userClient.getTenantUserMap(userMapQuery);
        } else {
            userMap = new HashMap<>();
        }
        Map<Long, String> projectMap;
        if (CollectionUtils.isNotEmpty(projectIds)) {
            projectMap = Optional.ofNullable(projectScheduleMapper.selectList(Wrappers.<ProjectSchedulePO>lambdaQuery()
                                                                                      .in(ProjectSchedulePO::getId, projectIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(ProjectSchedulePO::getId, ProjectSchedulePO::getProjectName));

        } else {
            projectMap = new HashMap<>();
        }
        List<ProjectInspectRecordVO> recordVOList = inspectRecordPOPage.getRecords().stream()
                .map(record -> ProjectInspectRecordVO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .projectName(projectMap.get(record.getProjectScheduleId()))
                        .userName(Objects.nonNull(record.getUserId()) ? userMap.get(record.getUserId().toString()).getName() : "")
                        .completeTime(record.getCreateTime())
                        .result(record.getResult())
                        .build())
                .collect(Collectors.toList());
        return PagingUtils.convert(inspectRecordPOPage, recordVOList);
    }

    @Override
    public ProjectInspectRecordVO getProjectInspectRecordDetail(Long id) {
        ProjectInspectRecordPO projectInspectRecordPO = this.getById(id);
        if (Objects.isNull(projectInspectRecordPO)) {
            throw new ServiceException("巡查记录不存在");
        }
        ProjectSchedulePO projectSchedulePO = projectScheduleMapper.selectById(projectInspectRecordPO.getProjectScheduleId());
        UserDTO user = userClient.getUser(projectInspectRecordPO.getUserId());
        List<FileVO> fileVOList = fileClient.getByBusinessKey(projectInspectRecordPO.getId());
        return ProjectInspectRecordVO.builder()
                .id(projectInspectRecordPO.getId())
                .name(projectInspectRecordPO.getName())
                .projectName(Objects.nonNull(projectSchedulePO) ? projectSchedulePO.getProjectName() : null)
                .result(projectInspectRecordPO.getResult())
                .userName(Objects.nonNull(user) ? user.getName() : null)
                .remarks(projectInspectRecordPO.getRemarks())
                .completeTime(projectInspectRecordPO.getCreateTime())
                .imageFiles(fileVOList)
                .build();

    }

    @Override
    public Boolean isExitAbnormal(Long id) {
        List<ProjectInspectRecordPO> list = this.lambdaQuery().eq(ProjectInspectRecordPO::getProjectScheduleId, id).list();
        if (CollectionUtils.isNotEmpty(list)) {
            return Boolean.TRUE;
        }
        return inspectRecordClient.isExitAbnormal(id, "SMALL_ENGINEERING");
    }

}
