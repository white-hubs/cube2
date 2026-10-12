package com.guhecloud.cube.biz.ouaa.server.utils;

import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TerminalEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;

public class UserWechatUtils {
    public static void setWechatOpenid(UserWechat wechatInfo, String openId) {
        TerminalEnum terminal = TerminalEnum.build(SessionContextHolder.getRequester().getTerminal());
        switch (terminal) {
            case WEB -> wechatInfo.setWebOpenid(openId);
            case APP -> wechatInfo.setAppOpenid(openId);
            case MP -> wechatInfo.setMpOpenid(openId);
            default -> throw new UnimplementedException("Unsupported terminal");
        }
    }

    public static String getWechatOpenid(UserWechat info) {
        TerminalEnum terminal = TerminalEnum.build(SessionContextHolder.getRequester().getTerminal());
        return switch (terminal) {
            case WEB -> info.getWebOpenid();
            case APP -> info.getAppOpenid();
            case MP -> info.getMpOpenid();
            default -> throw new UnimplementedException("Unsupported terminal");
        };
    }
}
