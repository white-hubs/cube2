package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.FriendLinkService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.FriendLinkVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友情链接Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/friend-link")
@Validated
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    /**
     * 新增
     */
    @PostMapping
    public void add(@Validated @RequestBody FriendLinkParam param) {
        friendLinkService.add(param);
    }

    /**
     * 编辑
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Validated @RequestBody FriendLinkParam param) {
        friendLinkService.update(id, param);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public FriendLinkVO detail(@PathVariable Long id) {
       return friendLinkService.detail(id);
    }

    /**
     * 启用、禁用
     */
    @PutMapping("/{id}/status")
    public void toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        friendLinkService.toggleStatus(id, status);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        friendLinkService.delete(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public List<FriendLinkVO> list(@RequestParam(required = false) Integer status) {
       return friendLinkService.list(status);
    }

}
