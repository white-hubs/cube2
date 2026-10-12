package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.AnnouncementCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.AnnouncementStatusCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.ModelTypeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.SubmitAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.dao.AnnouncementMapper;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.dao.ModelTypeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.AnnouncementStatusEnum;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.ContentFileSceneCode;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.InformationTypeEnum;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.ModelTypeVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.ModelType;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.NoticeConfigCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command.SubmitNoticeConfigCommand;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementCommandServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementCommandService {
    private static final Integer FALSE = 0;
    private final NoticeConfigCommandService noticeConfigCommandService;
    private final FileRefClient fileRefClient;
    private final ModelTypeMapper modelTypeMapper;
    private final MessageSendClient messageSendClient;
    private static final String MESSAGE_SCENE_CODE = "TECHNICAL_BULLETIN_COMMON_NOTIFY";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SubmitAnnouncementCommand command) {

        // 新增公告
        long id = IdWorker.getId();
        Announcement announcement = ObjectUtils.copying(command, Announcement.class);
        announcement.setNoticeTime(DateUtils.now());
        announcement.setId(id);
        announcement.setViewed(FALSE);
        announcement.setStatus(AnnouncementStatusEnum.DISPLAY.name());
        announcement.setIsTopping(FALSE);
        this.buildFiles(announcement.getId(), command.getFileIds());

        // 新增通知人配置
        this.noticeConfigCommandService.save(command.getNoticeConfigCommands(), announcement.getId());

        this.baseMapper.insert(announcement);

        //消息发送
        makeMsg(command.getNoticeConfigCommands(), announcement);

    }

    @Override
    public void updateAnnouncement(Long id, SubmitAnnouncementCommand command) {
        Announcement announcement = this.getById(id);
        ObjectUtils.copying(command, announcement);
        this.updateById(announcement);
        fileRefClient.removeByBusinessKeyAndScene(announcement.getId(), ContentFileSceneCode.CONTENT_ANNOUNCEMENT.getCode());
        this.buildFiles(announcement.getId(), command.getFileIds());
        List<NoticeConfig> list = noticeConfigCommandService.list(Wrappers.<NoticeConfig>lambdaQuery()
                                                                          .eq(NoticeConfig::getBusinessKey, id));
        List<Long> collect = list.stream().map(NoticeConfig::getId).collect(Collectors.toList());
        noticeConfigCommandService.removeByIds(collect);
        this.noticeConfigCommandService.save(command.getNoticeConfigCommands(), announcement.getId());
        //消息发送
        makeMsg(command.getNoticeConfigCommands(), announcement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void removeBath(List<Long> ids) {
        ids.forEach(s -> {
            DatabaseContextHolder.softDelete(this, s);
        });
    }

    @Override
    public void add(ModelTypeCommand command) {
        ModelType copying = ObjectUtils.copying(command, ModelType.class);
        copying.setOneLevelType(Objects.nonNull(command.getOneLevelType()) ? command.getOneLevelType().name() : null);
        modelTypeMapper.insert(copying);
    }

    @Override
    public void updateModelType(Long id, ModelTypeCommand command) {
        ModelType modelType = modelTypeMapper.selectById(id);
        ObjectUtils.copying(command, modelType);
        modelTypeMapper.updateById(modelType);
    }

    @Override
    public void removeModelType(Long id) {
        modelTypeMapper.deleteById(id);
    }

    @Override
    public List<ModelTypeVo> getList(String modelType, String keyword) {

        List<ModelType> modelTypes = modelTypeMapper.selectList(Wrappers.<ModelType>lambdaQuery()
                                                                        .eq(ModelType::getModelType, modelType)
                                                                        .like(StringUtils.isNotEmpty(keyword), ModelType::getTypeName, keyword));
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(modelTypes)) {
            return new ArrayList<>();
        }
        List<ModelTypeVo> result = new ArrayList<>();
        for (ModelType type : modelTypes) {
            ModelTypeVo modelTypeVo = ObjectUtils.copying(type, ModelTypeVo.class);
            if (StringUtils.isNotBlank(type.getOneLevelType())) {
                modelTypeVo.setOneLevelType(InformationTypeEnum.valueOf(type.getOneLevelType()));
            }
            result.add(modelTypeVo);
        }
        return result;
    }

    @Override
    public List<ModelType> getLevel(String modelType) {
        return modelTypeMapper.selectList(Wrappers.<ModelType>lambdaQuery()
                                                  .eq(ModelType::getModelType, modelType)
                                                  .eq(ModelType::getOneLevelType, Arrays.stream(InformationTypeEnum.values())
                                                          .map(InformationTypeEnum::name).collect(Collectors.toList())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAnnouncementHideAndTop(List<AnnouncementStatusCommand> listCommands) {
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(listCommands)) {
            throw new RuntimeException("请先勾选数据数据");
        }
        Set<Long> idList = new HashSet<>();
        Map<Long, AnnouncementStatusCommand> announcementMap = new HashMap<>();
        for (AnnouncementStatusCommand listCommand : listCommands) {
            idList.add(listCommand.getId());
            announcementMap.put(listCommand.getId(), listCommand);
        }
        List<Announcement> list = this.list(Wrappers.<Announcement>lambdaQuery()
                                                    .in(Announcement::getId, idList)
                                           );
        for (Announcement announcement : list) {
            if (announcementMap.containsKey(announcement.getId())) {
                AnnouncementStatusCommand command = announcementMap.get(announcement.getId());
                announcement.setIsTopping(command.getIsTopping() ? 1 : 0);
                if (StringUtils.isNotBlank(command.getStatus())) {
                    announcement.setStatus(command.getStatus());
                }
            }
        }
        this.updateBatchById(list);
    }

    private void buildFiles(Long id, Collection<Long> fileIds) {
        if (!CollectionUtils.isEmpty(fileIds)) {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setFileIdList(new ArrayList<>(fileIds));
            param.setBusinessKey(id);
            param.setSceneCode(ContentFileSceneCode.CONTENT_ANNOUNCEMENT.getCode());
            this.fileRefClient.refInduce(param);
        }
    }

    /**
     * @Description 消息保存
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    private void makeMsg(List<SubmitNoticeConfigCommand> commands, Announcement announcement) {
        if (com.guhecloud.platform.base.util.CollectionUtils.isNotEmpty(commands)) {
            List<MessageNotifyTarget> messageNotifyTargets = new ArrayList<>();
            List<MessageChannelRequest> messageChannelRequests = new ArrayList<>();
            Set<MessageChannelEnum> messageChannelEnums = new HashSet<>();
            commands.forEach(submitNoticeConfigCommand -> {
                //发送的通道
                String[] split = submitNoticeConfigCommand.getWay().split(",");
                for (String s : split) {
                    messageChannelEnums.add(MessageChannelEnum.valueOf(s.toUpperCase()));
                }
                MessageNotifyTarget notifyTargets = getNotifyTargets(submitNoticeConfigCommand, messageNotifyTargets);
                if (Objects.nonNull(notifyTargets)) {
                    messageNotifyTargets.add(notifyTargets);
                }
            });
            messageChannelEnums.forEach(messageChannel -> {
                messageChannelRequests.add(MessageChannelRequest.builder()
                                                   .channelCode(messageChannel)
                                                   .tenant(SessionContextHolder.getRequester().getTenant())
                                                   .build());
            });
            MessageSpecifyTmpSendRequest messageSpecifyTmpSendRequest = MessageSpecifyTmpSendRequest.builder()
                    .msgSceneCode(MESSAGE_SCENE_CODE)
                    .tmpParams(getSendMsgContent(announcement.getTitle()))
                    .businessId(announcement.getId().toString())
                    .channel(messageChannelRequests)
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .messageNotifyTargets(messageNotifyTargets)
                    .build();
            this.messageSendClient.sceneSpecifyTmpSend(messageSpecifyTmpSendRequest);
        }
    }

    private MessageNotifyTarget getNotifyTargets(SubmitNoticeConfigCommand submitNoticeConfigCommand,
                                                 List<MessageNotifyTarget> messageNotifyTargets
                                                ) {
        //发送的人员信息
        if (!CollectionUtils.isEmpty(submitNoticeConfigCommand.getUserIds())) {
            Set<Long> userIds = submitNoticeConfigCommand.getUserIds()
                    .stream().map(Long::valueOf).collect(Collectors.toSet());
            return buildNotifyTarget(userIds, MessageNotifyTargetEnum.USER);
        } else if (!CollectionUtils.isEmpty(submitNoticeConfigCommand.getPostIds())) {
            Set<Long> postIds = submitNoticeConfigCommand.getPostIds()
                    .stream().map(Long::valueOf).collect(Collectors.toSet());
            return buildNotifyTarget(postIds, MessageNotifyTargetEnum.POST);
        } else if (!CollectionUtils.isEmpty(submitNoticeConfigCommand.getDeptIds())) {
            Set<Long> deptIds = submitNoticeConfigCommand.getDeptIds()
                    .stream().map(Long::valueOf).collect(Collectors.toSet());
            return buildNotifyTarget(deptIds, MessageNotifyTargetEnum.DEPARTMENT);
        } else if (!CollectionUtils.isEmpty(submitNoticeConfigCommand.getRoleIds())) {
            Set<Long> roleIds = submitNoticeConfigCommand.getRoleIds()
                    .stream().map(Long::valueOf).collect(Collectors.toSet());
            return buildNotifyTarget(roleIds, MessageNotifyTargetEnum.ROLE);
        }
        return null;
    }

    private MessageNotifyTarget buildNotifyTarget(Set<Long> ids, MessageNotifyTargetEnum notifyTarget) {
        return MessageNotifyTarget.builder()
                .ids(ids)
                .notifyTargetType(notifyTarget)
                .build();
    }

    private Map<String, String> getSendMsgContent(String content) {
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        return map;
    }
}
