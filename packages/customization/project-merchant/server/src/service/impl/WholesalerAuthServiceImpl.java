package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DataDesensitizedUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerAuth;
import com.guhecloud.cube.deliver.project.merchant.service.*;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerAuthMapper;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerAuthServiceImpl extends ServiceImpl<WholesalerAuthMapper, WholesalerAuth> implements WholesalerAuthService {

    private final SpaceService spaceService;
    private final FileClient fileClient;
    //    private final SmsApi smsApi;
    private final UserClient userClient;
    private final WholesalerAuthMapper mapper;
    private final CompanyUserService cuService;
    private final WholesalerNoticeService wholesalerNoticeService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TaskExecutor taskExecutor;
    //    private final SiteMsgApi siteMsgApi;
    //    private final WeChatMsgApi weChatMsgApi;
    private final UserDeliverService userDeliverService;

    @Override
    public WholesalerAuthDTO getAuth(String projectId, String projectName, String userId) {
        WholesalerAuthDTO dto = this.getAuthByUserId(userId);
        List<SpaceDTO> spaceDtos = spaceService.getSpacesByUserId(userId);
        UserDeliver user = userDeliverService.getById(userId);
        // 是否可以重新认证，如果新增关联的档位那么就需要重新认证
        boolean flag = false;
        String stallsString = "";
        String stallIds = "";
        for (SpaceDTO spaceDTO : spaceDtos) {
            if (StringUtils.isEmpty(stallsString)) {
                stallsString = spaceDTO.getName();
                stallIds = spaceDTO.getId();
                dto.setCompanyId(spaceDTO.getCompanyId());
                dto.setCompanyName(spaceDTO.getCompanyName());
            } else {
                stallsString = stallsString.concat("," + spaceDTO.getName());
                stallIds = stallIds.concat("," + spaceDTO.getId());
            }
            // 如果该用户现有的关联档位中有未认证的，则可以再次认证
            String oldStallIds = dto.getStallIds();
            if (StringUtils.isNotEmpty(oldStallIds) && !oldStallIds.contains(spaceDTO.getId())) {
                flag = true;
            }
        }
        dto.setProjectName(projectName);
        dto.setProjectId(projectId);
        dto.setStallName(stallsString);
        dto.setUserId(userId);
        dto.setLoginAccount(user.getUsername());
        dto.setStallIds(stallIds);
        dto.setRepAuth(flag);
        return dto;
    }

    @Override
    public String saveOrUpdate(WholesalerAuthDTO dto) {
        //首先验证一下短信验证码
        //        String key = PROJECT_NAME + dto.getProjectId() + REDIS_TYPE + dto.getMobile() + ":" + dto.getUserId();
        //        String value = (String) redisTemplate.opsForValue().get(key);
        //        if (value == null) {
        //            throw new ServiceException("请先获取验证码");
        //        }
        //        if (!dto.getVerifyCode().equals(value)) {
        //            throw new ServiceException("验证码输入不正确，请重新输入，验证码5分钟内均有效");
        //        }
        //这里要根据userid找到companyId......................
        WholesalerAuth auth = dto.convert();
        auth.setLicenseId(dto.getLicenseIds().stream().collect(Collectors.joining(",")));
        //判断是insert还是save
        WholesalerAuth wholesalerAuth = this.lambdaQuery().eq(WholesalerAuth::getUserId, dto.getUserId()).one();
        String authId;
        auth.setStatus(0);
        if (wholesalerAuth == null) {
            auth.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            auth.setCreateTime(LocalDateTime.now());
            mapper.insert(auth);
            authId = auth.getId();
        } else {
            authId = wholesalerAuth.getId();
            auth.setId(authId);
            mapper.updateById(auth);
        }
        //TODO 通知
        //        this.sendNoticeToProject(auth);
        return authId;
    }

    @Override
    public void batchPass(BatchDTO dto, LoginUser user) {
        UserDTO userInfoDTO = userClient.getUser(user.getId());
        List<String> ids = dto.getIds();
        if (ids == null || ids.size() < 1) {
            return;
        }
        for (String id : ids) {
            this.lambdaUpdate().eq(WholesalerAuth::getId, id)
                    .set(WholesalerAuth::getStatus, 1)
                    .set(WholesalerAuth::getReviewer, userInfoDTO == null ? user.getUsername() : userInfoDTO.getName())
                    .set(WholesalerAuth::getReviewTime, LocalDateTime.now())
                    .update();
        }
        this.sendNoticeToCompany(ids, AUTH_STATUS_PASS);
    }

    @Override
    public void pass(String id, LoginUser user) {
        UserDTO userInfoDTO = userClient.getUser(user.getId());
        this.lambdaUpdate().eq(WholesalerAuth::getId, id)
                .set(WholesalerAuth::getStatus, 1)
                .set(WholesalerAuth::getReviewer, userInfoDTO == null ? user.getUsername() : userInfoDTO.getName())
                .set(WholesalerAuth::getReviewTime, LocalDateTime.now())
                .update();
        this.sendNoticeToCompany(Collections.singletonList(id), AUTH_STATUS_PASS);
    }

    @Override
    public void doNotPass(String id, String rejectReason, LoginUser user) {
        UserDTO userInfoDTO = userClient.getUser(user.getId());
        this.lambdaUpdate().eq(WholesalerAuth::getId, id).set(WholesalerAuth::getStatus, 2)
                .set(WholesalerAuth::getReviewer, userInfoDTO == null ? user.getUsername() : userInfoDTO.getName())
                .set(WholesalerAuth::getReviewTime, LocalDateTime.now())
                .set(WholesalerAuth::getRejectReason, rejectReason)
                .update();
        this.sendNoticeToCompany(Collections.singletonList(id), AUTH_STATUS_NO_PASS);
    }

    @Override
    public String sendVerifyCode(String projectId, String userId, String mobile) {
        //verifyCode为全局变量，下一个方法也可以用
        String verifyCode = RandomUtil.randomNumbers(AUTH_RANDOM_NUMBER_LENGTH);
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        //发送信息
        String key = PROJECT_NAME + projectId + REDIS_TYPE + mobile + ":" + userId;
        redisTemplate.opsForValue().set(key, verifyCode, 5, TimeUnit.MINUTES);
        //        smsApi.send(projectId, AUTH_VERIFY_CODE_TYPE, true, mobile, userIds,
        //                JSmsTemplateEnum.T_VERIFY.getSmsTempId(), verifyCode);
        return verifyCode;
    }

    @Override
    public WholesalerAuthDTO getAuthDto(String id) {
        WholesalerAuthDTO dto = new WholesalerAuthDTO(this.lambdaQuery().eq(WholesalerAuth::getId, id).one());
        dto.setStallName(this.getStallNamesByIds(dto.getStallIds()));
        //获取正面照，身份证正反照，营业执照信息等
        getMarkFile(dto);
        try {
            UserDTO userInfoDTO = userClient.getUserByUserName(dto.getCreateBy());
            dto.setCreateBy(userInfoDTO == null ? dto.getCreateBy() : userInfoDTO.getName());
        } catch (Exception e) {
            log.error("扩展创建人失败", e);
        }
        return dto;
    }

    @Override
    public WholesalerAuthInfoDTO getAuthByStallId(String projectId, String stallId, Long userId) {
        Space space = spaceService.lambdaQuery().eq(Space::getId, stallId).one();
        String companyId = (space == null ? "" : space.getCompanyId());
        //        WholesalerAuth auth = this.lambdaQuery().eq(WholesalerAuth::getProjectId, projectId)
        //                .eq(WholesalerAuth::getStallIds, companyId)
        //                .eq(WholesalerAuth::getStatus, 1)
        //                .one();
        List<WholesalerAuth> auths = this.lambdaQuery().eq(WholesalerAuth::getProjectId, projectId)
                .eq(WholesalerAuth::getUserId, userId)
                .eq(WholesalerAuth::getCompanyId, companyId)
                .like(WholesalerAuth::getStallIds, stallId)
                .eq(WholesalerAuth::getStatus, 1)
                .list();
        if (auths.size() < 1) {
            return null;
        }
        WholesalerAuthInfoDTO dto = new WholesalerAuthInfoDTO(auths.get(0));
        UserDTO userDto = userClient.getUser(userId);
        if (userDto != null) {
            dto.setSignatory(userDto.getName());
            dto.setSignatoryMobile(userDto.getMobile());
        }
        return dto;
    }

    @Override
    public void extendStallNames(List<WholesalerAuthDTO> dtos) {
        dtos.forEach(auth -> {
            auth.setIdentityCard(DataDesensitizedUtils.desensitizedIdNumber(auth.getIdentityCard()));
            String stallIds = auth.getStallIds();
            auth.setStallName(this.getStallNamesByIds(stallIds));
            //获取正面照，身份证正反照，营业执照信息等
            getMarkFile(auth);
        });
    }

    @Override
    public void removeAuth(String userId) {
        List<WholesalerAuth> auths = this.lambdaQuery().eq(WholesalerAuth::getUserId, userId).list();
        auths.stream().forEach(auth -> {
            //            删除相关文件
        });
    }

    // 根据逗号分隔的档位ID查找档位名称
    private String getStallNamesByIds(String stallIds) {
        if (StringUtils.isNotEmpty(stallIds)) {
            List<String> stallIdList = Arrays.asList(stallIds.split(","));
            if (stallIdList.size() > 0) {
                List<Space> spaceList = spaceService.lambdaQuery().in(Space::getId, stallIdList).list();
                return spaceList.stream().map(Space::getName).collect(Collectors.joining(","));
            }
        }
        return "";
    }

    /**
     * 如果有认证记录就返回，没有就返回个空的
     *
     * @param userId 用户ID
     *
     * @return 实名认证记录
     */
    private WholesalerAuthDTO getAuthByUserId(String userId) {
        WholesalerAuth auth = this.lambdaQuery().eq(WholesalerAuth::getUserId, userId)
                .one();
        if (auth == null) {
            WholesalerAuthDTO dto = new WholesalerAuthDTO();
            //认证状态为未认证
            dto.setStatus(-1);
            return dto;
        }
        WholesalerAuthDTO dto = new WholesalerAuthDTO(auth);
        //获取正面照，身份证正反照，营业执照信息等
        getMarkFile(dto);
        //找对应的类型和id
        return dto;
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerAuthDTO dto) {
        FileVO fileDtos = null;
        if (ObjectUtil.isNotEmpty(dto.getIdCardPhotoId())) {
            fileDtos = fileClient.getById(dto.getIdCardPhotoId());
            if (fileDtos != null) {
                dto.setIdCardPhotoDtos(fileDtos);
            }
        }
        if (ObjectUtil.isNotEmpty(dto.getIdCardNationId())) {
            fileDtos = fileClient.getById(dto.getIdCardNationId());
            if (fileDtos != null) {
                dto.setIdCardNationDtos(fileDtos);
            }
        }
        if (ObjectUtil.isNotEmpty(dto.getPersonnelPicId())) {
            fileDtos = fileClient.getById(dto.getPersonnelPicId());
            if (fileDtos != null) {
                dto.setPersonnelPicIdsDtos(fileDtos);
            }
        }
        if (ObjectUtil.isNotEmpty(dto.getLicenseId())) {
            List<Long> fileIds = Arrays.stream(dto.getLicenseId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            List<FileVO> fileVOList = fileClient.getByIds(fileIds);
            if (fileVOList != null) {
                dto.setLicenseDtos(fileVOList);
            }
        }
    }

    private void markFiles(String businessKey, String businessType, List<Long> fileIds) {
        if (fileIds != null && !fileIds.isEmpty()) {
            //            fileIds.forEach(fileId ->
            //                    fileApi.mark(fileId, businessType, businessKey));
        }
    }

    // 发送消息给市场方
    private void sendNoticeToProject(WholesalerAuth auth) {
        try {
            // 获取发送通知的用户
            List<WholesalerNoticeDTO> noticeDTOS = wholesalerNoticeService.getNoticeIds(AUTH_NOTICE);
            noticeDTOS.forEach(notice -> {
                Set<String> userIds = notice.getNoticeUserIds();
                if (userIds.size() < 1) {
                    return;
                }
                if (notice.isSite()) {
                    //                    siteMsgApi.send(auth.getProjectId(), AUTH_NOTICE, auth.getId(), new ArrayList<>(userIds), AUTH_NOTICE_TITLE, auth.getRealName() + AUTH_NOTICE_CONTENT_TO_PROJECT, 7 * 24, true, notice.isPop(), auth);
                }
                if (notice.isSms()) {
                    //                    smsApi.send(auth.getProjectId(), AUTH_NOTICE, true, auth.getId(), new ArrayList<>(userIds), JSmsTemplateEnum.T_AUTH_TO_PROJECT.getSmsTempId(), auth.getRealName());
                }
                if (notice.isWx()) {
                    //                    weChatMsgApi.send(auth.getProjectId(), AUTH_NOTICE, new ArrayList<>(userIds), auth.getRealName() + AUTH_NOTICE_CONTENT_TO_PROJECT, true);
                }
            });
        } catch (Exception e) {
            log.error("实名认证发送消息给市场人员失败，任务ID：" + auth.getId(), e);
        }
    }

    // 发送消息给商户
    private void sendNoticeToCompany(List<String> authIds, String status) {
        if (authIds == null || authIds.size() < 1) {
            return;
        }
        WholesalerAuthServiceImpl _this = this;
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //                DataSourceContextHolder.setDataSourceKey(DataSources.PROJECT.getValue());

                authIds.forEach(authId -> {
                    List<String> stallIds = new ArrayList<>();
                    WholesalerAuth auth = _this.getById(authId);
                    // 找到认证关联的档位ID
                    if (auth != null && StringUtils.isNotEmpty(auth.getStallIds())) {
                        stallIds.addAll(Arrays.asList(auth.getStallIds().split(",")));
                        List<String> userIds = new ArrayList<>();
                        stallIds.forEach(stallId -> {
                            // 查到档位的档主信息并发送通知
                            String stallOwner = _this.getStallOwner(auth.getProjectId(), stallId);
                            if (StringUtils.isNotEmpty(stallOwner)) {
                                userIds.add(stallOwner);
                            }
                        });
                        _this.sendNotice(auth, auth.getProjectId(), userIds, status);
                    }
                });

            }
        });
    }

    // 查询档位的档主
    private String getStallOwner(String projectId, String stallId) {
        CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, stallId)
                .eq(CompanyUser::getProjectId, projectId)
                .eq(CompanyUser::getOwner, true).one();
        return companyUser == null ? "" : companyUser.getUserId();
    }

    private void sendNotice(WholesalerAuth auth, String projectId, List<String> userIds, String status) {
        try {
            //            smsApi.send(projectId, AUTH_NOTICE, auth.getId(), new ArrayList<>(userIds), JSmsTemplateEnum.T_AUTH_TO_COMPANY.getSmsTempId(), status);
            //            weChatMsgApi.send(projectId, AUTH_NOTICE, new ArrayList<>(userIds), AUTH_NOTICE_CONTENT_TO_COMPANY + status);
            //            siteMsgApi.send(projectId, AUTH_NOTICE, auth.getId(), userIds, AUTH_NOTICE_TITLE, AUTH_NOTICE_CONTENT_TO_COMPANY + status, 7 * 24, false, null, auth);
        } catch (Exception e) {
            log.error("实名认证发送消息给商户失败，任务ID：" + auth.getId(), e);
        }
    }

}
