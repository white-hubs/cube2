package com.guhecloud.cube.iot.mgt.base.controller;

import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemUnitVo;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.base.vo.IotSysUnitVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotCompareType;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/base/common-dict")
public class CommonDictController {

    private final List<DictItemVo> iotDataTypeList = new LinkedList<>();
    private final List<DictItemVo> iotCompareTypeList = new LinkedList<>();

    @Resource
    private CommonDictItemService commonDictItemService;

    public CommonDictController() {
        for (IotDataType value : IotDataType.values()) {
            DictItemVo dictItemVo = new DictItemVo();
            dictItemVo.setItemLabel(value.label);
            dictItemVo.setItemValue(value.code);
            iotDataTypeList.add(dictItemVo);
        }

        for (IotCompareType value : IotCompareType.SUPPORT_OPERATORS_NOW) {
            DictItemVo dictItemVo = new DictItemVo();
            dictItemVo.setItemLabel(value.label);
            dictItemVo.setItemValue(value.code);
            iotCompareTypeList.add(dictItemVo);
        }
    }

    @GetMapping("/iotDataType")
    public List<DictItemVo> iotDataType() {
        return iotDataTypeList;
    }

    @GetMapping("/prop-unit")
    public List<DictItemUnitVo> propUnit() {
        List<DictItemUnitVo> unitList = new ArrayList<>();
        List<IotSysUnitVo> iotSysUnitVos = commonDictItemService.queryUnitList();
        for (IotSysUnitVo iotSysUnitVo : iotSysUnitVos) {
            DictItemUnitVo dictItemVo = new DictItemUnitVo();
            dictItemVo.setItemLabel(iotSysUnitVo.getUnitName());
            dictItemVo.setId(iotSysUnitVo.getUnitCode());
            dictItemVo.setStatus(iotSysUnitVo.getUnitStatus());
            dictItemVo.setRemark(iotSysUnitVo.getRemark());
            unitList.add(dictItemVo);
        }
        return unitList;
    }

    @DeleteMapping("/delete-unit")
    public Integer deleteUnit(@RequestParam(value = "id") String id) {
        return commonDictItemService.deleteUnit(Long.parseLong(id));
    }

    @PutMapping("/save-unit")
    public void saveUnit(@RequestBody IotSysUnitVo iotSysUnitVo) {
        commonDictItemService.saveUnit(iotSysUnitVo);
    }

    @GetMapping("/event-type")
    public List<DictItemVo> eventType() {
        return commonDictItemService.queryListByDictId(InnerDicEnum.event_type.id);
    }

    @GetMapping("/compare-type")
    public List<DictItemVo> compareType() {
        return iotCompareTypeList;
    }

    @GetMapping("/loadBottomItemTree/{itemId}")
    public DictItemVo loadBottomItemTree(@PathVariable Long itemId) {
        return commonDictItemService.loadBottomItemTree(itemId);
    }

}
