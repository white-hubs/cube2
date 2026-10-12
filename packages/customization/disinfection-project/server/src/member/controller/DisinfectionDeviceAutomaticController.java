package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceAutomaticCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DisinfectionDeviceAutomaticQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionDeviceAutomaticService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDeviceAutomaticPageResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自动化指令相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionDeviceAutomaticController {

    private final DisinfectionDeviceAutomaticService disinfectionDeviceAutomaticService;

    /**
     * 自动指令分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/bus/member/automatic/page")
    public Pagination<MemberDeviceAutomaticPageResult> deviceAutomaticPage(DisinfectionDeviceAutomaticQueryParam param) {
        return disinfectionDeviceAutomaticService.deviceAutomaticPage(param);
    }

    /**
     * 添加自动化指令
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/automatic/add")
    public Boolean addDeviceAutomatic(@RequestBody DeviceAutomaticCommandParam param) {
        return disinfectionDeviceAutomaticService.addDeviceAutomatic(param);
    }

    /**
     * 修改自动化指令
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("bus/member/automatic/edit")
    public Boolean editDeviceAutomatic(@RequestBody DeviceAutomaticCommandParam param) {
        return disinfectionDeviceAutomaticService.editDeviceAutomatic(param);
    }

    /**
     * 移除指令
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/bus/member/automatic/remove")
    public Boolean removeDeviceAutomatic(Long id) {
        return disinfectionDeviceAutomaticService.removeDeviceAutomatic(id);
    }
}
