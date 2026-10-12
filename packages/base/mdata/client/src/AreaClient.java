package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.client.vo.AreaListVO;
import com.guhecloud.cube.biz.mdata.client.vo.AreaStatisticsVo;
import com.guhecloud.cube.biz.mdata.client.vo.EnergyAreaVo;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lzy
 * @date 2021/10/19
 */
@FeignClient(name = "mdata")
public interface AreaClient {

    @GetMapping("/area/child/ids")
    Set<Long> getChildrenIds(@RequestParam(value = "id") Long id);

    @GetMapping("/area/ids")
    List<AreaDTO> getByIds(@RequestParam("ids") Collection<Long> ids);

    @GetMapping("/areas-inner")
    List<AreaDTO> listAreas();

    @GetMapping("/area/{id}/parents")
    List<Long> getParentIds(@PathVariable("id") Long id);

    @GetMapping("/area/{id}")
    AreaDTO getAreaById(@PathVariable("id") Long id);

    @GetMapping("/hiddendanger-area/{id}")
    AreaDTO getHiddenDangerAreaById(@PathVariable("id") Long id);

    /**
     * 获取所有的一级区域
     *
     * @return
     */
    @GetMapping("/area/{level}/level")
    List<AreaDTO> getAreaByLevelOne(@PathVariable("level") Integer level);

    /**
     * 区域列表 内部使用
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/area-inner/list")
    List<AreaListDTO> listInnerArea(@SpringQueryMap AreaQueryParam queryParam);

    /**
     * 获取区域名称
     *
     * @return
     */
    @GetMapping("getAreaNameById")
    String getAreaNameById(@RequestParam("id") Long id, @RequestParam("tenant") Integer tenant);

    //安全管理区域树
    @GetMapping("/area/security/list")
    List<AreaListVO> securityAreaList(@SpringQueryMap AreaQueryParam queryParam);

    /**
     * @Description 内部区域树
     */
    @GetMapping("/area/inner/list")
    List<EnergyAreaVo> innerList();

    /**
     * @Description 区域拼接
     * @Author lrj
     */
    @GetMapping("/getAreaNamesById")
    String getAreaNamesById(@RequestParam("id") Long id);

    @GetMapping("/area/list")
    List<AreaListVO> list(@Valid AreaQueryParam queryParam);

    @GetMapping("/area-statistics")
    List<AreaStatisticsVo> getAreaStatistics(@RequestParam("id") Long id);

    @GetMapping("/area/by-name")
    List<AreaDTO> getAreaByName(@RequestParam("areaName") String areaName);

    /**
     * 根据区域树序列号查询区域。
     *
     * @param sequence 区域树序列号
     *
     * @return 区域信息；不存在时返回空。
     */
    @GetMapping("/area/by-sequence")
    AreaDTO getAreaBySequence(@RequestParam("sequence") String sequence);

    /**
     * @Description 区域过滤
     * @Author lrj
     */
    @GetMapping("/area/level/filter")
    List<AreaDTO> getFilterAreaByLevelOne(@RequestParam("level") Integer level);

    /**
     * @description: 查询所有区域，名称使用全称如：广东省-广州市-天河区
     * @author: lijie
     * @date: 2024/8/16 10:04
     * @param:
     * @return: java.util.List<com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO>
     **/
    @GetMapping("/getAllAreaFullName")
    List<AreaDTO> getAllAreaFullName();

    /**
     * 获取层级+子对象的名称
     *
     * @param level 层级
     *
     * @return map
     */
    @GetMapping("/motherland-district/level-map")
    Map<String, List<String>> getMotherlandDistrictLevelMap(@RequestParam("level") Integer level);

    /**
     * 获取区域列表
     */
    @PostMapping("/area/tree")
    List<AreaDTO> getAreas(@RequestBody AreaQueryParam queryParam);

    /**
     * 获取区域ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 区域ID列表
     */
    @PostMapping("/area/idTypes")
    List<AreaIdTypeDTO> getAreaIdTypes(@RequestBody AreaQueryParam queryParam);

    /**
     * 获取区域列表
     */
    @PostMapping("/area-inner/list/post")
    List<AreaDTO> listInnerAreaPost(@RequestBody AreaQueryParam queryParam);
}
