package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseParam;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseScaleEnum;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseTypeEnum;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseDirectoryVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnumVo;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseQuery;
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
 * 企业 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseController {

    private final EnterpriseQueryService queryService;
    private final EnterpriseCommandService commandService;

    // 企业类型
    @GetMapping("/enterprise/types")
    public List<EnumVo> getTypes() {
        List<EnumVo> enumVos = new ArrayList<>();
        for (EnterpriseTypeEnum value : EnterpriseTypeEnum.values()) {
            EnumVo enumVo = new EnumVo();
            enumVo.setCode(value.toString());
            String description = value.getDescription();
            enumVo.setName(description);
            enumVos.add(enumVo);
        }
        return enumVos;
    }

    // 企业规模
    @GetMapping("/enterprise/scales")
    public List<EnumVo> getScales() {
        List<EnumVo> enumVos = new ArrayList<>();
        for (EnterpriseScaleEnum value : EnterpriseScaleEnum.values()) {
            EnumVo enumVo = new EnumVo();
            enumVo.setCode(value.toString());
            String description = value.getDescription();
            enumVo.setName(description);
            enumVos.add(enumVo);
        }
        return enumVos;
    }

    // 详情
    @GetMapping("/enterprise/{id}")
    public EnterpriseVO getById(@PathVariable Long id) {
        return queryService.getVoById(id);
    }

    // 列表查询
    @GetMapping("/enterprise/list")
    public List<EnterpriseVO> list(EnterpriseQuery query) {
        return queryService.getVOList(query);
    }

    // 分页
    @GetMapping("/enterprise/page")
    public Pagination<EnterpriseVO> page(EnterpriseQuery query) {
        return queryService.getVOPage(query);
    }

    // 企业名录分页
    @GetMapping("/enterprise/directory/page")
    public Pagination<EnterpriseDirectoryVO> pageByDirectory(EnterpriseQuery query) {
        return queryService.getVOPageByDirectory(query);
    }

    // 企业新增
    @PostMapping("/enterprise")
    public void save(@Valid @RequestBody EnterpriseParam param) {
        commandService.save(param);
    }

    // 企业修改
    @PutMapping("/enterprise/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EnterpriseParam param) {
        commandService.update(id, param);
    }

    // 企业删除
    @DeleteMapping("/enterprise/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 企业批量删除
    @DeleteMapping("/enterprise/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
