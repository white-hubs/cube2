package com.guhecloud.cube.base.videogw.server.common.application.commandgateway.aspect;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileGenerateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoBaseCommand;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoFileStoreCommand;
import com.guhecloud.cube.base.videogw.server.common.domain.annotation.VideoCommand;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoCommandStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoFileStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoCommandRecordPO;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.VideoCommandRecordRepository;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.VideoFileRepository;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoCommandRecordAspect {

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final VideoCommandRecordRepository videoCommandRecordRepository;

    private final VideoFileRepository videoFileRepository;

    private final FileClient fileClient;

    @Pointcut("@annotation(com.guhecloud.cube.base.videogw.server.common.domain.annotation.VideoCommand)")
    public void point() {
        // This method is used to define the pointcut expression for updateByTenant method in UserUnderTenantController
    }

    @Before(value = "point()")
    public void beforeInsert(JoinPoint joinPoint) {
        try {
            VideoCommand videoCommand = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(VideoCommand.class);
            VideoCommandRecordPO videoCommandRecordPO = new VideoCommandRecordPO();
            Object[] args = joinPoint.getArgs();//请求参数
            SubsystemEndpointConfig subsystemConfig = subsystemRedisUtils.getSubEndpointRefreshConfig(
                    SubsystemTypeConstants.VIDEO_SERVER + ":" + SessionContextHolder
                            .getRequester().getTenant().toString());
            for (Object arg : args) {
                if (arg instanceof VideoBaseCommand) {
                    VideoBaseCommand videoBaseCommand = (VideoBaseCommand) arg;
                    videoCommandRecordPO.setId(videoBaseCommand.getId());
                    videoCommandRecordPO.setResultParam(videoBaseCommand.getRequestId());
                    videoCommandRecordPO.setCommand(videoCommand.command().name());
                    videoCommandRecordPO.setManufacturerCode(subsystemConfig.getManufacturer());
                    videoCommandRecordPO.setRequestParam(JsonUtils.serialize(arg));
                    videoCommandRecordPO.setStatus(VideoCommandStatusEnum.ISSUE_PROGRESS.name());
                    break;
                }
            }
            this.videoCommandRecordRepository.save(videoCommandRecordPO);
        } catch (Exception e) {
            log.error("视频命令下发前置记录失败");
        }
    }

    @AfterReturning(value = "point()", returning = "res")
    public void afterUpdate(JoinPoint joinPoint, Object res) {
        try {
            Long id = getCommandId(joinPoint);
            VideoFileStoreCommand videoFileStoreCommand = null;
            Object[] args = joinPoint.getArgs();//请求参数
            for (Object arg : args) {
                if (arg instanceof VideoFileStoreCommand) {
                    videoFileStoreCommand = (VideoFileStoreCommand) arg;
                    break;
                }
            }
            this.videoCommandRecordRepository.updateResult(id, VideoCommandStatusEnum.ISSUE_SUCCESS.name(), JsonUtils.serialize(res));
            if (Objects.nonNull(videoFileStoreCommand)) {
                VideoCommand videoCommand = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(VideoCommand.class);
                boolean locallyStored = Objects.nonNull(videoFileStoreCommand.getLocallyStored()) ? videoFileStoreCommand.getLocallyStored()
                                                                                                  : false;
                String resultUrl = "";
                Field[] fields = res.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(videoCommand.resultUrl())) {
                        field.setAccessible(true);
                        resultUrl = field.get(res).toString();
                        break;
                    }
                }
                VideoFilePO videoFilePO = VideoFilePO.builder()
                        .id(id)
                        .path(resultUrl)
                        .locallyStored(locallyStored)
                        .storeSceneCode(videoFileStoreCommand.getSceneCode())
                        .locallyStoredStatus(VideoFileStatusEnum.LOCAL_STAY_STORAGE.name())
                        .build();
                if (Objects.nonNull(videoFileStoreCommand.getDelayTime())) {
                    videoFilePO.setLocallyStoredStatus(VideoFileStatusEnum.LOCAL_DELAY_STORAGE.name());
                    videoFilePO.setRetryTimes(0);
                    videoFilePO.setRetryNextTime(new Date(System.currentTimeMillis() + videoFileStoreCommand.getDelayTime() * 1000));
                }
                if (locallyStored) {
                    FileGenerateParam fileGenerateParam = new FileGenerateParam();
                    fileGenerateParam.setSceneCode(videoFileStoreCommand.getSceneCode());
                    fileGenerateParam.setFileName(IdWorker.getIdStr() + ".text");
                    FileVO file = this.fileClient.getUploadDetails(fileGenerateParam);
                    videoFilePO.setFileId(file.getId());
                }
                videoFileRepository.save(videoFilePO);
            }
        } catch (Exception e) {
            log.error("视频命令下发后置更新记录失败");
        }
    }

    private Long getCommandId(JoinPoint joinPoint) {
        Long id = 0L;
        Object[] args = joinPoint.getArgs();//请求参数
        for (Object arg : args) {
            if (arg instanceof VideoBaseCommand) {
                VideoBaseCommand videoBaseCommand = (VideoBaseCommand) arg;
                id = videoBaseCommand.getId();
                break;
            }
        }
        return id;
    }

    @AfterThrowing(value = "point()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            Long id = getCommandId(joinPoint);
            this.videoCommandRecordRepository.updateResult(id, VideoCommandStatusEnum.ISSUE_FAIL.name(), ex.getMessage());
        } catch (Exception e) {
            log.error("视频命令下发更新异常记录失败");
        }
    }

}
