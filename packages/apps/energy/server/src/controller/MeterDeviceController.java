package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.dto.InstanceListByGenreDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyValueUpdateBatchParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceUpdateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceStatisticsParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataTimeFlagVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceDetailVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceExcelVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceVOV1;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeInstanceVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeRecordVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

/**
 * @author lrj
 * @Description 仪表管理
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterDeviceController {
    private final MeterDeviceService meterDeviceService;

    /**
     * 功能描述： 仪表管理添加
     */
    @PostMapping(value = "/meter-device")
    public Long add(@Valid @RequestBody MeterDeviceParam param) {
        return meterDeviceService.add(param);
    }

    /**
     * 功能描述： 仪表管理批量添加
     */
    @PostMapping(value = "/meter-device/batchAdd")
    public void addBatch(@Valid @RequestBody List<MeterDeviceParam> param) {
        meterDeviceService.addBatch2(param);
    }

    /**
     * 功能描述： 仪表管理修改
     */
    @PutMapping(value = "/meter-device/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody MeterDeviceParam param) {
        meterDeviceService.update(id, param);
    }

    /**
     * 功能描述： 仪表管理删除
     */
    @DeleteMapping(value = "/meter-device/{deviceId}")
    public void delete(@PathVariable Long deviceId) {
        meterDeviceService.deleteAndCheck(deviceId);
    }

    /**
     * 功能描述： 仪表管理批量删除
     */
    @PostMapping(value = "/meter-device/batchdelete")
    public void deleteBatch(@RequestBody List<String> ids) {
        meterDeviceService.batchDelete(ids);
    }

    /**
     * 功能描述： 仪表管理分页
     */
    @GetMapping(value = "/meter-device/page")
    public Pagination<MeterDeviceVo> getPage(MeterDeviceQueryParam param) {
        return meterDeviceService.getPage(param);
    }

    /**
     * 功能描述： 仪表管理列表查询
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping(value = "/meter-device/drop-down/list")
    public List<KeyValueDTO> getDropdownList(MeterDeviceQueryParam param) {
        return meterDeviceService.getDropdownList(param);
    }

    /**
     * 功能描述： 能量仪表管理
     */
    @GetMapping(value = "/meter-device/list")
    public List<MeterDeviceVo> getEnergyMeters() {
        return meterDeviceService.getEnergyMeters();
    }

    /**
     * 根据仪表类型查询仪表
     */
    @GetMapping("/meter-device/getByIdAndCategory/{id}/{instrumentType}")
    public List<MeterDeviceVo> getByIdAndCategory(@PathVariable("id") String id, @PathVariable("instrumentType") String instrumentType) {
        return meterDeviceService.getByIdAndCategory(id, instrumentType);
    }

    /**
     * 配置上级仪表
     *
     * @param id       当前设备id
     * @param parentId 父级设备id
     */
    @PutMapping("/meter-device/config/superiorInstrument/{id}/{parentId}")
    public void configSuperiorInstrument(@PathVariable("id") String id, @PathVariable("parentId") String parentId) {
        meterDeviceService.configSuperiorInstrument(id, parentId);
    }

    /**
     * 功能描述： 仪表管理详情
     */
    @GetMapping(value = "/meter-device/{id}")
    public MeterDeviceVo getById(@PathVariable("id") String id) {

        return meterDeviceService.getById(id);
    }

    /**
     * 功能描述： app仪表管理详情
     */
    @GetMapping(value = "/aap-meter-device/{id}")
    public MeterDeviceVo getAppById(@PathVariable Long id, Period period) throws URISyntaxException {
        return meterDeviceService.getAppById(id, period);
    }

    /**
     * @Description 采集分页
     * @Author lrj
     */
    @GetMapping("/meter-statistics")
    public Pagination<KeyValueWithTsDTO> getMeterDeviceStatistics(MeterDeviceStatisticsParam param) {
        return meterDeviceService.getMeterDeviceStatistics(param);
    }

    /**
     * @Description 水电气采集分页
     * @Author lrj
     */
    @GetMapping("/quantity-meter-statistics")
    public Page<DataVo> getQuantityMeterDeviceStatistics(MeterDeviceStatisticsParam param) {
        return meterDeviceService.getQuantityMeterDeviceStatistics(param);
    }

    /**
     * @Description 水电气总用量采集趋势
     * @Author lrj
     */
    @GetMapping("/total-usage")
    public List<DataTimeFlagVo> totalUsage(MeterDeviceStatisticsParam param) {
        return meterDeviceService.totalUsage(param);
    }

    /**
     * @Description 能量总用量采集趋势
     * @Author lrj
     */
    @GetMapping("/energy-usage")
    public DeviceTSDTO energyUsage(MeterDeviceStatisticsParam param) {
        return meterDeviceService.energyUsage(param);
    }

    /**
     * 获取仪表管理中所有的仪表区域信息
     *
     * @return
     */
    @GetMapping("/meter-device/device-area/{instrumentType}")
    public List<MeterDeviceVo> getDeviceAreaArr(@PathVariable("instrumentType") String instrumentType) {
        return meterDeviceService.getDeviceAreaArr(instrumentType);
    }

    /**
     * 获取仪表管理中所有的仪表区域信息
     *
     * @return
     */
    @GetMapping("/meter-device/getAllDeviceAreaArr")
    public List<MeterDeviceVo> getAllDeviceAreaArr() {
        return meterDeviceService.getAllDeviceAreaArr();
    }

    /**
     * 分类获取仪表信息
     *
     * @return
     */
    @GetMapping("/meter-device/getByCategory")
    public List<MeterReadeInstanceVO> getByCategory() {
        return meterDeviceService.getByCategory();
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    @GetMapping("/meter-device/getTree")
    public List<MeterDeviceVOV1> getTree(@RequestParam("instrumentType") String instrumentType) {
        return meterDeviceService.getTree(instrumentType);
    }

    /**
     * 抄表分页
     *
     * @return
     */
    @GetMapping("/meter-device/getReadeList")
    public Page<MeterReadeRecordVo> getReadeList(MeterDeviceQueryParam param) throws ParseException {
        return meterDeviceService.getReadeList(param);
    }

    /**
     * 设备历史
     *
     * @return
     */
    @GetMapping("/meter-device/getDeviceHistory")
    public Page<DataVo> getDeviceHistory(MeterDeviceQueryParam param) {

        return meterDeviceService.getDeviceHistory(param);
    }

    /**
     * 抄表修改
     *
     * @return
     */
    @PutMapping("/meter-device/updateReade")
    public void updateReade(@RequestBody MeterDeviceUpdateParam param) {

        meterDeviceService.updateReade(param);
    }

    /**
     * 仪表管理导表列表
     *
     * @return
     */
    @GetMapping("/meter-device/devices")
    public List<InstanceListByGenreDTO> getDevices() throws URISyntaxException {
        return meterDeviceService.getDevices();
    }

    /**
     * 获取能耗系统中的所有设备的ID
     * 目前用于已经导入设备的初始化
     *
     * @return
     */
    @GetMapping("/meter-device/all/deviceId")
    public List<String> getAllDeviceIdList() {
        List<MeterDevice> deviceList = meterDeviceService.list();
        return deviceList.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
    }

    /**
     * @Description 筛出ids
     * @Author lrj
     */
    @GetMapping("/sieve-out")
    public List<Long> sieveOut() {
        return meterDeviceService.sieveOut();
    }

    /**
     * @Description 仪表区域列表
     * @Author lrj
     */
    @GetMapping("/meterArea")
    public List<String> meterArea() {
        List<MeterDevice> list = meterDeviceService.list();
        List<String> areas = list.stream().map(MeterDevice::getDeviceArea).distinct().collect(Collectors.toList());
        return areas;
    }

    /**
     * 更新设备的最新读数及时间
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/meter-device/{id}/update/prop/value")
    public MeterDeviceVo getLatestDeviceDataFromIot(@PathVariable Long id) {
        return meterDeviceService.getLatestDeviceDataFromIot(id);
    }

    /**
     * 批量更新设备读数
     *
     * @param param idList
     *
     * @return 是否成功
     */
    @PutMapping("/meter-device/update/prop/value/batch")
    public Boolean getLatestDeviceDataFromIotForBatch(@RequestBody EnergyValueUpdateBatchParam param) {
        if (param == null || CollectionUtils.isEmpty(param.idList())) {
            return true;
        }
        param.idList().forEach(meterDeviceService::getLatestDeviceDataFromIot);
        return true;
    }

    /**
     * 获取设备详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping(value = "/meter-device/detail/{id}")
    public MeterDeviceDetailVO getDetailById(@PathVariable("id") String id) {
        return meterDeviceService.getDetailById(id);
    }

    /**
     * @Description 导表
     * @Author lrj
     */
    @GetMapping("/downloadExcel")
    public void downLoadFile(HttpServletResponse response, MeterDeviceQueryParam param) throws IOException {
        List<MeterDeviceExcelVO> excelList = meterDeviceService.getExcelList(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("code", "设备编码");
        writer.addHeaderAlias("deviceName", "仪表名称");
        writer.addHeaderAlias("instrumentName", "仪表类型");
        writer.addHeaderAlias("deviceArea", "所属区域名称");
        writer.addHeaderAlias("readeValue", "当前读数");
        writer.addHeaderAlias("nowReadeValue", "本期用量");
        writer.addHeaderAlias("lastReadeValue", "上期用量");
        writer.addHeaderAlias("parentName", "上级仪表");
        writer.write(excelList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "能耗仪表" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            writer.close();
            if (Objects.nonNull(out)) {
                out.close();
            }
        }
    }

    /**
     * 获取非子级的仪表列表(用于添加下级)
     *
     * @param param 仪表id,仪表类型
     *
     * @return 仪表列表
     */
    @GetMapping("/meter-device/getNotChildren")
    public List<MeterDevice> getNotChildrenDevice(MeterDeviceQueryParam param) {
        return meterDeviceService.getNotChildrenDevice(Long.valueOf(param.getDeviceId()), param.getInstrumentType());
    }
}