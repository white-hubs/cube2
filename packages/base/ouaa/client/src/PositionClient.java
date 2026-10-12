package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.param.PosistionQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户接口
 *
 * @author appet
 * @date 2021/7/9
 */
@FeignClient(name = "ouaa")
public interface PositionClient {
    /**
     * 通过ID 查询岗位信息
     *
     * @param id 岗位主键
     *
     * @return
     */
    @GetMapping("/authenticated/position/{id}")
    PositionDTO getPosition(@PathVariable("id") Long id);

    /**
     * 通过IDS 查询岗位信息
     *
     * @param ids 岗位主键
     *
     * @return
     */
    @GetMapping("/authenticated/position/ids")
    List<PositionDTO> getPositions(@RequestParam(value = "ids") List<Long> ids);

    /**
     * 根据名称查询岗位
     *
     * @param name
     *
     * @return
     */
    @GetMapping("/authenticated/position/name")
    List<PositionDTO> getByName(@RequestParam(value = "name") String name);

    /**
     * 岗位列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/authenticated/position/list")
    List<PositionDTO> list(@SpringQueryMap PosistionQueryParam param);
}
