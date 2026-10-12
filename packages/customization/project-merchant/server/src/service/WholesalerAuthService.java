package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerAuthDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerAuthInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerAuth;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-08
 */
public interface WholesalerAuthService extends IService<WholesalerAuth> {

    String AUTH_NOTICE = "authNotice";
    String AUTH_ID_CARD_PHOTO = "wholesaler-auth-identity-card-photo";
    String AUTH_ID_CARD_NATION = "wholesaler-auth-identity-card-nation";
    String AUTH_BUSINESS_LICENSE ="wholesaler-auth-business-license";
    String AUTH_PERSONNEL_PIC ="wholesaler-personnel-pic";
    Integer AUTH_RANDOM_NUMBER_LENGTH = 6;
    String AUTH_VERIFY_CODE_TYPE = "auth-verify-code";
    String REDIS_TYPE = ":verify_code:";
    String PROJECT_NAME = "iops:";

    String AUTH_STATUS_PASS = "通过";
    String AUTH_STATUS_NO_PASS = "未通过";
    String AUTH_NOTICE_TITLE = "实名认证";
    String AUTH_NOTICE_CONTENT_TO_COMPANY = "您提交的实名认证审核";
    String AUTH_NOTICE_CONTENT_TO_PROJECT = "提交了一个实名认证申请，请及时审核处理";

    /**
     * 根据用户ID返回实名用户信息相关DTO
     * @param userId 用户ID
     * @return 实名认证相关信息
     */
    WholesalerAuthDTO getAuth(String projectId, String projectName, String userId);

    /**
     * 新增实名认证（还要调用验证码验证）
     * @param dto 实名认证信息
     * @return 返回记录ID
     */
    String saveOrUpdate(WholesalerAuthDTO dto);

    /**
     * 批量通过认证
     * @param dto
     */
    void batchPass(BatchDTO dto, LoginUser user);

    /**
     * 单个认证通过
     * @param id
     * @param user
     */
    void pass(String id, LoginUser user);

    /**
     * 驳回认证
     * @param id
     * @param rejectReason
     * @param user
     */
    void doNotPass(String id, String rejectReason, LoginUser user);

    /**
     *
     * 发送验证码
     * @param projectId 项目ID，存放sms的时候需要
     * @param userId 用户ID，存放sms的时候需要
     * @param mobile 电话号码，存放sms的时候需要
     * @return 并且将验证码保存到内存中
     */
    String sendVerifyCode(String projectId,String userId,String mobile);

    WholesalerAuthDTO getAuthDto(String id);

    WholesalerAuthInfoDTO getAuthByStallId(String projectId, String stallId, Long userId);

    void extendStallNames(List<WholesalerAuthDTO> dtos);

    /**
     * 删除实名认证信息
     * @param userId
     */
    void removeAuth(String userId);

}
