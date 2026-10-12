package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.controller;

import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceLntAndLatCountDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.HistoricalAttributeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.GroupAttributiveDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.AttributiveGroupVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.GroupAttributiveInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupAttributiveDetailsController {
    private final GroupAttributiveDetailsService groupAttributiveDetailsService;

    /**
     * @Description 分组信息
     */
    @GetMapping("/group-attributive/{applicationCode}")
    public List<GroupAttributiveInfoVo> getGroupAttributive(@PathVariable String applicationCode,
                                                            @RequestParam(value = "groupName", required = false) String groupName
                                                           ) {
        return groupAttributiveDetailsService.getGroupAttributive(applicationCode, groupName);
    }

    /**
     * @Description 区域点位
     */
    @GetMapping("/area-point/{applicationCode}")
    public List<DeviceLntAndLatCountDTO> getAreaPoint(@PathVariable String applicationCode) {
        return groupAttributiveDetailsService.getAreaPoint(applicationCode);
    }

    /**
     * @Description 历史数据
     */
    @PostMapping("/historical-attribute")
    public Map<String, List<AttributiveGroupVo>> getHistoricalAttribute(@RequestBody HistoricalAttributeParam historicalAttributeParam
                                                                       ) throws ExecutionException, InterruptedException {
        return groupAttributiveDetailsService.getHistoricalAttribute(historicalAttributeParam);
    }

}
