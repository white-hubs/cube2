package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.scheduled;

import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/26 15:06
 * @Description: 大华区域数据同步
 */

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitAreaSyncTask {

    private final LampAreaService lampAreaService;
    private final VisitAreaService visitAreaService;

    @JobAnnotation(name = "visitorAreaInfoSyncTask", cron = "0 0 0/1 * * ?", desc = "访客管理-大华区域数据定时同步")
    public void visitorAreaInfoSyncTask(String param) {
        List<AreaSelectResult> areaList = lampAreaService.getAreaList(new KeyWordPageParam());
        if (CollectionUtils.isEmpty(areaList)) {
            return;
        }
        List<Long> areaIdList = areaList.stream().map(AreaSelectResult::getId).distinct().collect(Collectors.toList());
        List<VisitAreaPO> visitAreaPOS = visitAreaService.listByIds(areaIdList);
        Map<Long, VisitAreaPO> areaPOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(visitAreaPOS)) {
            areaPOMap = visitAreaPOS.stream()
                    .collect(Collectors.toMap(VisitAreaPO::getAreaId, Function.identity()));
        }
        List<VisitAreaPO> updateList = new ArrayList<>();
        List<VisitAreaPO> insertList = new ArrayList<>();
        for (AreaSelectResult areaSelectResult : areaList) {
            VisitAreaPO visitAreaPO = areaPOMap.get(areaSelectResult.getId());
            if (Objects.nonNull(visitAreaPO)
                && !StringUtils.equals(visitAreaPO.getAreaName(), areaSelectResult.getName())) {
                visitAreaPO.setAreaName(areaSelectResult.getName());
                updateList.add(visitAreaPO);
            } else {
                VisitAreaPO po = new VisitAreaPO();
                po.setId(areaSelectResult.getId());
                po.setNormal(true);
                po.setAreaName(areaSelectResult.getName());
                po.setAreaId(areaSelectResult.getId());
                po.setCreateUserName("system");
                insertList.add(po);
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            visitAreaService.updateBatchById(updateList);
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            visitAreaService.saveBatch(insertList);
        }

    }

}
