package com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.param.PerimeterStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.vo.AreaOfflineVo;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PerimeterServiceImpl implements PerimeterService {

    private final SecurityCommonService securityCommonService;

    private final SecurityCommonMapper securityCommonMapper;

    @Override
    public Map<String, Object> getAreaOfflineNum(PerimeterStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        List<SecurityDevice> list = securityCommonService.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                                       .eq(SecurityDevice::getApplicationCode, param.getApplicationCode()));
        List<Long> collect = list.stream().map(SecurityDevice::getDeviceId).distinct().collect(Collectors.toList());
        List<AreaOfflineVo> areaOffline = new ArrayList<>();
        Integer num = 0;
        if (CollectionUtils.isNotEmpty(collect)) {
            areaOffline = securityCommonMapper.getAreaStatusNum(collect);
            for (AreaOfflineVo areaOfflineVo : areaOffline) {
                num += areaOfflineVo.getOfflineNum();
            }
        }
        remap.put("totalNum", collect.size());
        remap.put("offlineNum", num);
        remap.put("areaOfflineareaOffline", areaOffline);
        return remap;
    }

}
