package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final TerminalService terminalService;

    private final RoleTerminalService roleTerminalService;

    public UserDetailsServiceImpl(UserService userService, UserRoleService userRoleService, TerminalService terminalService,
                                  RoleTerminalService roleTerminalService
                                 ) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.terminalService = terminalService;
        this.roleTerminalService = roleTerminalService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.lookupUser(username);
        if (Objects.isNull(user)) {
            return null;
        }
        this.doLoginLog(username);
        List<GrantedAuthority> authorities = this.getUserAuthorities(user);
        // activiti7要求用户必须具有ROLE_ACTIVITI_USER角色才能调用其api
        authorities.add(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
    }

    private void doLoginLog(String username) {
        User user = this.userService.lookupUser(username);
        user.setLastLoginTime(DateUtils.now());
        user.setLastLoginAddress(SessionContextHolder.getRequester().getHost());
        // @formatter:off
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.<User>lambdaUpdate().eq(User::getId, user.getId())
                .set(User::getLastLoginTime, DateUtils.now())
                .set(User::getLastLoginAddress, SessionContextHolder.getRequester().getHost());
        // @formatter:on
        this.userService.update(updateWrapper);
    }

    private List<GrantedAuthority> getUserAuthorities(User user) {
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 能访问当前访问端的角色
        List<Long> terminalRoleIds = Objects.isNull(terminal) ? Collections.emptyList()
                                                              : this.roleTerminalService.listByTerminal(terminal.getId()).stream()
                                             .map(RoleTerminal::getRoleId).toList();
        // 用户在平台下的角色
        List<Long> userRoleIds = this.userRoleService.listUserRoles(Collections.singleton(user.getId())).stream().map(UserRoleDTO::getId)
                .toList();
        // 交集
        List<Long> roleIds = userRoleIds.stream().filter(terminalRoleIds::contains).toList();
        // 转换为权限对象
        return roleIds.stream().filter(Objects::nonNull).distinct().map(Object::toString).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
