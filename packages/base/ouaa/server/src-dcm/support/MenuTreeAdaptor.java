package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.support.ITreeNode;
import com.guhecloud.platform.dcm.support.TreeAdaptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.NODE_PATH_SEPARATOR;

@Slf4j
class MenuTreeAdaptor implements TreeAdaptor<MenuTreeAdaptor.MenuWrapper> {
    static final Long EMPTY_ROOT_ID = -1L;

    private final Map<Long, Menu> existedMenuById;
    private final Map<Long, List<Menu>> existedMenusByParentId = new HashMap<>();

    private Map<String, Long> existedMenuIdByCodePath;
    private final Map<Long, String> existedMenuCodePathById = new HashMap<>();

    /**
     * 下面两个字段用于直接添加的记录
     */
    private final Map<String, MenuWrapper> menuWrapperByCodePath = new HashMap<>();
    private final Map<String, List<MenuWrapper>> menuWrappersByParentCodePath = new HashMap<>();

    MenuTreeAdaptor(List<Menu> menus) {
        existedMenuById = menus.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));

        for (Menu menu : menus) {
            Long parentId = menu.getParentId() == null ? EMPTY_ROOT_ID : menu.getParentId();
            List<Menu> siblings = existedMenusByParentId.computeIfAbsent(parentId, k -> new ArrayList<>());
            siblings.add(menu);
        }
        existedMenuCodePathById.put(EMPTY_ROOT_ID, "");
        if (!existedMenusByParentId.containsKey(EMPTY_ROOT_ID) && !menus.isEmpty()) {
            log.debug("no root menu items. please check the dataset.");
        }
        Queue<Menu> inProcessing = new LinkedList<>(existedMenusByParentId.getOrDefault(EMPTY_ROOT_ID, Collections.emptyList()));
        while (!inProcessing.isEmpty()) {
            Menu currentNode = inProcessing.poll();
            String parentCodePath = currentNode.getParentId() == null ? "" : existedMenuCodePathById.get(currentNode.getParentId());
            existedMenuCodePathById.put(currentNode.getId(), buildCodePath(parentCodePath, currentNode.getCode()));
            if (existedMenusByParentId.get(currentNode.getId()) != null) {
                inProcessing.addAll(existedMenusByParentId.get(currentNode.getId()));
            }
        }
        existedMenuIdByCodePath = new LinkedHashMap<>();
        for (Entry<Long, String> entry : existedMenuCodePathById.entrySet()) {
            existedMenuIdByCodePath.merge(entry.getValue(), entry.getKey(), (oldValue, newValue) -> oldValue);
        }
    }

    public String getExistedMenuCodePath(Long menuId) {
        return existedMenuCodePathById.get(menuId == null ? EMPTY_ROOT_ID : menuId);
    }

    public void appendExistedMnu(Menu menu) {
        Objects.requireNonNull(menu.getId(),
                               String.format("Saving menu [appId:%d, code:%s, parent:%s] without Id",
                                             menu.getApplicationId(), menu.getCode(), menu.getParentId()));
        Menu oldValue = existedMenuById.putIfAbsent(menu.getId(), menu);
        if (oldValue != null) {
            log.warn("menu re-added {}. Please check whether anything wrong.", menu.getId());
            return;
        }
        Long parentId = menu.getParentId() == null ? EMPTY_ROOT_ID : menu.getParentId();
        Objects.requireNonNull(existedMenuCodePathById.get(parentId),
                               String.format("parent node must already existed for [appId:%d, code:%s, parent: %d] ",
                                             menu.getApplicationId(), menu.getCode(), menu.getParentId()));
        existedMenusByParentId.computeIfAbsent(parentId, k -> new ArrayList<>()).add(menu);
        String codePath = buildCodePath(existedMenuCodePathById.get(parentId), menu.getCode());
        existedMenuCodePathById.put(menu.getId(), codePath);
        existedMenuIdByCodePath.put(codePath, menu.getId());
    }

    public void appendDcmMenu(DcmMenu dcmMenu) {
        String key = buildCodePath(dcmMenu.getParentCodePath(), dcmMenu.getCode());

        MenuWrapper newWrapper = new MenuWrapper(dcmMenu);
        MenuWrapper prev = menuWrapperByCodePath.putIfAbsent(key, newWrapper);
        if (prev != null) {
            log.warn("menu item already existed with codePath: {}", key);
        } else {
            List<MenuWrapper> siblings = menuWrappersByParentCodePath.computeIfAbsent(dcmMenu.getParentCodePath(), k -> new ArrayList<>());
            siblings.add(newWrapper);
        }
    }

    public List<String> getDcmParentCodePaths() {
        List<String> parents = new ArrayList<>(menuWrappersByParentCodePath.keySet());
        parents.sort(Comparator.naturalOrder()); // parent一定在child的code path之前
        return parents;
    }

    public List<DcmMenu> getDcmMenusByParentCodePath(String parentCodePath) {
        return menuWrappersByParentCodePath.getOrDefault(parentCodePath, Collections.emptyList())
                .stream().map(x -> x.wrapped).collect(Collectors.toList());
    }

    private String buildCodePath(String parentCodePath, String code) {
        Objects.requireNonNull(code, "code path parts cannot be null");
        if (parentCodePath == null || parentCodePath.isEmpty()) {
            return code;
        } else {
            return parentCodePath + NODE_PATH_SEPARATOR + code;
        }
    }

    public MenuWrapper getNode(List<String> nodePaths) {
        List<String> nodeParts = StringUtils.isEmpty(nodePaths.get(0)) ? nodePaths.subList(1, nodePaths.size()) : nodePaths;
        String nodePath = nodeParts.stream().collect(Collectors.joining(NODE_PATH_SEPARATOR));
        return menuWrapperByCodePath.get(nodePath);
    }

    public Collection<? extends Identifiable> getChildren(MenuWrapper parent) {
        return parent.getChildren();
    }

    Long getExistedMenuId(String codePath) {
        return existedMenuIdByCodePath.get(codePath);
    }

    class MenuWrapper implements ITreeNode<MenuWrapper> {
        private final DcmMenu wrapped;

        MenuWrapper(DcmMenu wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getCode();
        }

        @Override
        public String getFingerPrint() {
            return wrapped.getFingerPrint();
        }

        @Override
        public String getNodeContentFp() {
            return wrapped.getFingerPrint();
        }

        @Override
        public List<Identifiable> getChildren() {
            // get children from the context
            return menuWrappersByParentCodePath.getOrDefault(wrapped.getParentCodePath(), Collections.emptyList())
                    .stream()
                    .map(x -> new MiniMenuNode(x.getId()))
                    .collect(Collectors.toList());
        }
    }

    @Data
    @AllArgsConstructor
    static class MiniMenuNode implements Identifiable {
        String id;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getFingerPrint() {
            return "";
        }
    }
}
