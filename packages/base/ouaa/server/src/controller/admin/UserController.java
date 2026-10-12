package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.query.SysUserQuery;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.vo.UserPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserSimpleVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理接口
 *
 * @author Woody
 * @date 2021/7/4
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public UserDTO saveByAdmin(@RequestBody @Valid UserModifyParam param) {
        return ObjectUtils.copying(this.userService.saveByAdmin(param), UserDTO.class);
    }

    @PutMapping("/user")
    public void updateByAdmin(@RequestBody @Valid UserUpdateParam param) {
        this.userService.updateByAdmin(param);
    }

    @PutMapping("/user/{id}/enable/{enable}")
    public void updateUserEnable(@PathVariable Long id, @PathVariable Boolean enable) {
        User user = this.userService.getById(id);
        Assert.isNotFound(user, "未找到要更新的用户");
        user.setEnabled(enable);
        this.userService.updateById(user);
    }

    @Deprecated
    @GetMapping("/user/page")
    public Pagination<UserPageVO> getByPage(@Valid UserQueryParam param) {
        Page<User> page = this.userService.getByPage(param);
        List<UserPageVO> users = ObjectUtils.copying(page.getRecords(), UserPageVO.class);
        return PagingUtils.convert(page, users);
    }

    /**
     * 获取系统用户分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/users")
    public Pagination<UserPageVO> getUsers(@Valid SysUserQuery query) {
        Page<User> page = this.userService.getSysUsers(query);
        List<UserPageVO> users = ObjectUtils.copying(page.getRecords(), UserPageVO.class);
        return PagingUtils.convert(page, users);
    }

    @GetMapping("/user/{id}")
    public UserVO getById(@PathVariable("id") Long id) {
        return ObjectUtils.copying(this.userService.getById(id), UserVO.class);
    }

    @PostMapping("/users/batch-get")
    public List<UserSimpleVO> getByIds(@Valid @RequestBody List<Long> ids) {
        var users = this.userService.listByIds(ids);
        return users.stream()
                .map(user -> new UserSimpleVO(user.getId(), user.getUsername(), user.getName(), user.getMobile(), user.getAdmin()))
                .toList();
    }
}
