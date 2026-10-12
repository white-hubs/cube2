package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiAttendanceScheduleListbyusersRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.config.DingTalkConfig;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.converts.ScheduleListByUsersVOConvertsTOResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.converts.UserIdByMobileVOConvertsTOResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response.DingTalkScheduleListByUsersResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response.DingTalkUserIdByMobileResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DingTalkApiService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACCESS_TOKEN_KEY = "DING_TALK_ACCESS_TOKEN:%s";

    private static final String GET = "GET";

    private static final String ACCESS_TOKEN_URL = "/gettoken";

    private static final String DING_TALK_SCHEDULE_LIST_BY_USERS_URL = "/topapi/attendance/schedule/listbyusers";

    private static final String USER_ID_BY_MOBILE_URL = "/topapi/v2/user/getbymobile";

    public DingTalkApiService(RedisTemplate<String, Object> redisTemplate) {this.redisTemplate = redisTemplate;}

    /**
     * 批量查询人员排班信息
     *
     * @param userIds   用户id集合,一次性最多只支持查询50个
     * @param beginTime 开始时间和结束时间的间隔不能超过7天。查询时间限制距今180天内。
     * @param endTime   开始时间和结束时间的间隔不能超过7天。 查询时间限制距今180天内。
     *
     * @return 排班列表
     */
    public List<DingTalkScheduleListByUsersResponse> getListByUsers(Set<String> userIds, LocalDateTime beginTime,
                                                                    LocalDateTime endTime
                                                                   ) {
        try {
            var dingTalkConfig = DingTalkConfig.getDingTalkConfig();
            var client = getClient(DING_TALK_SCHEDULE_LIST_BY_USERS_URL);
            var req = new OapiAttendanceScheduleListbyusersRequest();
            if (dingTalkConfig != null) {
                req.setOpUserId(dingTalkConfig.getOpUserId());//操作人id
            }
            req.setUserids(String.join(",", userIds));
            req.setFromDateTime(DateUtils.toTimestamp(beginTime));
            req.setToDateTime(DateUtils.toTimestamp(endTime));
            var response = client.execute(req, getAccessToken());
            if (!response.isSuccess()) {
                throw new ApiException(response.getErrorCode(), response.getErrmsg());
            }
            return ScheduleListByUsersVOConvertsTOResponse.scheduleListByUsersVOSConvertsTOResponses(response.getResult());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     *
     * @return 用户id
     */
    public DingTalkUserIdByMobileResponse getUserIdByMobile(String mobile) {
        try {
            var client = getClient(USER_ID_BY_MOBILE_URL);
            var req = new OapiV2UserGetbymobileRequest();
            req.setMobile(mobile);
            var response = client.execute(req, getAccessToken());
            if (!response.isSuccess()) {
                throw new ApiException(response.getErrorCode(), response.getErrmsg());
            }
            return UserIdByMobileVOConvertsTOResponse.userIdByMobileVOConvertsTOResponse(response.getResult());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken() {
        var key = String.format(ACCESS_TOKEN_KEY, SessionContextHolder.getRequester().getTenant());
        var oApiGetTokenResponse = redisTemplate.opsForValue()
                .get(key);
        if (Objects.nonNull(oApiGetTokenResponse)) {
            return (String) oApiGetTokenResponse;
        }
        try {
            var response = getOapiGettokenResponse();
            if (!response.isSuccess()) {
                throw new ApiException(response.getErrorCode(), response.getErrmsg());
            }
            redisTemplate.opsForValue()
                    .set(key, response.getAccessToken(), response.getExpiresIn() - 60, TimeUnit.SECONDS);
            return response.getAccessToken();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static OapiGettokenResponse getOapiGettokenResponse() throws ApiException {
        var dingTalkConfig = DingTalkConfig.getDingTalkConfig();
        if (Objects.isNull(dingTalkConfig)) {
            throw new ApiException("DingTalkConfig is null");
        }
        var client = getClient(ACCESS_TOKEN_URL);
        var request = new OapiGettokenRequest();
        request.setAppkey(dingTalkConfig.getAppKey());
        request.setAppsecret(dingTalkConfig.getAppSecret());
        request.setHttpMethod(GET);
        return client.execute(request);
    }

    private static DefaultDingTalkClient getClient(String url) throws ApiException {
        var dingTalkConfig = DingTalkConfig.getDingTalkConfig();
        if (Objects.isNull(dingTalkConfig)) {
            throw new ApiException("DingTalkConfig is null");
        }
        return new DefaultDingTalkClient(dingTalkConfig.getBaseUrl() + url);
    }
}
