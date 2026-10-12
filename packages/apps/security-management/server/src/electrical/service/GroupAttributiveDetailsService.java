package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceLntAndLatCountDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.GroupAttributiveDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.HistoricalAttributeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.AttributiveGroupVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.GroupAttributiveInfoVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GroupAttributiveDetailsService extends IService<GroupAttributiveDetails> {
    List<GroupAttributiveInfoVo> getGroupAttributive(String applicationCode, String groupName);

    List<DeviceLntAndLatCountDTO> getAreaPoint(String applicationCode);

    Map<String, List<AttributiveGroupVo>> getHistoricalAttribute(HistoricalAttributeParam historicalAttributeParam
                                                                ) throws ExecutionException, InterruptedException;
}
