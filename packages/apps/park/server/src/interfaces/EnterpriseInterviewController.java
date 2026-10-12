package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseInterviewCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseInterviewParam;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseInterviewQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseInterviewQuery;
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
 * 企业走访 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseInterviewController {

    private final EnterpriseInterviewQueryService queryService;
    private final EnterpriseInterviewCommandService commandService;

    // 详情
    @GetMapping("/enterprise-interview/{id}")
    public EnterpriseInterviewVO getById(@PathVariable("id") Long id) {
        return queryService.getVoById(id);
    }

    // 列表
    @GetMapping("/enterprise-interview/list")
    public List<EnterpriseInterviewListVO> getList(EnterpriseInterviewQuery query) {
        return queryService.getVOList(query);
    }

    // 分页
    @GetMapping("/enterprise-interview/page")
    public Pagination<EnterpriseInterviewListVO> getPage(EnterpriseInterviewQuery query) {
        return queryService.getVOPage(query);
    }

    // 新增
    @PostMapping("/enterprise-interview")
    public void save(@Valid @RequestBody EnterpriseInterviewParam param) {
        commandService.save(param);
    }

    // 修改
    @PutMapping("/enterprise-interview/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EnterpriseInterviewParam param) {
        commandService.update(id, param);
    }

    // 删除
    @DeleteMapping("/enterprise-interview/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    // 批量删除
    @DeleteMapping("/enterprise-interview/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
