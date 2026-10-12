package com.guhecloud.cube.srv.gen.securitymanagement.server.common.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.PasswordCheckParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseInfoService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseVo;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupControlCommonController {

    private final GroupBaseInfoService groupBaseInfoService;

    /**
     * @Description 群控组新增
     * @Author lrj
     */
    @PostMapping("/group-control")
    public void addGroupControl(@RequestBody GroupControlParam groupControlParam) {
        groupBaseInfoService.addGroupControl(groupControlParam);
    }

    /**
     * @Description 群控组编辑
     * @Author lrj
     */
    @PutMapping("/group-control/{id}")
    public void updateGroupControl(@PathVariable Long id, @RequestBody GroupControlParam groupControlParam) {
        groupBaseInfoService.updateGroupControl(id, groupControlParam);
    }

    /**
     * @Description 群控组移除
     * @Author lrj
     */
    @DeleteMapping("/group-control/{id}")
    public void deleteGroupControl(@PathVariable Long id) {
        groupBaseInfoService.deleteGroupControl(id);
    }

    /**
     * @Description 群控分页
     * @Author lrj
     */
    @GetMapping("/group-controls")
    public Pagination<GroupBaseInfoVo> getGroupControlsPage(GroupControlQueryParam param) {
        return groupBaseInfoService.getGroupControlsPage(param);
    }

    /**
     * @Description 群控详情
     * @Author lrj
     */
    @GetMapping("/group-control/{id}")
    public GroupBaseInfoVo getGroupControlsInfo(@PathVariable Long id) {
        return groupBaseInfoService.getGroupControlsInfo(id);
    }

    /**
     * @Description 群控列表
     * @Author lrj
     */
    @GetMapping("/object-type/group-controls")
    public List<GroupBaseVo> getGroupControlsList(GroupControlQueryParam param) {
        return groupBaseInfoService.getGroupControlsList(param);
    }

    /**
     * @Description 密码校验
     * @Author lrj
     */
    @PostMapping("/group-control/password/check")
    public Boolean groupControlPasswordCheck(@RequestBody PasswordCheckParam param) {
        return groupBaseInfoService.groupControlPasswordCheck(param);
    }

}
