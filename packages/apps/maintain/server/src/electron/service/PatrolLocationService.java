package com.guhecloud.cube.srv.gen.maintain.server.electron.service;//package com.guhecloud.cube.srv.gen.maintain.electron.server.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationByBluetoothParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationDistributeStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PatrolLocationService extends IService<PatrolLocation> {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    PatrolLocationVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    Pagination<PatrolLocationVO> queryLocationPage(PatrolLocationPageParam param);

    List<PatrolLocationVO> queryLocationByPlan(Long planId);

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    Boolean insert(PatrolLocationParam param);

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    Boolean update(PatrolLocationParam param);

    Boolean update(String pastNfcCode, String nfcCode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);

    /**
     * @Author wql
     * @Description 根据蓝牙编号查询巡更点列表
     * @Date 13:59 2023/10/25
     **/
    List<PatrolLocationVO> listByBluetooth(PatrolLocationByBluetoothParam param);

    LocationDistributeStatisticsVO locationDistributeStatistics(StatisticsQueryParam param);

    List<PatrolLocationVO> listByRoute(Long routeId);

    void getTemplateExcel(HttpServletRequest request, HttpServletResponse response);

    void importTemplate(MultipartFile file);

    List<PatrolLocationVO> queryLocationBatch(PatrolLocationPageParam param);

    /**
     * 获取巡更点ID列表
     *
     * @param param 查询参数
     *
     * @return 巡更点ID列表
     */
    List<PatrolLocationIdTypeDTO> getPatrolPointIdTypes(PatrolLocationPageParam param);

    /**
     * 根据区域id列表批量获取巡更点数统计
     *
     * @param gridAreaIds 区域id列表
     *
     * @return 区域id与巡更点数的映射，key为区域id，value为巡更点数
     */
    Map<Long, Long> countByAreaIds(List<Long> gridAreaIds);
}
