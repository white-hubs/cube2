package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query.DeviceInstanceReliabilityQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceInstanceReliabilityExportVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceInstanceReliabilityVO;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.vo.DevcieCountVo;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/3 14:18
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReliabilityController {
    @Resource
    private FaultClient faultClient;

    @Resource
    private DeviceCenterStdClient deviceCenterStdClient;

    @Resource
    private DeviceBaseStatisticsClient deviceBaseStatisticsClient;

    @GetMapping("/device/instance/analysis/reliability")
    public List<DeviceInstanceReliabilityVO> reliability(@Validated DeviceInstanceReliabilityQuery query) {
        List<DeviceInstanceReliabilityVO> deviceInstanceReliabilityVOS = new ArrayList<>();
        /*
         * 1.获取设备的在线时长
         * 2. 获取设备的故障次数
         */
        var qry = new DeviceBaseInfoPageQry();
        qry.setMatchedDeviceIds(new ArrayList<>(query.getDeviceIds()));
        var pagination = deviceCenterStdClient.pageOfBaseInfo(qry);
        //获取设备状态统计信息
        var deviceStateStatisticsMap = deviceBaseStatisticsClient.deviceStatistics(query.getDeviceIds());
        var archivesInfoMap = pagination.getRecords().stream()
                .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, o -> o));
        var iterator = archivesInfoMap.entrySet().iterator();
        var faultCountMap = faultClient
                .getDeviceCount(query.getDeviceIds().stream().toList())
                .stream().collect(Collectors.toMap(DevcieCountVo::getDeviceId, DevcieCountVo::getNum));
        while (iterator.hasNext()) {
            var next = iterator.next();
            var key = next.getKey();
            var instanceReliabilityVO = new DeviceInstanceReliabilityVO();
            instanceReliabilityVO.setId(key);
            instanceReliabilityVO.setCode(next.getValue().getCode());
            instanceReliabilityVO.setName(next.getValue().getName());
            instanceReliabilityVO.setWorkerDay(
                    deviceStateStatisticsMap.containsKey(key) ? deviceStateStatisticsMap.get(key).getOnlineTime() / (24 * 60) : 0);
            instanceReliabilityVO.setTroubleRunNumber(faultCountMap.containsKey(key) ? faultCountMap.get(key).intValue() : 0);
            deviceInstanceReliabilityVOS.add(instanceReliabilityVO);
        }
        return deviceInstanceReliabilityVOS;
    }

    /**
     * 可靠性分析导出
     */
    @PostMapping("/device/instance/analysis/reliability/export")
    public void export(@RequestBody DeviceInstanceReliabilityQuery query) throws IOException {
        var attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        var excelName = URLEncoder.encode("可靠性分析", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        var reliability = reliability(query);
        var deviceInstanceReliabilityExportVOS = ObjectUtils.copying(reliability, DeviceInstanceReliabilityExportVO.class);
        EasyExcel.write(response.getOutputStream(), DeviceInstanceReliabilityExportVO.class).sheet("可靠性分析")
                .doWrite(deviceInstanceReliabilityExportVOS);
    }
}
