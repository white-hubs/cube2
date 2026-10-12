package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway.AttGroupCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupPageResult;
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
 * 考勤组入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttGroupController {
    private final AttGroupCommandService attGroupCommandService;
    private final AttGroupQueryService attGroupQueryService;

    /**
     * 新增考勤组
     *
     * @param param 考勤组对象
     *
     * @return id
     */
    @PostMapping("/att/group/add")
    public String addAttGroup(@RequestBody @Valid AttGroupParam param) {
        return attGroupCommandService.addAttGroup(param);
    }

    /**
     * 编辑考勤组
     *
     * @param param 考勤组对象
     *
     * @return id
     */
    @PostMapping("/att/group/edit")
    public String editAttGroup(@RequestBody @Valid AttGroupParam param) {
        return attGroupCommandService.editAttGroup(param);
    }

    /**
     * 删除考勤组
     *
     * @param id 主键
     *
     * @return id
     */
    @DeleteMapping("/att/group/remove")
    public Long removeAttGroup(Long id) {
        return attGroupCommandService.removeAttGroup(id);
    }

    /**
     * 考勤组分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/att/group/page")
    public Pagination<AttGroupPageResult> pageAttGroup(AttGroupPageParam param) {
        return attGroupQueryService.pageAttGroup(param);
    }

    /**
     * 考勤组详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/att/group/detail")
    public AttGroupDetailResult infoAttGroup(Long id) {

        return attGroupQueryService.infoAttGroup(id);
    }

    /**
     * 考勤组列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping("/att/group/list")
    public List<AttGroupListResult> listAttGroup(AttGroupPageParam param) {
        return attGroupQueryService.listAttGroup(param);
    }

    /**
     * 校验考勤组人员照片
     *
     * @param param 入参
     *
     * @return 提示信息
     */
    @PostMapping("/att/group/check/staff")
    public String checkGroupStaff(@RequestBody AttGroupParam param) {
        return attGroupQueryService.checkGroupStaffPhoto(param);
    }
}
