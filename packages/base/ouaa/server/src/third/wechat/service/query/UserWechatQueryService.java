package com.guhecloud.cube.biz.ouaa.server.third.wechat.service.query;

import com.guhecloud.cube.biz.ouaa.server.dao.UserWechatMapper;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.param.query.UserWechatQueryParam;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.vo.UserWechatVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserWechatQueryService {

    private final UserWechatMapper userWechatMapper;

    /**
     * 获取用户微信信息
     *
     * @param param
     *
     * @return
     */
    public List<UserWechatVO> getUserWechatList(UserWechatQueryParam param) {
        return this.userWechatMapper.selectUserWechatListByConfigId(param);
    }
}
