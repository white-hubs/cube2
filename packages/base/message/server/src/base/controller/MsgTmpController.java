package com.guhecloud.cube.base.message2.server.base.controller;

import com.guhecloud.cube.base.message2.server.base.param.MsgTmpCopyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgTmpService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import com.guhecloud.platform.base.model.Pagination;
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

/**
 * 消息模板管理
 *
 * @author lanchun
 * @date 2023/6/25 15:51
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgTmpController {

    private final MsgTmpService msgTmpService;

    /**
     * 模板新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/msg-tmp")
    public Long save(@Valid @RequestBody MsgTmpModifyParam param) {
        return this.msgTmpService.save(param);
    }

    /**
     * 模板修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/msg-tmp/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody MsgTmpUpdateParam param) {
        this.msgTmpService.update(id, param);
    }

    /**
     * 模板分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-tmp/page")
    public Pagination<MsgTmpPageVO> getMsgTmpPage(@Valid MsgTmpQueryParam param) {
        return this.msgTmpService.getMsgTmpPage(param);
    }

    /**
     * 模板复制
     *
     * @param param
     */
    @PostMapping("/msg-tmp/copy")
    public void copyChannelMsgTmp(@Valid @RequestBody MsgTmpCopyParam param) {
        this.msgTmpService.copyChannelMsgTmp(param);
    }

    /**
     * 模板删除
     *
     * @param id
     */
    @DeleteMapping("/msg-tmp/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.msgTmpService.remove(id);
    }
}
