package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api;

import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.config.TencentConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request.DisbandRoomRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request.RemoveUserRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.response.DisbandRoomResponse;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.response.RemoveUserResponse;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomByStrRoomIdRequest;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomByStrRoomIdResponse;
import com.tencentcloudapi.trtc.v20190722.models.RemoveUserByStrRoomIdRequest;
import com.tencentcloudapi.trtc.v20190722.models.RemoveUserByStrRoomIdResponse;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 腾讯实时音视频api接口
 *
 * @author lanchun
 * @date 2025/2/11 14:55
 */
@Slf4j
@Service
public class TencentRealTimeCommunicationApiService {

    private TrtcClient getClient() {
        TencentConfig tenantConfig = TencentConfig.getTenantConfig();
        Credential cred = new Credential(tenantConfig.getSecurityId(), tenantConfig.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setConnTimeout(10); // 请求连接超时时间，单位为秒(默认60秒)
        httpProfile.setWriteTimeout(10);  // 设置写入超时时间，单位为秒(默认0秒)
        httpProfile.setReadTimeout(10);  // 设置读取超时时间，单位为秒(默认0秒)
        ClientProfile clientProfile = new ClientProfile();
        /* SDK默认用TC3-HMAC-SHA256进行签名
         * 非必要请不要修改这个字段 */
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new TrtcClient(cred, tenantConfig.getRegion(), clientProfile);
    }

    /**
     * 从房间移除用户
     *
     * @param request RemoveUserRequest
     *
     * @return RemoveUserResponse
     */
    public RemoveUserResponse removeUser(RemoveUserRequest request) {
        TrtcClient trtcClient = getClient();
        RemoveUserByStrRoomIdRequest removeUserByStrRoomIdRequest = new RemoveUserByStrRoomIdRequest();
        removeUserByStrRoomIdRequest.setUserIds(request.getUserIds().toArray(new String[0]));
        removeUserByStrRoomIdRequest.setRoomId(request.getRoomId());
        removeUserByStrRoomIdRequest.setSdkAppId(TencentConfig.getTenantConfig().getSdkAppId());
        try {
            RemoveUserByStrRoomIdResponse tencentResponse = trtcClient.RemoveUserByStrRoomId(removeUserByStrRoomIdRequest);
            return RemoveUserResponse.builder()
                    .requestId(tencentResponse.getRequestId())
                    .build();
        } catch (TencentCloudSDKException e) {
            log.error("移除用户失败,requestId:{},错误码:{}", e.getRequestId(), e.getErrorCode());
            throw new ServiceException("移除用户失败");
        }
    }

    /**
     * 解散房间
     *
     * @param request DisbandRoomRequest
     *
     * @return DisbandRoomResponse
     */
    public DisbandRoomResponse disbandRoom(DisbandRoomRequest request) {
        TrtcClient trtcClient = getClient();
        DismissRoomByStrRoomIdRequest dismissRoomByStrRoomIdRequest = new DismissRoomByStrRoomIdRequest();
        dismissRoomByStrRoomIdRequest.setRoomId(request.getRoomId());
        dismissRoomByStrRoomIdRequest.setSdkAppId(TencentConfig.getTenantConfig().getSdkAppId());
        try {
            DismissRoomByStrRoomIdResponse response = trtcClient.DismissRoomByStrRoomId(dismissRoomByStrRoomIdRequest);
            return DisbandRoomResponse.builder()
                    .requestId(response.getRequestId())
                    .build();
        } catch (TencentCloudSDKException e) {
            log.error("解散房间失败,requestId:{},错误码:{}", e.getRequestId(), e.getErrorCode());
            throw new ServiceException("解散房间失败");
        }
    }

    /**
     * 获取 UserSig
     * UserSig 是腾讯云设计的一种安全保护签名，目的是为了阻止恶意攻击者盗用您的云服务使用权
     *
     * @param userId 用户id
     *
     * @return UserSig
     */
    public String getSig(String userId) {
        TencentConfig tenantConfig = TencentConfig.getTenantConfig();
        TLSSigAPIv2 api = new TLSSigAPIv2(tenantConfig.getSdkAppId(), tenantConfig.getSdkAppSecretKey());
        //一个小时过期
        return api.genUserSig(userId, 3600);
    }
}
