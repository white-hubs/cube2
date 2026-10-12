package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.service.CenterDeviceCustomerService;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBindTagsCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagBatchBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class CenterDeviceCustomerTagController {

    @Resource
    private CenterDeviceCustomerService centerDeviceCustomerService;

    /**
     * 新建/编辑Tag
     *
     * @param createTagCmd Tag参数
     */
    @PostMapping("/customer/tag")
    public void editTag(@RequestBody CustomerTagParam createTagCmd) {
        centerDeviceCustomerService.createTagIfNeed(createTagCmd);
    }

    /**
     * 删除客户自定义标签
     *
     * @param id 自定义标签id
     */
    @DeleteMapping("/customer/tag/delete/{id}")
    public void deleteCustomerTag(@PathVariable String id) {
        centerDeviceCustomerService.deleteTag(id);
    }

    /**
     * tag启用状态修改
     *
     * @param status 状态
     */
    @PutMapping("/customer/{tagId}/enable/{status}")
    public void editStatus(@PathVariable String tagId, @PathVariable String status) {
        centerDeviceCustomerService.editStatus(tagId, status);
    }

    /**
     * 分页查询标签
     *
     * @param param 查询参数
     *
     * @return tag信息
     */
    @GetMapping("/tag/{type}/page")
    public Pagination<CustomerTagVO> customerTagPage(@PathVariable String type, CustomerTagPageParam param) {
        return centerDeviceCustomerService.customerTagPage(type, param);
    }

    /**
     * 自定义标签下拉列表
     *
     * @return 自定义标签下拉列表
     */
    @GetMapping("/tag/customer/list")
    public List<CustomerTagVO> customerTagList() {
        return centerDeviceCustomerService.customerTagList();
    }

    /**
     * 查询设备自定义标签列表
     *
     * @param deviceId 设备id
     *
     * @return 标签列表
     */
    @GetMapping("/customer/tag/{deviceId}/list")
    public List<CustomerTagVO> deviceTagList(@PathVariable String deviceId) {
        return centerDeviceCustomerService.deviceTagList(deviceId);
    }

    /**
     * 设备绑定自定义标签
     *
     * @param bindTagCmd 操作命令
     */
    @PostMapping("/customer/tag/bind")
    public void deviceBindTag(@RequestBody DeviceBindTagsCmd bindTagCmd) {
        centerDeviceCustomerService.deviceBindTag(bindTagCmd);
    }

    /**
     * 批量设备绑定自定义标签
     *
     * @param bindTagCmd 操作命令
     */
    @PostMapping("/customer/tag/batch/bind")
    public void batchBindTag(@RequestBody TagBatchBindDeviceCmd bindTagCmd) {
        centerDeviceCustomerService.batchBindTag(bindTagCmd);
    }

    /**
     * 获取tag关联设备的所有标签
     *
     * @param tagCode tagCode
     * @param prefix  标签前缀
     *
     * @return 标签列表
     */
    @GetMapping("/{tagCode}/refTag/list")
    public List<CustomerTagVO> tagRefTagList(@PathVariable("tagCode") String tagCode, String prefix) {
        return centerDeviceCustomerService.tagRefTagList(tagCode, prefix);
    }

    /**
     * 获取tag关联设备的所有标签
     *
     * @param tagCode     tagCode
     * @param prefixCodes 标签前缀集合
     *
     * @return 标签列表
     */
    @GetMapping("/{tagCode}/refTags")
    public List<CustomerTagVO> tagRefTagList(@PathVariable("tagCode") String tagCode,
                                             @RequestParam(value = "prefixCodes", required = false) Set<String> prefixCodes
                                            ) {
        return centerDeviceCustomerService.tagRefTagList(tagCode, prefixCodes);
    }

    /**
     * 查询业务标签下,关联指定自定义标签的所有设备id
     *
     * @param businessTagCode  业务标签Code
     * @param customerTagCodes 自定义标签Codes
     *
     * @return 设备id列表
     */
    @GetMapping("/{businessTagCode}/deviceIds/customerTags")
    Set<Long> queryBusinessCustomerTagDevices(@PathVariable("businessTagCode") String businessTagCode,
                                              @RequestParam("customerTagCodes") List<String> customerTagCodes
                                             ) {
        return centerDeviceCustomerService.queryBusinessCustomerTagDevices(businessTagCode, customerTagCodes);
    }

    /**
     * 根据标签code和前缀获取第三方设备id集合
     *
     * @param param 标签code和前缀
     *
     * @return 设备集合
     */
    @GetMapping("/tag/getThirdPartyIdListByTagCode")
    List<DeviceBaseDetailsDTO> getThirdPartyIdListByTagCode(@RequestBody Map<String, Object> param) {
        return centerDeviceCustomerService.getThirdPartyIdListByTagCode(param);
    }
}
