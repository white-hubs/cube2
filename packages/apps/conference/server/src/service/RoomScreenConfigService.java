package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.RoomScreenConfigParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.RoomScreenConfigVO;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @description 会议屏配置Service
 * @createDate 2024-09-10 15:41:00
 */
public interface RoomScreenConfigService extends IService<RoomScreenConfig> {
    /**
     * 会议屏配置列表
     *
     * @param keyword
     *
     * @return
     */
    List<RoomScreenConfigVO> roomScreenConfigList(String keyword);

    /**
     * 新增或更新
     *
     * @param roomScreenConfigParam
     */
    void saveRoomScreenConfig(RoomScreenConfigParam roomScreenConfigParam);

    /**
     * 开启会议屏展示
     *
     * @param roomScreenConfigParam
     */
    void openScreenShow(RoomScreenConfigParam roomScreenConfigParam, HttpServletRequest httpServletRequest);

}
