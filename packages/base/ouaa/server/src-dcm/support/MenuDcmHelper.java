package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import com.guhecloud.platform.dcm.support.ITreeDcmHelper;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.MenuTreeAdaptor.EMPTY_ROOT_ID;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_MENU;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PARENT;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PROD_CODE;
import static com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.TerminalKey;

public class MenuDcmHelper extends AbstractGroupedDcmHelper<Menu, DcmMenu> implements ITreeDcmHelper<Menu, DcmMenu> {

    private final Map<MenuGroupKey, MenuTreeAdaptor> existedMenuTrees;

    private final RmdpHelperSupportRepo supportRepo;

    public MenuDcmHelper(List<Product> products, List<Terminal> terminals, List<Application> applications,
                         List<Menu> menus
                        ) {
        super(Menu.class, DcmMenu.class);
        supportRepo = new RmdpHelperSupportRepo(products, Collections.emptyList(), terminals, applications, menus);

        existedMenuTrees = menus.stream()
                .collect(Collectors.groupingBy(x -> buildMenuGroupKey(x.getTerminalId(), x.getApplicationId())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> new MenuTreeAdaptor(x.getValue())))
        ;
    }

    public static IDcmRciConverter<DcmMenu> createDcmRciConverter() {
        return new MenuDcmHelper(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                 Collections.emptyList());
    }

    /**
     * Menu 是一个树形结构，同时全局的Menu 依 AppId/TerminalId 分组
     */
    public boolean isTreeStyle() {
        return true;
    }

    @Override
    public String getBizKey(Menu bizEntity) {
        TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        String parentCodePath = existedMenuTrees.get(buildMenuGroupKey(bizEntity.getTerminalId(), bizEntity.getApplicationId()))
                .getExistedMenuCodePath(bizEntity.getParentId());
        return String.format("%s:%s:%s:%s:%s", terminalKey.getProdCode(), terminalKey.getTerminalCode(),
                             appCode, parentCodePath, bizEntity.getCode());
    }

    @Override
    public void updateBizWithDcm(Menu input, DcmMenu dcmEntity) {
        input.setName(dcmEntity.getName());
        input.setPath(dcmEntity.getPath());
        input.setIcon(dcmEntity.getIcon());
        // 暂时DCM 不更新已有项目的启用、禁用特性。
        // input.setEnabled(dcmEntity.getEnabled());
        if (dcmEntity.getSort() != null) {
            input.setSort(dcmEntity.getSort());
        }
        if (dcmEntity.getRemark() != null) {
            input.setRemark(dcmEntity.getRemark());
        }
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_MENU;
    }

    @Override
    protected Object generateGroupKey(DcmMenu dcmEntity) {
        return buildDcmMenuGroupKey(dcmEntity);
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmMenu> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setVargs(new ArrayList<>(dcmEntities));
        DcmMenuGroupKey key = (DcmMenuGroupKey) groupKey;
        rci.setKwargs(KW_KEY_PROD_CODE, key.getProdCode());
        rci.setKwargs(OuaaDcmSupport.KW_KEY_TERMINAL_CODE, key.getTerminal());
        rci.setKwargs(OuaaDcmSupport.KW_KEY_APP_CODE, key.getApp());
        if (key.getParentCodePath() != null && !key.getParentCodePath().isEmpty()) {
            rci.setKwargs(KW_KEY_PARENT, key.getParentCodePath());
        }
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmMenu> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PROD_CODE, null);
        Objects.requireNonNull(prodCode, "product code invalid");
        String terminalCode = rci.getKwargs(OuaaDcmSupport.KW_KEY_TERMINAL_CODE, null);
        Objects.requireNonNull(terminalCode, "terminal code invalid");
        String appCode = rci.getKwargs(OuaaDcmSupport.KW_KEY_APP_CODE, null);
        Objects.requireNonNull(appCode, "app code invalid");
        String parentCodePath = rci.getKwargs(KW_KEY_PARENT, "");
        for (DcmMenu dcmMenu : dcmEntities) {
            dcmMenu.setProdCode(prodCode);
            dcmMenu.setAppCode(appCode);
            dcmMenu.setTerminalCode(terminalCode);
            dcmMenu.setParentCodePath(parentCodePath);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(Menu bizEntity, DcmMenu dcmEntity) {
        Long terminalId = supportRepo.getTerminalIdByKey(new TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode()));
        Long appId = supportRepo.getAppIdByCode(dcmEntity.getAppCode());
        MenuTreeAdaptor mta = existedMenuTrees.get(buildMenuGroupKey(terminalId, appId));
        Long parentId = mta.getExistedMenuId(dcmEntity.getParentCodePath());
        Objects.requireNonNull(parentId,
                               String.format("parent Code Path not valid for: %s:%s:%s",
                                             dcmEntity.getAppCode(), dcmEntity.getParentCodePath(), dcmEntity.getCode()));
        if (parentId.equals(EMPTY_ROOT_ID)) {
            parentId = null;
        }
        bizEntity.setParentId(parentId);
        bizEntity.setTerminalId(terminalId);
        bizEntity.setApplicationId(appId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmMenu dcmEntity, Menu bizEntity) {
        TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());

        MenuTreeAdaptor mta = existedMenuTrees.get(buildMenuGroupKey(bizEntity));
        String parentCode = mta.getExistedMenuCodePath(bizEntity.getParentId());
        Objects.requireNonNull(parentCode, "parent id invalid " + bizEntity.getParentId());
        dcmEntity.setAppCode(appCode);
        dcmEntity.setProdCode(terminalKey.getProdCode());
        dcmEntity.setTerminalCode(terminalKey.getTerminalCode());
        dcmEntity.setParentCodePath(parentCode);
        dcmEntity.setRemark(bizEntity.getRemark() == null ? "" : bizEntity.getRemark());
        dcmEntity.setIcon(bizEntity.getIcon() == null ? "" : bizEntity.getIcon());
    }

    @Override
    public void treeSaveBatch(IService<Menu> service, Collection<DcmMenu> entities) {
        if (entities.isEmpty()) {
            return;
        }
        Map<MenuGroupKey, List<DcmMenu>> addTreeByGroup = entities.stream()
                .collect(Collectors.groupingBy(this::buildMenuGroupKey));

        for (Map.Entry<MenuGroupKey, List<DcmMenu>> entry : addTreeByGroup.entrySet()) {
            saveTree(service, entry.getValue(), existedMenuTrees.computeIfAbsent(entry.getKey(),
                                                                                 key -> new MenuTreeAdaptor(Collections.emptyList())));
        }
    }

    private void saveTree(IService<Menu> service, List<DcmMenu> dcmMenus, MenuTreeAdaptor menuTreeAdaptor) {
        for (DcmMenu dcmMenu : dcmMenus) {
            menuTreeAdaptor.appendDcmMenu(dcmMenu);
        }
        List<String> parentCodePaths = menuTreeAdaptor.getDcmParentCodePaths();
        for (String parentCodePath : parentCodePaths) {
            List<DcmMenu> dcmMenusToAdd = menuTreeAdaptor.getDcmMenusByParentCodePath(parentCodePath);
            for (DcmMenu dcmMenuToAdd : dcmMenusToAdd) {
                Menu menu = convertDcm2Biz(dcmMenuToAdd);
                if (menu != null) {
                    service.save(menu);
                    menuTreeAdaptor.appendExistedMnu(menu);
                } else {
                    log.error("failed to add {}", dcmMenuToAdd);
                }
            }
        }
    }

    private DcmMenuGroupKey buildDcmMenuGroupKey(DcmMenu menu) {
        return DcmMenuGroupKey.builder()
                .prodCode(menu.getProdCode())
                .app(menu.getAppCode())
                .terminal(menu.getTerminalCode())
                .parentCodePath(menu.getParentCodePath())
                .build();
    }

    private MenuGroupKey buildMenuGroupKey(Long terminalId, Long appId) {
        TerminalKey terminalKey = supportRepo.getTerminalKeyOrDefault(terminalId, null);
        String appCode = supportRepo.getAppCodeOrDefault(appId, null);
        if (terminalKey == null || appCode == null) {
            return DUMMY_GROUP;
        } else {
            return MenuGroupKey.builder()
                    .prodCode(terminalKey.getProdCode())
                    .terminal(terminalKey.getTerminalCode())
                    .app(appCode)
                    .build();
        }
    }

    private MenuGroupKey buildMenuGroupKey(Menu bizEntity) {
        return buildMenuGroupKey(bizEntity.getTerminalId(), bizEntity.getApplicationId());
    }

    private MenuGroupKey buildMenuGroupKey(DcmMenu dcmEntity) {
        return MenuGroupKey.builder()
                .prodCode(dcmEntity.getProdCode())
                .app(dcmEntity.getAppCode())
                .terminal(dcmEntity.getTerminalCode())
                .build();
    }

    @Override
    protected Comparator<DcmMenu> getDcmComparator() {
        return Comparator
                .comparing(DcmMenu::getProdCode)
                .thenComparing(DcmMenu::getTerminalCode)
                .thenComparing(DcmMenu::getAppCode)
                .thenComparing(DcmMenu::getParentCodePath)
                .thenComparing(DcmMenu::getCode);
    }

    /**
     * 用于对全局范围内的Menu进行分组的Key. 相同Key下的Menu形成一颗树
     */
    @Data
    @Builder
    static class MenuGroupKey {
        private String prodCode;
        private String terminal;
        private String app;
    }

    /**
     * 这个是DCM中对实体进行分组的Key. 相同Key下的实体可以合并使用同一个RCI进行表达
     */
    @Data
    @Builder
    static class DcmMenuGroupKey {
        private String prodCode;
        private String terminal;
        private String app;
        private String parentCodePath;
    }

    private static final MenuGroupKey DUMMY_GROUP = MenuGroupKey.builder().build();
}
