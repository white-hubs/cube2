package com.guhecloud.cube.srv.gen.conferencemanage.server.third.service;

import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/24 10:15
 * @Description:
 */
public interface DeviceProgramSendService {

    /**
     * 新建发送节目
     *
     * @param screenConfig
     */
    void newProgram(RoomScreenConfig screenConfig, HttpServletRequest httpServletRequest);

    /**
     * 清空节目
     */
    void clearProgram(RoomScreenConfig screenConfig);

    /**
     * 发送节目
     */
    void sendProgram(BookRoom bookRoom, RoomScreenConfig roomScreenConfig);

    /**
     * 判断是否支持该厂商
     *
     * @param codeEnum
     *
     * @return
     */
    boolean support(String codeEnum);
}
