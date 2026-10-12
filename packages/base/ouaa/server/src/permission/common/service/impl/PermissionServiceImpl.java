package com.guhecloud.cube.biz.ouaa.server.permission.common.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dao.PermissionMapper;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dto.ApplicationNode;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dto.MenuNode;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dto.TerminalNode;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.PermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.PermissionVO;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionPO> implements PermissionService {
    private final TerminalService terminalService;
    private final TerminalApplicationService terminalApplicationService;
    private final ApplicationService applicationService;
    private final MenuService menuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull PermissionModify permissionModify) {
        PermissionPO permissionPO = ObjectUtils.copying(permissionModify, PermissionPO.class);
        this.baseMapper.insert(permissionPO);
        return permissionPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull PermissionModify permissionModify) {
        PermissionPO permissionPO = this.baseMapper.selectById(id);
        if (Objects.nonNull(permissionPO)) {
            ObjectUtils.copying(permissionModify, permissionPO);
            this.baseMapper.updateById(permissionPO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<PermissionVO> getPermissions(String keyword) {
        List<PermissionPO> permissionPOS = this.list(Wrappers.<PermissionPO>lambdaQuery()
                                                             .and(StringUtils.isNotBlank(keyword), q -> q
                                                                     .like(PermissionPO::getCode, keyword)
                                                                     .or()
                                                                     .like(PermissionPO::getName, keyword)
                                                                     .orderByDesc(PermissionPO::getCreateTime))
                                                    );
        return ObjectUtils.copying(permissionPOS, PermissionVO.class);
    }

    @Override
    public void export(HttpServletRequest req, HttpServletResponse response) {
        // 模拟数据，实际可以从数据库查出来
        var terminalNodeList = buildData();
        var filename = ExcelUtils.encodeFileName("系统权限.xlsx", req);
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        ExcelWriter excelWriter;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            log.error("excel 创建失败", e);
            throw new ServiceException("excel 写入失败！");
        }
        var writeSheet = EasyExcel.writerSheet("权限表格").build();
        // 关键！必须写一次，否则 writeSheetHolder 是 null
        excelWriter.write(Collections.emptyList(), writeSheet);
        var sheet = excelWriter.writeContext().writeSheetHolder().getSheet();
        var cellStyle = initStyle(sheet);
        //设置列宽度
        sheet.setColumnWidth(0, 35 * 256);   // 20表示20个字符宽度
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        var headerStyle = initHeaderStyle(sheet);
        writeCell(sheet, headerStyle, 0, 0, "访问端");
        writeCell(sheet, headerStyle, 0, 1, "功能应用");
        writeCell(sheet, headerStyle, 0, 2, "一级菜单");
        writeCell(sheet, headerStyle, 0, 3, "二级菜单");
        var colTerminal = 0; // 访问端
        var colApp = colTerminal + 1;      // 应用
        var colMenu = colApp + 1;     // 菜单
        var colPerm = colMenu + 1;     // 权限
        var rowIndex = 1;
        for (TerminalNode terminalNode : terminalNodeList) {
            var terminalRowSpan = countTerminalRows(terminalNode);
            // 写入访问端名称
            writeCell(sheet, cellStyle, rowIndex, colTerminal, terminalNode.getName());
            merge(sheet, rowIndex, rowIndex + terminalRowSpan - 1, colTerminal, colTerminal);
            for (ApplicationNode app : terminalNode.getApps()) {
                var appRowSpan = countApplicationRows(app);
                writeCell(sheet, cellStyle, rowIndex, colApp, app.getName());
                merge(sheet, rowIndex, rowIndex + appRowSpan - 1, colApp, colApp);
                for (MenuNode menuNode : app.getMenuNodes()) {
                    int menuColSpan = menuNode.getChildren().size();
                    writeCell(sheet, cellStyle, rowIndex, colMenu, menuNode.getName());
                    merge(sheet, rowIndex, rowIndex + Math.max(menuColSpan, 1) - 1, colMenu, colMenu);
                    if (menuColSpan == 0) {
                        rowIndex++; // 即使没有权限，也要至少占一个格子
                        continue;
                    }
                    for (MenuNode perm : menuNode.getChildren()) {
                        writeCell(sheet, cellStyle, rowIndex, colPerm, perm.getName());
                        rowIndex++;
                    }
                }
            }
        }
        excelWriter.write(Collections.emptyList(), writeSheet);
        excelWriter.finish();
    }

    private CellStyle initHeaderStyle(Sheet sheet) {
        var workbook = sheet.getWorkbook();
        // 字体设置（加粗、黑色、大小）
        var font = workbook.createFont();
        font.setBold(true);               // 加粗
        font.setColor(IndexedColors.BLACK.getIndex()); // 黑色字体
        font.setFontHeightInPoints((short) 12);        // 字体大小 (可改)

        // 样式设置
        var headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);      // 水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        headerStyle.setWrapText(true);                              // 自动换行

        // 设置边框
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        return headerStyle;
    }

    private CellStyle initStyle(Sheet sheet) {
        var workbook = sheet.getWorkbook();
        var cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);    // 水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle.setWrapText(true); // 自动换行（可选）
        return cellStyle;
    }

    private int countTerminalRows(TerminalNode terminalNode) {
        return terminalNode.getApps().stream().mapToInt(this::countApplicationRows).sum();
    }

    private int countApplicationRows(ApplicationNode app) {
        return app.getMenuNodes().stream().mapToInt(m -> m.getChildren().isEmpty() ? 1 : m.getChildren().size()).sum();
    }

    private void writeCell(Sheet sheet, CellStyle cellStyle, int rowIdx, int colIdx, String value) {
        var row = sheet.getRow(rowIdx);
        if (row == null) {
            row = sheet.createRow(rowIdx);
        }

        var cell = row.getCell(colIdx);
        if (cell == null) {
            cell = row.createCell(colIdx);
        }
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);

    }

    private void merge(Sheet sheet, int r1, int r2, int c1, int c2) {
        if (c2 < c1 || r2 < r1) {
            // 跳过非法合并，不然 POI 会直接炸
            return;
        }
        if (c1 == c2 && r1 == r2) {
            // 单格无需合并
            return;
        }
        sheet.addMergedRegion(new CellRangeAddress(r1, r2, c1, c2));
    }

    private List<TerminalNode> buildData() {
        //查询所有终端
        var terminals = terminalService.list();
        var terminalIds = ListUtils.convertItem(terminals, Terminal::getId);
        //查询终端下的应用
        var terApps = terminalApplicationService.lambdaQuery().in(TerminalApplication::getTerminalId, terminalIds).list();
        var terAppMapGroup = MapUtils.listGroupToMap(terApps, TerminalApplication::getTerminalId);
        var appIds = ListUtils.convertItem(terApps, TerminalApplication::getApplicationId);
        var apps = applicationService.lambdaQuery().in(Application::getId, appIds).list();
        var appMap = MapUtils.listToMap(apps, Application::getId, application -> application);
        //查询应用下的菜单
        var menus = menuService.lambdaQuery().in(Menu::getApplicationId, appIds).list();
        var menuAppMapGroup = MapUtils.listGroupToMap(menus, Menu::getApplicationId);
        //查询查询菜单下的功能权限
        return new ArrayList<>(terminals.stream().map(terminal -> {
            TerminalNode terminalNodeVO = new TerminalNode(terminal.getId(), terminal.getName(), new ArrayList<>());
            var terApplications = terAppMapGroup.getOrDefault(terminal.getId(), Collections.emptyList());
            var applicationNodes = terApplications.stream().map(application -> {
                var app = appMap.get(application.getApplicationId());
                ApplicationNode applicationNode = new ApplicationNode(application.getApplicationId(),
                                                                      Objects.nonNull(app) ? app.getName() : "", new ArrayList<>());
                var menuList = menuAppMapGroup.getOrDefault(application.getApplicationId(), Collections.emptyList());
                var parentMenus = menuList.stream().filter(menu -> Objects.isNull(menu.getParentId())).toList();
                var childMenus = menuList.stream().filter(menu -> Objects.nonNull(menu.getParentId())).toList();
                //根据父级菜单id进行分组
                var parentMenuGroup = MapUtils.listGroupToMap(childMenus, Menu::getParentId);
                var menuNodes = parentMenus.stream().map(menu -> {
                    var menuNode = new MenuNode(menu.getId(), menu.getName(), new ArrayList<>());
                    var children = parentMenuGroup.getOrDefault(menu.getId(), Collections.emptyList());
                    var childNods = children.stream().map(child -> new MenuNode(child.getId(), child.getName(), new ArrayList<>()))
                            .toList();
                    menuNode.setChildren(childNods);
                    return menuNode;
                }).toList();
                applicationNode.setMenuNodes(menuNodes);
                return applicationNode;
            }).toList();
            terminalNodeVO.setApps(applicationNodes);
            terminalNodeVO.setName(terminal.getName());
            return terminalNodeVO;
        }).toList());
    }
}
