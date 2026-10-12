package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * <p>
 * 巡检类型 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTypeController {

    private final InspectTypeMapper mapper;
    private final InspectTypeCommandService typeCommandService;
    private final InspectTypeQueryService typeQueryService;

    /**
     * 详情
     */
    @GetMapping("/inspect-type/{id}")
    public InspectTypeVO getById(@PathVariable("id") @NotNull Long id) {
        InspectTypePO po = mapper.selectById(id);
        return ObjectUtils.copying(po, InspectTypeVO.class);
    }

    /**
     * 分页
     */
    @GetMapping("/inspect-type/page")
    public Pagination<InspectTypeVO> getPage(InspectTypeQueryParam queryParam) {
        return typeQueryService.getPage(queryParam);
    }

    /**
     * 列表
     */
    @GetMapping("/inspect-type/list")
    public List<InspectTypeVO> getList(InspectTypeQueryParam queryParam) {
        return typeQueryService.getList(queryParam);
    }

    /**
     * 新增
     */
    @PostMapping("/inspect-type")
    public void save(@RequestBody @Valid InspectTypeCommandModel command) {
        typeCommandService.save(command);
    }

    /**
     * 修改
     */
    @PutMapping("/inspect-type/{id}")
    public void update(@PathVariable("id") @NotNull Long id, @RequestBody @Valid InspectTypeCommandModel command) {
        typeCommandService.update(id, command);
    }

    /**
     * 修改
     */
    @DeleteMapping("/inspect-type/{id}")
    public void remove(@PathVariable("id") @NotNull Long id) {
        typeCommandService.remove(id);
    }
}

