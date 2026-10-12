package com.guhecloud.cube.srv.gen.visitor.management.server.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.service.BlacklistService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.BlacklistParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.BlacklistResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问配置(时段、权限、黑名单等
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistController {

    private final BlacklistService blacklistService;

    /**
     * 黑名单分页
     *
     * @param param 入参
     *
     * @return 黑名单列表
     */
    @GetMapping("/visitor/blacklist/page")
    public Pagination<BlacklistResult> blacklistPage(KeyWordPageParam param) {
        return blacklistService.blacklistPage(param);
    }

    /**
     * 黑名单详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/visitor/blacklist/detail")
    public BlacklistResult blackListDetail(Long id) {
        return blacklistService.blackListDetail(id);
    }

    /**
     * 添加黑名单
     *
     * @param param 入参
     *
     * @return 结果
     */
    @PostMapping("/visitor/blacklist/add")
    public void addBlacklist(@RequestBody BlacklistParam param) {
        blacklistService.addBlacklist(param);
    }

    /**
     * 移除黑名单
     *
     * @param id 入参
     *
     * @return 结果
     */
    @DeleteMapping("/visitor/blacklist/remove")
    public void removeBlacklist(Long id) {
        blacklistService.removeBlacklist(id);
    }
}
