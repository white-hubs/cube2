package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomScadaResourcesCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomScadaResourcesParam;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.AreaListVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomScadaResourcesVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomScadaResourcesQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomScadaResourcesQuery;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 房间组态资源 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomScadaResourcesController {

    private final RoomScadaResourcesQueryService queryService;
    private final RoomScadaResourcesCommandService commandService;

    // 详情
    @GetMapping("/room-scada-resources/{id}")
    public RoomScadaResourcesVO getById(@PathVariable("id") Long id) {
        return queryService.getVoById(id);
    }

    //  列表查询
    @GetMapping("/room-scada-resources/list")
    public List<RoomScadaResourcesVO> list(RoomScadaResourcesQuery query) {
        return queryService.getVOList(query);
    }

    //  区域树
    @GetMapping("/room-scada-resources/tree")
    public List<AreaListVo> tree(RoomScadaResourcesQuery query) {
        return queryService.getVOTree(query);
    }

    // 新增
    @PostMapping("/room-scada-resources")
    public void save(@Valid @RequestBody RoomScadaResourcesParam param) {
        commandService.save(param);
    }

    // 修改
    @PutMapping("/room-scada-resources/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody RoomScadaResourcesParam param) {
        commandService.update(id, param);
    }

    // 删除
    @DeleteMapping("/room-scada-resources/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 批量删除
    @DeleteMapping("/room-scada-resources/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
