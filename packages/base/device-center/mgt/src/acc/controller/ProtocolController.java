package com.guhecloud.cube.iot.mgt.acc.controller;

import com.guhecloud.cube.iot.mgt.base.vo.DictItemThinVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.AccProtocolEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/acc/protocol")
public class ProtocolController {

    private final List<DictItemThinVo> protocolList = new LinkedList<>();

    public ProtocolController() {
        for (AccProtocolEnum value : AccProtocolEnum.values()) {
            DictItemThinVo dictItemThinVo = new DictItemThinVo();
            dictItemThinVo.setId(value.id);
            dictItemThinVo.setItemLabel(value.protocolName);
            dictItemThinVo.setItemValue(value.code);
            protocolList.add(dictItemThinVo);
        }
    }

    @GetMapping("/list")
    public List<DictItemThinVo> list() {
        return protocolList;
    }

}
