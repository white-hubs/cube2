package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomRentRecordCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomRentRecordParam;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomRentRecordVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomRentRecordQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomRentRecordQuery;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 房间租赁记录 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomRentRecordController {

    private final RoomRentRecordQueryService queryService;
    private final RoomRentRecordCommandService commandService;

    // 详情
    @GetMapping("/room-rent-record/{id}")
    public RoomRentRecordVO getById(@PathVariable("id") Long id) {
        return queryService.getVoById(id);
    }

    // 列表
    @GetMapping("/room-rent-record/list")
    public List<RoomRentRecordVO> list(RoomRentRecordQuery query) {
        return queryService.getVoList(query);
    }

    // 分页
    @GetMapping("/room-rent-record/page")
    public Pagination<RoomRentRecordVO> page(RoomRentRecordQuery query) {
        return queryService.getVoPage(query);
    }

    // 更新租赁信息Job
    @GetMapping("/room-rent-record/job")
    public void job(@RequestParam(value = "ids", defaultValue = "") String ids) {
        commandService.updateRoomRentInfo(ids);
    }

    // 新增
    @PostMapping("/room-rent-record")
    public void save(@Valid @RequestBody RoomRentRecordParam param) {
        commandService.save(param);
    }

    // 修改
    @PutMapping("/room-rent-record/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody RoomRentRecordParam param) {
        commandService.update(id, param);
    }

    // 删除
    @DeleteMapping("/room-rent-record/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 批量删除
    @DeleteMapping("/room-rent-record/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
