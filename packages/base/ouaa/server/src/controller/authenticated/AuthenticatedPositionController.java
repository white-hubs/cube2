package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PositionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.vo.PositionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 岗位管理接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class AuthenticatedPositionController {
    private final PositionService positionService;

    /**
     * 修改岗位
     *
     * @param id    岗位主键
     * @param param 修改参数
     */
    @PutMapping("/authenticated/position/{id}")
    public void updatePosition(@PathVariable("id") @Min(value = 1, message = "岗位主键不能小于1") Long id,
                               @RequestBody @Valid PositionModifyParam param
                              ) {
        this.positionService.updatePosition(id, param);
    }

    @GetMapping("/authenticated/position/ids")
    public List<PositionDTO> getPositions(@RequestParam List<Long> ids) {
        List<Position> list = this.positionService.lambdaQuery().in(Position::getId, ids).list();
        return ObjectUtils.copying(list, PositionDTO.class);
    }

    /**
     * 查询岗位
     *
     * @param param 查询参数
     *
     * @return 岗位列表分页
     */
    @GetMapping("/authenticated/positions")
    public Pagination<PositionVO> listPosistions(@Valid PosistionQueryParam param) {
        Page<Position> page = this.positionService.listPosistions(param);
        return PagingUtils.convert(page, PositionVO.class);
    }

    /**
     * 岗位列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/authenticated/position/list")
    public List<PositionVO> list(@Valid PosistionQueryParam param) {
        List<Position> positions = this.positionService.list(param);
        return ObjectUtils.copying(positions, PositionVO.class);
    }

    /**
     * 根据名称查询岗位
     *
     * @param name
     *
     * @return
     */
    @GetMapping("/authenticated/position/name")
    public List<PositionVO> getByName(@RequestParam(value = "name") String name) {
        List<Position> positions = this.positionService.lambdaQuery().eq(Position::getName, name).list();
        return ObjectUtils.copying(positions, PositionVO.class);
    }

}
