package com.guhecloud.cube.base.message2.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.message2.server.base.entity.MsgScene;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgSceneService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgScenePageVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgSceneVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
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

import java.util.Collections;
import java.util.List;

/**
 * 消息场景接口管理
 *
 * @author lanchun
 * @date 2023/3/7 16:16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgSceneController {

    private final MsgSceneService msgSceneService;

    /**
     * 消息场景新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/msg-scene")
    public Long save(@Valid @RequestBody MsgSceneModifyParam param) {
        return msgSceneService.save(param);
    }

    /**
     * 消息场景修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/msg-scene/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody MsgSceneUpdateParam param) {
        this.msgSceneService.update(id, param);
    }

    /**
     * 消息场景详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/msg-scene/{id}")
    public MsgSceneVO getMsgScene(@PathVariable("id") Long id) {
        MsgScene msgScene = this.msgSceneService.getById(id);
        Assert.isNotFound(msgScene, "场景不存在");
        return ObjectUtils.copying(msgScene, MsgSceneVO.class);
    }

    /**
     * 消息场景分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-scene/page")
    public Pagination<MsgScenePageVO> msgScenePage(@Valid MsgSceneQueryParam param) {
        Page<MsgScene> msgScenePage = this.msgSceneService.msgScenePage(param);
        List<MsgScenePageVO> msgScenePageVOS = ObjectUtils.copying(msgScenePage.getRecords(), MsgScenePageVO.class);
        if (CollectionUtils.isEmpty(msgScenePageVOS)) {
            return Pagination.empty();
        }
        return PagingUtils.convert(msgScenePage, msgScenePageVOS);
    }

    /**
     * 消息场景列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-scenes")
    public List<MsgSceneVO> msgScenes(@Valid MsgSceneQueryParam param) {
        List<MsgScene> msgScenes = this.msgSceneService.msgScenes(param);
        if (CollectionUtils.isEmpty(msgScenes)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(msgScenes, MsgSceneVO.class);
    }

    /**
     * 消息场景删除
     *
     * @param id
     */
    @DeleteMapping("/msg-scene/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.msgSceneService.remove(id);
    }
}
