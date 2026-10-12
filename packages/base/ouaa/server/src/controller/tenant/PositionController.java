package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PositionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.vo.PositionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class PositionController {
    private final PositionService positionService;
    private final EnterpriseExportService enterpriseExportService;

    /**
     * 新增岗位
     *
     * @param param 新增参数
     */
    @PostMapping("/position")
    public void addPosition(@RequestBody @Valid PositionModifyParam param) {
        this.positionService.addPosition(param);
    }

    /**
     * 修改岗位
     *
     * @param id    岗位主键
     * @param param 修改参数
     */
    @PutMapping("/position/{id}")
    public void updatePosition(@PathVariable("id") @Min(value = 1, message = "职位主键不能小于1") Long id,
                               @RequestBody @Valid PositionModifyParam param
                              ) {
        this.positionService.updatePosition(id, param);
    }

    /**
     * 删除岗位
     *
     * @param id 岗位主键
     */
    @DeleteMapping("/position/{id}")
    public void deletePosition(@PathVariable("id") Long id) {
        this.positionService.deletePosition(id);
    }

    /**
     * 查询岗位
     *
     * @param param 查询参数
     *
     * @return 岗位列表分页
     */
    @GetMapping("/positions")
    public Pagination<PositionVO> listPosistions(@Valid PosistionQueryParam param) {
        Page<Position> page = this.positionService.listPosistions(param);
        return PagingUtils.convert(page, PositionVO.class);
    }

    /**
     * 通过ID 查询岗位
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/position/{id}")
    public PositionDTO getPosition(@PathVariable("id") Long id) {
        Position post = this.positionService.getById(id);
        return ObjectUtils.copying(post, PositionDTO.class);
    }

    @GetMapping("/position/ids")
    public List<PositionDTO> getPositions(@RequestParam List<Long> ids) {
        List<Position> list = this.positionService.lambdaQuery().in(Position::getId, ids).list();
        return ObjectUtils.copying(list, PositionDTO.class);
    }

    /**
     * 岗位导出
     *
     * @param param 筛选条件
     */
    @GetMapping("/position/export")
    public void export(@SpringQueryMap @Valid AppointmentQueryParam param) {
        enterpriseExportService.exportAppointment(param);
    }

}
