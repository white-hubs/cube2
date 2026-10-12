package com.guhecloud.cube.srv.gen.business.info.sync.server.controller;

import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param.ThirdPartyWebhookParam;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param.UserInfoParam;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.OrganizationInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.ThirdPartyCallBackVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncOrganizationService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/20 16:19
 * @Description: 第三方对接获取
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdPartyController {
    private final SyncUserService syncUserService;

    private final SyncOrganizationService organizationService;

    /**
     * 全量获取员工信息
     *
     * @param keyword 关键字
     * @param period  时间范围
     */
    @GetMapping("/user/list-all")
    public List<UserInfoVO> getUserInfoListAll(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "period", required = false) Period period
                                              ) {
        return syncUserService.getUserInfoListAll(keyword, period);
    }

    /**
     * 分页获取员工信息
     *
     * @param userInfoParam 查询参数
     */
    @GetMapping("/user/list")
    public Pagination<UserInfoVO> getUserInfoList(UserInfoParam userInfoParam) {
        return syncUserService.getUserInfoList(userInfoParam);
    }

    /**
     * 全量获取部门信息
     *
     * @param keyword 关键字
     * @param period  时间范围
     */
    @GetMapping("/organization/list-all")
    public List<OrganizationInfoVO> getOrganizationInfoListAll(@RequestParam(value = "keyword", required = false) String keyword,
                                                               @RequestParam(value = "period", required = false) Period period
                                                              ) {
        return organizationService.getOrganizationInfoListAll(keyword, period);
    }

    /**
     * 回调测试接口
     */
    @PostMapping("/webhook/test/interface")
    public String webHookTestInterface(@RequestBody UserInfoVO userInfoVO) {
        log.info(userInfoVO.toString());
        return "success";
    }

    /**
     * 第三方业务回调
     */
    @PostMapping("/business/callback")
    public ThirdPartyCallBackVO webHookBusinessInterface(@RequestBody ThirdPartyWebhookParam thirdPartyWebhookParam) {
        log.info(thirdPartyWebhookParam.toString());
        ThirdPartyCallBackVO thirdPartyCallBackVO = new ThirdPartyCallBackVO();
        return thirdPartyCallBackVO;
    }

}
