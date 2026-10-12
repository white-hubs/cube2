package com.guhecloud.cube.base.device.center.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceAttrCmdMapper;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceInstanceExecuteCommandVO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceInstanceExecuteCommandQuery;
import com.guhecloud.cube.base.device.center.metadata.vo.ChangeLogByBatchIdVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterDeviceAttrCmdLogController {

    private final UserClient userClient;
    private final ExtDeviceAttrCmdMapper executeCommandMapper;

    /**
     * 设配控制是否需要密码
     *
     * @param deviceIds 设备id集合
     *
     * @return 设备id集合, key为设备id, value为是否需要密码
     */
    @PostMapping("/device/instance/running/attr/devices-pwd-need")
    List<Pair<String, Boolean>> devicesPwdNeed(@RequestBody Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return executeCommandMapper.devicesPwdNeed(deviceIds).stream()
                .map(item -> new Pair<>(item.getKey(), StringUtils.isNotEmpty(item.getVal())))
                .collect(Collectors.toList());
    }

    @PostMapping("/device/instance/attribute/change/log/page/post")
    public Pagination<DeviceInstanceExecuteCommandVO> pageChangeLogPost(@RequestBody DeviceInstanceExecuteCommandQuery query) {
        return this.pageChangeLog(query);
    }

    @GetMapping("/device/instance/attribute/change/log/page")
    public Pagination<DeviceInstanceExecuteCommandVO> pageChangeLog(DeviceInstanceExecuteCommandQuery query) {
        return PagingUtils.convert(getDeviceInstanceExecuteCommandVOPage(query));
    }

    @GetMapping("/device/instance/attribute/change/logByTenant/page")
    public Pagination<DeviceInstanceExecuteCommandVO> pageChangeLogByTenant(DeviceInstanceExecuteCommandQuery query) {
        SessionContextHolder.setRequester(new Requester(SessionContextHolder.getRequester().getUri(), SessionContextHolder.getRequester()
                .getHost(), SessionContextHolder.getRequester().getDomain(), SessionContextHolder.getRequester()
                                                                .getAuthorization(), query.getTenant(), SessionContextHolder.getRequester()
                                                                .getTerminal(), SessionContextHolder.getRequester()
                                                                .getVersion(), SessionContextHolder.getRequester()
                                                                .getSignature(), SessionContextHolder.getRequester().getPermission()));
        var page = getDeviceInstanceExecuteCommandVOPage(query);
        SessionContextHolder.clearSessionContext();
        return PagingUtils.convert(page);
    }

    private Page<DeviceInstanceExecuteCommandVO> getDeviceInstanceExecuteCommandVOPage(DeviceInstanceExecuteCommandQuery query) {
        var page = executeCommandMapper.page(
                PagingUtils.convert(query),
                query
                                            );
        var records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(executeCommandVO -> {
                UserDTO userByUserName;
                if (Objects.nonNull(executeCommandVO.getCreatorId()) &&
                    ActorTypeEnum.USER.name().equals(executeCommandVO.getCreatorType())) {
                    userByUserName = userClient.getUser(Long.valueOf(executeCommandVO.getCreatorId()));
                } else {
                    userByUserName = userClient.getUserByUserName(executeCommandVO.getCreateBy());
                }
                if (Objects.isNull(userByUserName)) {
                    executeCommandVO.setCreateBy("系统");
                } else {
                    executeCommandVO.setCreateBy(userByUserName.getName());
                }
            });
        }
        return page;
    }

    @GetMapping("/device/instance/attribute/change/{batchId}/list")
    public List<ChangeLogByBatchIdVO> getChangeLogByBatchId(@PathVariable("batchId") Long batchId) {
        return executeCommandMapper.listByBatchId(batchId);
    }

    @GetMapping("/device/instance/{deviceInstanceId}/attribute/change/log/page")
    public Pagination<DeviceInstanceExecuteCommandVO> pageChangeLogByDeviceId(@PathVariable("deviceInstanceId") Long deviceInstanceId,
                                                                              Paging paging
                                                                             ) {
        var deviceInstanceExecuteCommandQuery = new DeviceInstanceExecuteCommandQuery();
        deviceInstanceExecuteCommandQuery.setDeviceInstanceId(deviceInstanceId);
        deviceInstanceExecuteCommandQuery.setPageNumber(paging.getPageNumber());
        deviceInstanceExecuteCommandQuery.setPageSize(paging.getPageSize());
        return PagingUtils.convert(getDeviceInstanceExecuteCommandVOPage(deviceInstanceExecuteCommandQuery));
    }
}