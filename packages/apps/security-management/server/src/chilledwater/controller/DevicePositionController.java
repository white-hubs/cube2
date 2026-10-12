package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WaterDeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.DevicePositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeCodeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceDetailVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevicePositionController {

    private final DevicePositionService devicePositionService;

    /**
     * @Description 监测分页
     * @Author lrj
     */
    @GetMapping("/device-monitor")
    public Pagination<DevicePositionPageVo> getDeviceMonitor(WaterDeviceMonitorParam param) {
        return devicePositionService.getDeviceMonitor(param);
    }

    /**
     * @Description 设备详情
     * @Author lrj
     */
    @GetMapping("/detail/{id}")
    public DeviceDetailVo getDetail(@PathVariable Long id) {
        return devicePositionService.getDetail(id);
    }

    /**
     * @Description 商户明细列表
     * @Author lrj
     */
    @GetMapping("/wholesaler-detail")
    public Pagination<WholesalerDeviceVo> getWholesalerDetail(WholesalerDetailParam param) {

        return devicePositionService.getWholesalerDetail(param);
    }

    /**
     * @Description 商户明细列表导表
     * @Author lrj
     */
    @PostMapping("/wholesaler-detail-excel")
    public void wholesalerDetailExcel(HttpServletResponse response, @RequestBody WholesalerDetailParam param
                                     ) throws UnsupportedEncodingException {
        List<WholesalerDeviceVo> wholesalerDeviceVos = devicePositionService.wholesalerDetailExcel(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("num", "序号");
        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("shiftMasterPhone", "联系电话");
        writer.addHeaderAlias("gearPositionCode", "档位号");
        writer.addHeaderAlias("gearPositionArea", "所属区域");
        writer.addHeaderAlias("department", "管理部门");
        writer.addHeaderAlias("deviceCode", "钛泡编号");
        writer.addHeaderAlias("unitTraffic", "冷媒单位流量（m³/h)");
        writer.addHeaderAlias("updateBy", "操作员");
        writer.addHeaderAlias("createTime", "登记日期");
        writer.addHeaderAlias("updateTime", "修改日期");
        writer.setOnlyAlias(true);
        writer.write(wholesalerDeviceVos, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "商户明细" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("商户明细导出异常:", e);
        } finally {
            writer.close();
        }
    }

    /**
     * @Description 设备属性
     * @Author lrj
     */
    @GetMapping("/attribute/{id}")
    public DeviceAttributeVo getAttribute(@PathVariable Long id) {
        return devicePositionService.getAttribute(id);
    }

    /**
     * @Description 设备属性列表
     * @Author lrj
     */
    @GetMapping("/cold-device-attribute-code")
    List<DeviceAttributeCodeVo> getDeviceAttributeCode(@RequestParam("deviceId") Long deviceId) {
        return devicePositionService.getDeviceAttributeCode(deviceId);
    }

    /**
     * @Description 设备属性列表
     * @Author lrj
     */
    @GetMapping("/inner/gear-position-ids")
    public List<Long> getInnerGearPositionIds() {
        return devicePositionService.getGearPositionIds();
    }

}
