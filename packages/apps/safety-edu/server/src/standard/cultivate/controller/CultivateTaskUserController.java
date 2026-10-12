package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskUserVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

import java.util.List;

/**
 * @author Appet
 * @Description 培训人员
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskUserController {
    private final CultivateTaskUserService cultivateTaskUserService;

    /**
     * 功能描述： 培训人员添加
     */
    @PostMapping(value = "/cultivate-task-user")
    public Long add(@Valid @RequestBody CultivateTaskUserParam param) {
        return cultivateTaskUserService.add(param);
    }

    /**
     * 功能描述： 培训人员修改
     */
    @PutMapping(value = "/cultivate-task-user/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody CultivateTaskUserParam param) {
        cultivateTaskUserService.update(id, param);
    }

    /**
     * 功能描述： 培训人员删除
     */
    @DeleteMapping(value = "/cultivate-task-user/{id}")
    public void delete(@PathVariable Long id) {
        cultivateTaskUserService.delete(id);
    }

    /**
     * 功能描述： 培训人员批量删除
     */
    @DeleteMapping(value = "/cultivate-task-user/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        cultivateTaskUserService.batchDelete(ids);
    }

    /**
     * 功能描述： 培训人员分页
     */
    @GetMapping(value = "/cultivate-task-user/page")
    public Pagination<CultivateTaskUserVO> getPage(CultivateTaskUserQueryParam param) {
        return cultivateTaskUserService.getPage(param);
    }

    @PostMapping(value = "/cultivate-task-user/export")
    public void exportCultivateUser(@RequestBody CultivateTaskUserQueryParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response
                                   ) {
        cultivateTaskUserService.exportCultivateUser(param, request, response);
    }

    /**
     * 功能描述： 培训人员详情
     */
    @GetMapping(value = "/cultivate-task-user/{id}")
    public CultivateTaskUserVO getById(@PathVariable Long id) {
        return cultivateTaskUserService.getById(id);
    }

}