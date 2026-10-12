package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRoutePageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRouteParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolRouteService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 巡更路线配置
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolRouteController {
    private final PatrolRouteService patrolRouteService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @GetMapping("/patrol/route/{id}")
    public PatrolRouteVO queryById(@PathVariable("id") Long id) {
        return patrolRouteService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @GetMapping("/patrol/route/page")
    public Pagination<PatrolRouteVO> queryRoutePage(PatrolRoutePageParam param) {
        return patrolRouteService.queryRoutePage(param);
    }

    /**
     * 查询所有巡更路线
     *
     * @return 巡更路线
     */
    @GetMapping("/patrol/route/all")
    public List<PatrolRouteVO> queryRouteAll() {
        return patrolRouteService.queryRouteAll();
    }

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    @PostMapping("/patrol/route/insert")
    public Boolean insert(@RequestBody PatrolRouteParam param) {
        return patrolRouteService.insert(param);
    }

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    @PutMapping("/patrol/route/update")
    public Boolean update(@RequestBody PatrolRouteParam param) {
        return patrolRouteService.update(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/patrol/route/{id}")
    public Boolean deleteById(@PathVariable("id") Long id) {
        return patrolRouteService.deleteById(id);
    }
}
