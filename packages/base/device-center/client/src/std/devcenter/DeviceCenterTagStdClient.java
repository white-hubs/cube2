package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceLntAndLatCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.TagPercentageDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceCenterTagStdClient {

    /**
     * 新建Tag
     * 默认为一级标签
     * 创建标签
     *
     * @param createTagCmd 新建Tag参数
     */
    @PostMapping("/tag")
    void createTag(@RequestBody CreateTagCmd createTagCmd);

    /**
     * 绑定Tag<br/>
     *
     * @param tagBindDeviceCmd 绑定Tag参数
     */
    @PostMapping("/tag/bind")
    void bindTag(@RequestBody TagBindDeviceCmd tagBindDeviceCmd);

    /**
     * 解绑Tag<br/>
     * 注意：命令对象需要重新定义
     *
     * @param tagBindDeviceCmd 解绑Tag参数
     */
    @PutMapping("/tag/unbind")
    void unbindTag(@RequestBody TagBindDeviceCmd tagBindDeviceCmd);

    /**
     * 检查标签code是否已存在
     *
     * @param tagCode 标签code
     *
     * @return 标签数量
     */
    @GetMapping("/tag/check/{tagCode}")
    Integer checkTag(@PathVariable("tagCode") String tagCode);

    /**
     * 查询标签下设备占比, 仅支持内置标签
     *
     * @param tagCodes 标签编码列表 {@link Const#CODE_APP_INNER_TAG_TYPE}
     *
     * @return 标签下设备占比
     */
    @GetMapping("/device/tag/percentage/inner-tag")
    List<TagPercentageDTO> tagPercentageByAppInnerTag(@RequestBody Set<String> tagCodes);

    /**
     * 对一组设备进行经纬度分组<br/>
     * eg: 一个应用所导入的设备，对他进行经纬度分组，返回分组后的设备信息
     *
     * @param tagCode 标签编码
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/group/{tagCode}/area-point")
    List<DeviceLntAndLatCountDTO> getDeviceIdsGroupByLntAndLat(@PathVariable("tagCode") String tagCode);

    /**
     * 获取tag关联设备的所有标签
     *
     * @param tagCode tagCode
     * @param prefix  标签前缀
     *
     * @return 标签列表
     */
    @GetMapping("/{tagCode}/refTag/list")
    List<CustomerTagVO> tagRefTagList(@PathVariable("tagCode") String tagCode, @RequestParam("prefix") String prefix);

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
                                             );

    /**
     * 根据标签code和前缀获取第三方设备id集合
     *
     * @param param maP的key:tagCode 标签code 例如： builtin.app::INFORMATION_PUBLISH
     *
     * @return 第三方设备id集合
     */
    @GetMapping("/tag/getThirdPartyIdListByTagCode")
    List<DeviceBaseDetailsDTO> getThirdPartyIdListByTagCode(@RequestBody Map<String, Object> param);

    /**
     * 查询设备所有标签
     *
     * @param deviceIds 设备主键集合
     *
     * @return
     */
    @PostMapping("/device/all-tags")
    Map<Long, List<CustomerTagVO>> deviceAllTag(@RequestBody Set<Long> deviceIds);
}
