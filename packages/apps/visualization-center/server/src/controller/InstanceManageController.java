package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.param.InstanceManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.InstanceManageParam;
import com.guhecloud.cube.base.visualization.server.service.InstanceManageService;
import com.guhecloud.cube.base.visualization.server.vo.InstanceManageVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布实例控制层
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/instance")
public class InstanceManageController {
    private final InstanceManageService instanceManageService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    @GetMapping("/{id}")
    public InstanceManageVO queryById(@PathVariable Long id) {
        return instanceManageService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/queryInstancePage")
    public Pagination<InstanceManageVO> queryInstancePage(InstanceManagePageParam param) {
        return instanceManageService.queryInstancePage(param);
    }

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/insert")
    public Boolean insert(@RequestBody InstanceManageParam param) {
        return instanceManageService.insert(param);
    }

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody InstanceManageParam param) {
        return instanceManageService.update(param);
    }
}
