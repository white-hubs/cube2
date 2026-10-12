package com.guhecloud.cube.biz.ouaa.server.controller.context;

import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页应用卡片接口管理
 *
 * @author lanchun
 * @date 2022/5/13 14:12
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class HomeApplicationCardController {

    private final HomeApplicationCardService homeApplicationCardService;

    /***
     * 首页应用卡片保存
     * @param param
     */
    @PostMapping("/home-application-card")
    public void save(@Valid @RequestBody HomeApplicationCardModifyParam param) {
        this.homeApplicationCardService.save(param);
    }

    /**
     * 首页应用卡片批量保存
     *
     * @param param
     */
    @PostMapping("/home-application-card/batch")
    public void saveBatch(@Valid @RequestBody HomeApplicationCardBatchModifyParam param) {
        this.homeApplicationCardService.saveBatch(param);
    }

    /**
     * 首页应用卡片删除
     *
     * @param cardCode
     */
    @DeleteMapping("/home-application-card/{cardCode}")
    public void removeByApplicationId(@PathVariable("cardCode") String cardCode) {
        this.homeApplicationCardService.removeByCardCode(cardCode);
    }
}
