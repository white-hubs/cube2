package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectFeedbackMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.AbnormalProjectReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectFeedbackVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectFeedbackPO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectFeedbackService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.loose.works.service.impl.ProjectScheduleServiceImpl.SMALLENFINEERING;

/**
 * @author wql
 * @describe 工程反馈
 * @date 2023/9/14 18:41
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectFeedbackServiceImpl extends ServiceImpl<ProjectFeedbackMapper, ProjectFeedbackPO> implements ProjectFeedbackService {
    private final UserAndOrgService userAndOrgService;
    private final ProjectFeedbackMapper projectFeedbackMapper;
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;

    @Override
    public List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailList(Long scheduleId, String feedbackType) {
        List<ProjectFeedbackPO> list = this.getFeedbackList(scheduleId, feedbackType);
        if (ObjectUtils.isNotEmpty(list)) {
            List<ProjectDetailVO.FeedbackDetail> feedbackDetails = new ArrayList<>();
            list.parallelStream().forEach(po -> {
                feedbackDetails.add(ProjectDetailVO.FeedbackDetail.builder()
                                            .username(userAndOrgService.getUserByUsername(po.getCreateBy()).getName())
                                            .feedbackTime(po.getCreateTime())
                                            .schedule(po.getSchedule())
                                            .result(po.getAcceptanceResult())
                                            .build());
            });
            return feedbackDetails;
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailAll(Long scheduleId, String feedbackType) {
        List<ProjectFeedbackPO> feedbackList = this.getFeedbackList(scheduleId, feedbackType);
        List<ProjectDetailVO.FeedbackDetail> vos = new ArrayList<>();
        Set<String> userNameList = new HashSet<>();
        Set<Long> fileIds = new HashSet<>();
        for (ProjectFeedbackPO po : feedbackList) {
            userNameList.add(po.getCreateBy());
            if (ObjectUtils.isNotEmpty(po.getFiles())) {
                fileIds.addAll(Arrays.stream(po.getFiles().split(",")).map(Long::parseLong).collect(Collectors.toSet()));
            }
        }
        Map<String, UserDTO> userNameMap = userAndOrgService.getUserByUserNames(userNameList);
        Map<Long, FileVO> fileVOMap;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileVOMap = Optional.ofNullable(fileClient.getByIds(new ArrayList<>(fileIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));

        } else {
            fileVOMap = new HashMap<>();
        }
        if (ObjectUtils.isNotEmpty(feedbackList)) {
            feedbackList.forEach(po -> {
                UserDTO user = userNameMap.getOrDefault(po.getCreateBy(), new UserDTO());
                ProjectDetailVO.FeedbackDetail feedbackDetail = ConvertUtils.convert(ProjectDetailVO.FeedbackDetail.class, po);
                feedbackDetail.setUsername(user.getName());
                feedbackDetail.setFeedbackTime(po.getCreateTime());
                feedbackDetail.setResult(po.getAcceptanceResult());
                feedbackDetail.setFeedbackDepId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
                feedbackDetail.setFeedbackDepName(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getName());
                if (ObjectUtils.isNotEmpty(po.getFiles())) {
                    List<FileVO> fileVOList = Arrays.stream(po.getFiles().split(","))
                            .filter(t -> fileVOMap.containsKey(Long.parseLong(t)))
                            .map(t -> fileVOMap.get(Long.parseLong(t)))
                            .collect(Collectors.toList());
                    feedbackDetail.setProblemFileList(fileVOList);
                }
                vos.add(feedbackDetail);
            });
        }
        return vos;
    }

    @Override
    public List<ProjectFeedbackPO> getFeedbackList(Long scheduleId, String feedbackType) {
        return this.lambdaQuery().eq(ObjectUtils.isNotNull(scheduleId), ProjectFeedbackPO::getScheduleId, scheduleId)
                .eq(StringUtils.isNotBlank(feedbackType), ProjectFeedbackPO::getOperationType, feedbackType)
                .orderByAsc(ProjectFeedbackPO::getSchedule).orderByAsc(ProjectFeedbackPO::getCreateTime).list();
    }

    @Override
    public List<ProjectFeedbackPO> getCheckAcceptList(Long scheduleId, String feedbackType) {
        return this.lambdaQuery().eq(ObjectUtils.isNotNull(scheduleId), ProjectFeedbackPO::getScheduleId, scheduleId)
                .eq(StringUtils.isNotBlank(feedbackType), ProjectFeedbackPO::getOperationType, feedbackType)
                .orderByDesc(ProjectFeedbackPO::getCreateTime).list();
    }

    @Override
    public Long abnormalProjectNum(Period period) {
        return projectFeedbackMapper.abnormalProjectNum(period);
    }

    @Override
    public List<AbnormalProjectReportVO> selectAbnormalProject(Period period) {
        return projectFeedbackMapper.selectAbnormalProject(period);
    }

    @Override
    public ProjectFeedbackVO getFeedBackById(Long id) {
        ProjectFeedbackPO projectFeedbackPO = this.getById(id);
        ProjectFeedbackVO projectFeedbackVO = ConvertUtils.convert(ProjectFeedbackVO.class, projectFeedbackPO);
        if (ObjectUtils.isNotEmpty(projectFeedbackPO.getFiles())) {
            String[] fileIds = projectFeedbackPO.getFiles().split(",");
            List<Long> fileIdSet = new ArrayList<>();
            for (String fileId : fileIds) {
                fileIdSet.add(Long.parseLong(fileId));
            }
            List<FileVO> fileVOList = fileClient.getByIds(fileIdSet);
            projectFeedbackVO.setProblemFileList(fileVOList);
        }
        return projectFeedbackVO;
    }

    @Override
    public void saveFeedback(ProjectFeedbackPO entity) {
        //文件持久化
        if (StringUtils.isNotBlank(entity.getFiles())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setBusinessKey(entity.getId());
            fileInduceParam.setFileIdStr(entity.getFiles());
            fileInduceParam.setSceneCode(SMALLENFINEERING);
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        super.save(entity);
    }
}
