package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.NODE_PATH_SEPARATOR;
import static java.util.stream.Collectors.toMap;

/**
 * 一个对Product-Role-Menu-Terminal 关系实现有帮助的内存仓库
 */
@Slf4j
public class RmdpHelperSupportRepo {
    private final Map<Long, Role> rolesById;

    private final Map<Long, String> prodCodeById;

    private final Map<String, Long> prodIdByCode;

    private final Map<String, Long> appIdByCode;

    private final Map<Long, String> appCodeById;

    private final Map<Long, RoleKey> roleKeyById = new HashMap<>();
    private final Map<RoleKey, Long> roleIdByKey = new HashMap<>();

    private final Map<Long, TerminalKey> terminalKeyById = new HashMap<>();
    private final Map<TerminalKey, Long> terminalIdByKey = new HashMap<>();

    private final Map<Long, MenuKey> menuKeyById = new HashMap<>();
    private final Map<MenuKey, Long> menuIdByKey = new HashMap<>();

    private final Map<Long, String> menuCodePathById = new HashMap<>();  // 用于构建MenuKey的缓存

    private final Map<Long, Menu> menuById = new HashMap<>();

    public RmdpHelperSupportRepo(Collection<Product> products, Collection<Role> roles, Collection<Terminal> terminals
            , Collection<Application> applications, Collection<Menu> menus
                                ) {
        rolesById = roles.stream().collect(toMap(Role::getId, role -> role));
        prodCodeById = products.stream().collect(toMap(Product::getId, Product::getCode));
        prodIdByCode = products.stream().collect(toMap(Product::getCode, Product::getId));
        appIdByCode = applications.stream()
                .filter(x -> Objects.nonNull(x.getCode()))
                .collect(toMap(Application::getCode, Application::getId));
        appCodeById = appIdByCode.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        for (Role role : roles) {
            RoleKey roleKey = buildRoleKey(role);
            if (roleKey != null) {
                roleKeyById.put(role.getId(), roleKey);
                roleIdByKey.put(roleKey, role.getId());
            }
        }
        for (Terminal terminal : terminals) {
            TerminalKey terminalKey = buildTerminalKey(terminal);
            if (terminalKey != null) {
                terminalKeyById.put(terminal.getId(), terminalKey);
                terminalIdByKey.put(terminalKey, terminal.getId());
            }
        }
        for (Menu menu : menus) {
            menuById.put(menu.getId(), menu);
        }
        for (Menu menu : menus) {
            MenuKey menuKey = buildMenuKey(menu);
            if (menuKey != null) {
                menuKeyById.put(menu.getId(), menuKey);
                menuIdByKey.put(menuKey, menu.getId());
            }
        }
    }

    String generateMenuCodePath(Long menuId) {
        List<String> parentCodes = new ArrayList<>();
        Menu current = menuById.get(menuId);
        if (current == null) {
            // 作为一个marker, 标记这个menuId现在是无效的
            return NODE_PATH_SEPARATOR;
        }
        while (current != null) {
            parentCodes.add(0, current.getCode());
            current = current.getParentId() == null ? null : menuById.get(current.getParentId());
        }
        if (parentCodes.isEmpty()) {
            return "";
        } else if (parentCodes.size() == 1) {
            return parentCodes.get(0);
        } else {
            Collections.reverse(parentCodes);
            return String.join(NODE_PATH_SEPARATOR, parentCodes);
        }
    }

    MenuKey buildMenuKey(Menu menu) {
        String appCode = appCodeById.get(menu.getApplicationId());
        TerminalKey terminalKey = terminalKeyById.get(menu.getTerminalId());
        String parentMenuPath = menu.getParentId() == null ? "" :
                                menuCodePathById.computeIfAbsent(menu.getParentId(), this::generateMenuCodePath);

        if (terminalKey == null) {
            log.warn("invalid terminalId {}, menu: {}:{}", menu.getTerminalId(), appCode, menu.getCode());
            return null;
        }
        if (appCode == null) {
            log.warn("invalid app Id {}, menu: null:{}", menu.getApplicationId(), menu.getCode());
            return null;
        }
        if (parentMenuPath.equals(NODE_PATH_SEPARATOR)) {
            log.warn("invalid parent menuId {} menu: {}:{} ", menu.getParentId(), appCode, menu.getCode());
            return null;
        }

        return MenuKey.builder()
                .prodCode(terminalKey.prodCode)
                .terminalCode(terminalKey.terminalCode)
                .appCode(appCode)
                .parentCode(parentMenuPath)
                .menuCode(menu.getCode())
                .build();
    }

    RoleKey buildRoleKey(Role role) {
        String product = prodCodeById.get(role.getProductId());
        if (product == null) {
            log.warn("invalid product for role {}", role);
            return null;
        }
        return new RoleKey(product, role.getName());
    }

    TerminalKey buildTerminalKey(Terminal terminal) {
        String product = prodCodeById.get(terminal.getProductId());
        if (product == null) {
            log.warn("invalid product for terminal {}", terminal);
            return null;
        }
        return new TerminalKey(product, terminal.getCode());
    }

    public RoleKey getRoleKey(Long roleId) {
        RoleKey roleKey = roleKeyById.get(roleId);
        Objects.requireNonNull(roleKey, "role id invalid " + roleId);
        return roleKey;
    }

    public RoleKey getRoleKeyOrDefault(Long roleId, RoleKey defaultKey) {
        return roleKeyById.getOrDefault(roleId, defaultKey);
    }

    public TerminalKey getTerminalKeyOrDefault(Long terminalId, TerminalKey defaultKey) {
        return terminalKeyById.getOrDefault(terminalId, defaultKey);
    }

    public TerminalKey getTerminalKey(Long terminalId) {
        TerminalKey terminalKey = terminalKeyById.get(terminalId);
        Objects.requireNonNull(terminalKey, "terminal id invalid " + terminalId);
        return terminalKey;
    }

    public Role getRole(Long roleId) {
        Role role = rolesById.get(roleId);
        Objects.requireNonNull(role, "role id invalid " + roleId);
        return role;
    }

    public Role getRoleOrDefault(Long roleId, Role defaultVal) {
        return rolesById.getOrDefault(roleId, defaultVal);
    }

    public Long getTerminalIdByKey(TerminalKey terminalKey) {
        Long id = terminalIdByKey.get(terminalKey);
        Objects.requireNonNull(id, "terminal key invalid " + terminalKey);
        return id;
    }

    public Long getRoleIdByKey(RoleKey roleKey) {
        Long id = roleIdByKey.get(roleKey);
        Objects.requireNonNull(id, "role key invalid " + roleKey);
        return id;
    }

    public MenuKey getMenuKey(Long menuId) {
        MenuKey menuKey = menuKeyById.get(menuId);
        Objects.requireNonNull(menuKey, "menu id invalid " + menuId);
        return menuKey;
    }

    public Long getMenuIdByKey(MenuKey menuKey) {
        Long id = menuIdByKey.get(menuKey);
        Objects.requireNonNull(id, "menu key invalid " + menuKey);
        return id;
    }

    public Menu getMenuById(Long menuId) {
        Menu menu = menuById.get(menuId);
        Objects.requireNonNull(menu, "menu id invalid " + menuId);
        return menu;
    }

    public String getProductCode(Long productId) {
        String productCode = prodCodeById.get(productId);
        Objects.requireNonNull(productCode, "productId invalid " + productId);
        return productCode;
    }

    public Long getProductId(String productCode) {
        Long productId = prodIdByCode.get(productCode);
        Objects.requireNonNull(productId, "product code invalid " + productCode);
        return productId;
    }

    public String getAppCode(Long applicationId) {
        String appCode = appCodeById.get(applicationId);
        Objects.requireNonNull(appCode, "application id invalid " + applicationId);
        return appCode;
    }

    public String getAppCodeOrDefault(Long applicationId, String defaultVal) {
        return appCodeById.getOrDefault(applicationId, defaultVal);
    }

    public Long getAppIdByCode(String appCode) {
        Long appId = appIdByCode.get(appCode);
        Objects.requireNonNull(appId, "application code invalid " + appCode);
        return appId;
    }

    public Long getAppIdOrDefaultByCode(String appCode, Long defaultVal) {
        return appIdByCode.getOrDefault(appCode, defaultVal);
    }

    public RoleMenuKey getRoleMenuKey(Long roleId, Long menuId) {
        RoleKey roleKey = getRoleKey(roleId);
        MenuKey menuKey = getMenuKey(menuId);
        return RoleMenuKey.builder()
                .roleName(roleKey.roleName)
                .prodCode(menuKey.prodCode)
                .terminalCode(menuKey.terminalCode)
                .menuCode(menuKey.menuCode)
                .appCode(menuKey.appCode)
                .build();
    }

    @Data
    @Builder
    static
    public class MenuKey {
        private String prodCode;
        private String appCode;
        private String terminalCode;
        private String parentCode;
        private String menuCode;
    }

    @Data
    @AllArgsConstructor
    static
    public class RoleKey {
        private String prodCode;
        private String roleName;
    }

    @Data
    @AllArgsConstructor
    static
    public class TerminalKey {
        private String prodCode;
        private String terminalCode;
    }

    @Data
    @Builder
    public static class RoleMenuKey {
        String prodCode;
        String roleName;
        String parentCode;
        String menuCode;
        String appCode;
        String terminalCode;
    }
}
