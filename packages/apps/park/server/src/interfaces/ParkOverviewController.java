package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomRateVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkOverviewController {

    private final TenantClient tenantClient;

    private final AreaClient areaClient;

    private final RoomRentRecordMapper roomRentRecordMapper;

    /**
     * @Description 获取租户信息
     * @Author lrj
     */
    @GetMapping("/getTenant")
    public TenantDTO getTenant() {
        System.out.println("租户信息");
        TenantDTO byCode = tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
        return byCode;
    }

    /**
     * @Description 可租面积鱼出租率
     * @Author lrj
     */
    @GetMapping("/getRoomStatistics")
    public Map<String, Object> getRoomStatistics() {
        HashMap<String, Object> remap = new HashMap<>();
        //房间面积
        BigDecimal roomFloorRageSum = roomRentRecordMapper.getRoomFlooRageSum();
        //已租房间面积
        BigDecimal bigDecimal = roomRentRecordMapper.getFlooRageSum();
        //可租面积
        BigDecimal subtract = roomFloorRageSum.subtract(bigDecimal);

        remap.put("leaseNum", subtract);
        remap.put("leaseTotal", roomFloorRageSum);
        return remap;
    }

    /**
     * @Description 区域租率
     * @Author lrj
     */
    @GetMapping("/getRoomRateList")
    public List<RoomRateVo> getRoomRateList() {
        List<RoomRateVo> roomRateList = roomRentRecordMapper.getRoomRateList();
        roomRateList.forEach(s -> {
            String areaNameById = areaClient.getAreaNameById(s.getAreaId(), SessionContextHolder.getRequester().getTenant());
            s.setAreaName(areaNameById);
            s.setNum(s.getNum().setScale(2, RoundingMode.HALF_UP));
        });
        return roomRateList;
    }

}
