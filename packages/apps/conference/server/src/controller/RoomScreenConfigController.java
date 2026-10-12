package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.param.RoomScreenConfigParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.RoomScreenConfigService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.RoomScreenConfigVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 会议屏配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/room/screen/config")
public class RoomScreenConfigController {
    private final RoomScreenConfigService roomScreenConfigService;

    /**
     * 会议屏配置列表查询
     */

    @GetMapping("/list")
    public List<RoomScreenConfigVO> roomScreenConfigList(@RequestParam(value = "keyword", required = false) String keyword) {
        return roomScreenConfigService.roomScreenConfigList(keyword);
    }

    /**
     * 会议屏配置新增或修改
     *
     * @param roomScreenConfigParam
     */
    @PostMapping("/save")
    public void roomScreenConfig(@RequestBody RoomScreenConfigParam roomScreenConfigParam) {
        roomScreenConfigService.saveRoomScreenConfig(roomScreenConfigParam);
    }

    /**
     * 删除会议屏配置
     *
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteRoomScreenConfig(@PathVariable Long id) {
        DatabaseContextHolder.softDelete(roomScreenConfigService, id);
    }

    /**
     * 开启会议屏展示
     *
     * @param roomScreenConfigParam
     */
    @PostMapping("/open")
    public void openScreenShow(@RequestBody RoomScreenConfigParam roomScreenConfigParam, HttpServletRequest httpServletRequest) {
        roomScreenConfigService.openScreenShow(roomScreenConfigParam, httpServletRequest);
    }

}
