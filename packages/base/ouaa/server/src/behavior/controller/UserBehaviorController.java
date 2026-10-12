package com.guhecloud.cube.biz.ouaa.server.behavior.controller;

import com.guhecloud.cube.biz.ouaa.server.behavior.entity.UserBehavior;
import com.guhecloud.cube.biz.ouaa.server.behavior.param.UserBehaviorModifyParam;
import com.guhecloud.cube.biz.ouaa.server.behavior.service.UserBehaviorService;
import com.guhecloud.cube.biz.ouaa.server.behavior.vo.UserBehaviorVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserBehaviorController {

    private final UserBehaviorService userBehaviorService;

    /**
     * 用户行为保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/user-behavior")
    public Long save(@RequestBody @Valid UserBehaviorModifyParam param) {
        param.setUserId(SecurityContextHolder.getPrincipal().getId());
        param.setTerminal(SessionContextHolder.getRequester().getTerminal());
        return this.userBehaviorService.save(param);
    }

    /**
     * 用户行为获取
     *
     * @return
     */
    @GetMapping("/user-behavior")
    public UserBehaviorVO getUserBehavior() {
        UserBehavior userBehavior = this.userBehaviorService.getUserBehavior(SecurityContextHolder.getPrincipal()
                                                                                     .getId(), SessionContextHolder.getRequester()
                                                                                     .getTerminal());
        if (Objects.isNull(userBehavior)) {
            return null;
        }
        return ObjectUtils.copying(userBehavior, UserBehaviorVO.class);
    }
}
