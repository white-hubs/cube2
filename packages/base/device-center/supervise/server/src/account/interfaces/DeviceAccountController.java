package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceAccountCollectCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceAccountCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceAccountQueryService;
import com.guhecloud.cube.srv.gen.identity.client.QrcodeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

// TODO 22-08-29 ：由于需求调整（取消设备台账入账，设备台账原功能不变，其中的设备直接来自于物联设备，不再导入）

/**
 * <p>
 * 设备台账 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-06-20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAccountController {

    private final DeviceAccountCommandService commandService;
    private final DeviceAccountCollectCommandService accountCollectCommandService;
    private final DeviceAccountQueryService queryService;

    private final QrcodeClient qrcodeClient;

    // 详情
  /*  @GetMapping("/device-account/{id}")
    public DeviceAccountVO getVOById(@PathVariable("id") Long id) {
        QrcodeVO qrcode = qrcodeClient.getQrcodeByEntityId(id.toString());
        DeviceAccountVO deviceAccountVO = queryService.getVOById(id);
        if (Objects.nonNull(qrcode)) {
            deviceAccountVO.setConfigJson(qrcode.getConfigJson());
        }
        return deviceAccountVO;
    }
*/

    // 分页查询
   /* @GetMapping("/device-account/page")
    public Pagination<DeviceAccountPageVO> page(DeviceAccountQuery pageQuery) {
        return queryService.getPage(pageQuery);
    }
*/
    /**
     * 列表查询
     */
 /*   @GetMapping("/device-account/list")
    public List<DeviceAccountPageVO> list(DeviceAccountQuery query) {
        return queryService.getList(query);
    }
*/
  /*  @GetMapping("/device/account/list")
    public List<DeviceNameCollectionVO> list(Long genreId, Long areaId) {
        */

    /**
     * 类型->区域-> 设备列表
     * 设备列表与台账求交集
     *//*
        DeviceInstanceCollectionQuery deviceInstanceCollectionQuery = new DeviceInstanceCollectionQuery();
        deviceInstanceCollectionQuery.setArea(areaId);
        deviceInstanceCollectionQuery.setGenreId(genreId);
        List<DeviceInstanceCollectionDTO> list = deviceInstanceClient.list(deviceInstanceCollectionQuery);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<DeviceAccountPO> deviceAccountPOS = queryService.listByIds(
                list.stream().map(DeviceInstanceCollectionDTO::getId).collect(Collectors.toSet())
        );

        Map<Long, String> map = list.stream().collect(Collectors
                .toMap(DeviceInstanceCollectionDTO::getId,
                        DeviceInstanceCollectionDTO::getName));
        List<DeviceNameCollectionVO> deviceNameCollectionVOS = ObjectUtils.copying(deviceAccountPOS, DeviceNameCollectionVO.class);
        deviceNameCollectionVOS.forEach(deviceNameCollectionVO -> {
            deviceNameCollectionVO.setName(map.get(deviceNameCollectionVO.getId()));
        });

        return deviceNameCollectionVOS;

    }
*/
    // 获取设备台账ID
    @GetMapping("/device-account/deviceIds")
    public List<Long> getDeviceIds() {
        return queryService.getDeviceIds();
    }

    /**
     * 退役
     */
    @PostMapping("/device-account/{id}/decommissioning")
    public void decommissioning(@PathVariable("id") Long id) {
        commandService.decommissioning(id);
    }

    /**
     * 报废
     */
    @PostMapping("/device-account/{id}/scrapping")
    public void scrapping(@PathVariable("id") Long id) {
        commandService.scrapping(id);
    }

    // 新增
    @PostMapping("/device-account/batch")
    public void addBatch(@RequestBody Set<Long> ids) {
        commandService.addBatch(ids);
    }

    /**
     * 设备收藏
     */
    @PostMapping("/device-account/{deviceId}/collect")
    public void collect(@PathVariable Long deviceId) {
        accountCollectCommandService.collect(deviceId);
    }

    /**
     * 取消收藏
     */
    @PostMapping("/device-account/{deviceId}/collect/cancel")
    public void cancel(@PathVariable Long deviceId) {
        accountCollectCommandService.cancel(deviceId);
    }

    // 删除
    @DeleteMapping("/device-account/{id}")
    public void remove(@PathVariable Long id) {
        commandService.remove(id);
    }

    // 批量删除
    @DeleteMapping("/device-account/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
