package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 协议管理Controller
 * 提供协议相关的查询接口
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/v3/protocol")
@RequiredArgsConstructor
public class V3ProtocolController {

    private final V3DictItemService v3DictItemService;

    /**
     * 查询所有协议列表
     *
     * @return 协议列表
     */
    @GetMapping("/list")
    public List<DictItemVO> listAll() {
        return v3DictItemService.listAllByDictId(DictEnum.PROTOCOL, true);
    }
}
