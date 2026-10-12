package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnumVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomEnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomQuery;
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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 房间 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomController {

    private final RoomQueryService queryService;
    private final RoomCommandService commandService;

    // 房间状态
    @GetMapping("/room/status")
    public List<EnumVo> getStatus() {
        List<EnumVo> enumVos = new ArrayList<>();
        for (RoomStatusEnum value : RoomStatusEnum.values()) {
            EnumVo enumVo = new EnumVo();
            enumVo.setCode(value.toString());
            String description = value.getDescription();
            enumVo.setName(description);
            enumVos.add(enumVo);
        }
        return enumVos;
    }

    // 详情
    @GetMapping("/room/{id}")
    public RoomVO getById(@PathVariable("id") Long id) {
        return queryService.getVoById(id);
    }

    // 房间企业详情
    @GetMapping("/room-enterprise")
    public RoomEnterpriseVO getRoomEnterpriseVO(@RequestParam("id") Long id, @RequestParam("name") String name) {
        return queryService.getRoomEnterpriseVO(id, name);
    }

    // 列表查询
    @GetMapping("/room/list")
    public List<RoomVO> list(RoomQuery query) {
        return queryService.getVOList(query);
    }

    // 租户房间列表查询
    @GetMapping("/tenantRoom/list")
    public List<RoomVO> tenantRoom() {
        return queryService.tenantRoom();
    }

    // 分页
    @GetMapping("/room/page")
    public Pagination<RoomVO> page(RoomQuery query) {
        return queryService.getVOPage(query);
    }

    // 企业新增
    @PostMapping("/room")
    public void save(@Valid @RequestBody RoomParam param) {
        commandService.save(param);
    }

    // 企业修改
    @PutMapping("/room/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody RoomParam param) {
        commandService.update(id, param);
    }

    // 企业删除
    @DeleteMapping("/room/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 企业批量删除
    @DeleteMapping("/room/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
