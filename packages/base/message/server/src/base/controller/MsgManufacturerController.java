package com.guhecloud.cube.base.message2.server.base.controller;

import com.guhecloud.cube.base.message2.server.base.entity.MsgManufacturer;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgManufacturerService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgManufacturerVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

import java.util.List;

/**
 * 消息厂家接口
 *
 * @author lanchun
 * @date 2021/11/10 13:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgManufacturerController {

    private final MsgManufacturerService msgManufacturerService;

    /**
     * 消息厂家新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/msg-manufacturer")
    public Long save(@Valid @RequestBody MsgManufacturerModifyParam param) {
        return msgManufacturerService.save(param);
    }

    /**
     * 消息厂家修改
     *
     * @param code
     * @param param
     */
    @PutMapping("/msg-manufacturer/{code}")
    public void update(@PathVariable("code") @Min(value = 1, message = "消息厂家编码不能小于1") String code,
                       @Valid @RequestBody MsgManufacturerUpdateParam param
                      ) {
        msgManufacturerService.update(code, param);
    }

    /**
     * 消息厂家详情
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/msg-manufacturer/{code}")
    public MsgManufacturerVO getMsgChannel(@PathVariable("code") String code) {
        MsgManufacturer msgManufacturer = msgManufacturerService.getByCode(code);
        Assert.isNotFound(msgManufacturer, "消息厂家不存在");
        return ObjectUtils.copying(msgManufacturer, MsgManufacturerVO.class);
    }

    /**
     * 消息厂家列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-manufacturers")
    public List<MsgManufacturerVO> listMsgManufacturers(MsgManufacturerQueryParam param) {
        return ObjectUtils.copying(this.msgManufacturerService.listMsgManufacturers(param), MsgManufacturerVO.class);
    }

    /**
     * 消息厂家删除
     *
     * @param code
     */
    @DeleteMapping("/msg-manufacturer/{code}")
    public void remove(@PathVariable("code") String code) {
        msgManufacturerService.remove(code);
    }

}
