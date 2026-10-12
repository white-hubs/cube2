package com.guhecloud.cube.biz.fusion.communicate.server.gps.controller;

import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UploadGpsParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UserGpsHistoriesQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.service.UserGpsHistoryService;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.vo.UserGpsHistoriesVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserGpsController {

    private final UserGpsHistoryService userGpsHistoryService;

    /**
     * 上报gps坐标信息
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/gps/upload")
    public Long uploadUserGps(@Valid @RequestBody UploadGpsParam param) {
        return this.userGpsHistoryService.uploadUserGps(param);
    }

    /**
     * gps坐标信息分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/gps/page")
    public Pagination<UserGpsHistoriesVO> getGpsHistoriesPage(@Valid UserGpsHistoriesQueryParam param) {
        return this.userGpsHistoryService.getGpsHistories(param);
    }
}
