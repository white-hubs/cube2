package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway.AttClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesPageResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考勤班次入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassesController {

    private final AttClassesCommandService attClassesCommandService;
    private final AttClassesQueryService attClassesQueryService;

    /**
     * 新增班次
     *
     * @param param 班次对象入参
     *
     * @return 班次id
     */
    @PostMapping("/att/classes/add")
    public Long addAttClasses(@RequestBody @Valid AttClassesParam param) {
        return attClassesCommandService.addAttClasses(param);
    }

    /**
     * 修改班次
     *
     * @param param 编辑后的对象
     */
    @PostMapping("/att/classes/edit")
    public void editAttClasses(@RequestBody @Valid AttClassesParam param) {
        attClassesCommandService.editAttClasses(param);
    }

    /**
     * 删除班次
     *
     * @param id 主键
     */
    @DeleteMapping("/att/classes/remove")
    public void removeAttClasses(Long id) {
        attClassesCommandService.removeAttClass(id);
    }

    /**
     * 分页查询
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @GetMapping("/att/classes/page")
    public Pagination<AttClassesPageResult> pageAttClasses(AttClassesPageParam param) {

        return attClassesQueryService.pageAttClasses(param);
    }

    /**
     * 班次详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/att/classes/detail")
    public AttClassesDetailResult infoAttClasses(Long id) {

        return attClassesQueryService.infoAttClasses(id);
    }

    /**
     * 班次列表
     *
     * @param param 条件
     *
     * @return 班次列表
     */
    @GetMapping("/att/classes/list")
    public List<AttClassesListResult> listAttClasses(AttClassesPageParam param) {

        return attClassesQueryService.listAttClasses(param);
    }

    /**
     * 预校验班次有哪些考勤组使用
     *
     * @param id 班次id
     *
     * @return 使用的考勤组提示
     */
    @GetMapping("/att/classes/checkGroupUse")
    public String checkClassUseGroupStr(Long id) {
        return attClassesQueryService.checkClassUseGroupStr(id);
    }
}
