package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseAppealCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseAppealParam;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseAppealQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseAppealQuery;
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
 * 企业诉求 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseAppealController {

    private final EnterpriseAppealQueryService queryService;
    private final EnterpriseAppealCommandService commandService;

    // 详情
    @GetMapping("/enterprise-appeal/{id}")
    public EnterpriseAppealVO getById(@PathVariable("id") Long id) {
        return queryService.getVoById(id);
    }

    // 列表
    @GetMapping("/enterprise-appeal/list")
    public List<EnterpriseAppealListVO> getList(EnterpriseAppealQuery query) {
        return queryService.getVOList(query);
    }

    // 分页
    @GetMapping("/enterprise-appeal/page")
    public Pagination<EnterpriseAppealListVO> getPage(EnterpriseAppealQuery query) {
        return queryService.getVOPage(query);
    }

    // 新增
    @PostMapping("/enterprise-appeal")
    public void save(@Valid @RequestBody EnterpriseAppealParam param) {
        commandService.save(param);
    }

    // 修改
    @PutMapping("/enterprise-appeal/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EnterpriseAppealParam param) {
        commandService.update(id, param);
    }

    // 删除
    @DeleteMapping("/enterprise-appeal/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 批量删除
    @DeleteMapping("/enterprise-appeal/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
