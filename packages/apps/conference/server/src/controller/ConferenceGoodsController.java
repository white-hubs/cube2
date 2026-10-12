package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceGoods;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceGoodsService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * 会议室物品
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceGoodsController {

    private final ConferenceGoodsService conferenceGoodsService;

    /**
     * 功能描述： 会议室物品分页
     */
    @GetMapping(value = "/conference-goods/page")
    public Pagination<ConferenceGoods> getPage(ConferenceGoodsQueryParam param) {
        return conferenceGoodsService.getPage(param);
    }

    /**
     * 功能描述： 会议室物品选择
     */
    @GetMapping(value = "/conference-goods/list")
    public List<ConferenceGoods> getGoodsList(ConferenceGoodsQueryParam param) {
        return conferenceGoodsService.getGoodsList(param);
    }

    /**
     * 功能描述： 会议室物品新增
     */
    @PostMapping("/add-conference-goods")
    public void add(@RequestBody ConferenceGoodsParam param) {
        conferenceGoodsService.addConferenceGoods(param);
    }

    /**
     * 功能描述： 会议室物品更新
     */
    @PutMapping(value = "/conference-goods/{id}")
    public void update(@PathVariable Long id, @RequestBody ConferenceGoodsParam param) {
        conferenceGoodsService.updateConferenceGoods(id, param);
    }

    /**
     * 功能描述： 会议室物品删除
     */
    @DeleteMapping(value = "/conference-goods/delete/{id}")
    public void delete(@PathVariable Long id) {
        conferenceGoodsService.delete(id);
    }

    /**
     * 功能描述： 会议室物品批量删除
     */
    @PostMapping(value = "/conference-goods/batch-delete")
    public void deleteBatch(@RequestBody @NotBlank(message = "ids不能为空") List<Long> ids) {
        conferenceGoodsService.batchDelete(ids);
    }
}
