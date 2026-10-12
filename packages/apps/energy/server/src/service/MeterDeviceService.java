package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.dto.InstanceListByGenreDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceUpdateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceStatisticsParam;
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

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 描述：仪表管理服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface MeterDeviceService extends IService<MeterDevice> {

    Long add(MeterDeviceParam param);

    void update(Long id, MeterDeviceParam param);

    void delete(Long deviceId);

    /**
     * 检查设备是否被关联
     * 没被其他地方使用则可以删除
     *
     * @param deviceId
     */
    void deleteAndCheck(Long deviceId);

    void batchDelete(List<String> ids);

    MeterDeviceVo getById(String id);

    /**
     * 获取设备详情
     *
     * @param id
     *
     * @return
     */
    MeterDeviceDetailVO getDetailById(String id);

    MeterDeviceVo getAppById(Long id, Period period) throws URISyntaxException;

    Pagination<MeterDeviceVo> getPage(MeterDeviceQueryParam param);

    List<KeyValueDTO> getDropdownList(MeterDeviceQueryParam param);

    List<MeterDeviceExcelVO> getExcelList(MeterDeviceQueryParam param);

    List<MeterDeviceVo> getEnergyMeters();

    /**
     * 批量新增
     *
     * @param param
     */
    void addBatch(List<MeterDeviceParam> param);

    void addBatch2(List<MeterDeviceParam> param);

    /**
     * 分类获取仪表信息
     *
     * @return
     */
    List<MeterReadeInstanceVO> getByCategory();

    /**
     * 根据仪表类型查询仪表
     *
     * @param id
     * @param instrumentType
     *
     * @return
     */
    List<MeterDeviceVo> getByIdAndCategory(String id, String instrumentType);

    /**
     * 配置上级仪表
     *
     * @param id
     * @param parentId
     */
    void configSuperiorInstrument(String id, String parentId);

    /**
     * 获取树形结构
     *
     * @return
     */
    List<MeterDeviceVOV1> getTree(String instrumentType);

    //设备抄表
    Map<String, Object> readeRecord(String tenant) throws URISyntaxException;

    Page<MeterReadeRecordVo> getReadeList(MeterDeviceQueryParam param) throws ParseException;

    Page<DataVo> getDeviceHistory(MeterDeviceQueryParam param);

    void updateReade(MeterDeviceUpdateParam param);

    /**
     * 根据仪表分类获取区域信息
     *
     * @param instrumentType
     *
     * @return
     */
    List<MeterDeviceVo> getDeviceAreaArr(String instrumentType);

    /**
     * 获取所有区域信息
     *
     * @return
     */
    List<MeterDeviceVo> getAllDeviceAreaArr();

    /**
     * @Description 设备列表
     * @Author lrj
     */
    List<InstanceListByGenreDTO> getDevices() throws URISyntaxException;

    List<Long> sieveOut();

    Pagination<KeyValueWithTsDTO> getMeterDeviceStatistics(MeterDeviceStatisticsParam param);

    //水电气热历史分页
    Page<DataVo> getQuantityMeterDeviceStatistics(MeterDeviceStatisticsParam param);

    List<DataTimeFlagVo> totalUsage(MeterDeviceStatisticsParam param);

    DeviceTSDTO energyUsage(MeterDeviceStatisticsParam param);

    /**
     * 从物联网更新设备的当前属性值
     *
     * @param id
     *
     * @return
     */
    MeterDeviceVo getLatestDeviceDataFromIot(Long id);

    List<MeterDevice> getNotChildrenDevice(Long deviceId, String instrumentType);
}