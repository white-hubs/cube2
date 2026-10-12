package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.CenterBizDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.CenterBizDeviceParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dao.MachinesMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dao.SurgeryItemMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dto.SurgeryItemExportDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.Machines;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.SurgeryItem;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.events.MachinesUpdateEvent;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.SurgeryItemService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.task.executor.ResourcesExecutorUtils;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ItemDevicePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.KevValueString;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.SurgeryItemVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * (手术室）物品
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurgeryItemServiceImpl extends ServiceImpl<SurgeryItemMapper, SurgeryItem> implements SurgeryItemService {
    private final MachinesMapper machinesMapper;
    private final OrganizationClient organizationClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final SurgeryItemMapper surgeryItemMapper;
    private final FileClient fileClient;
    private final TenantClient tenantClient;

    /**
     * 新增/编辑物品
     *
     * @param param 物品入参
     *
     * @return 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateSurgeryItem(SurgeryItemParam param) {
        SurgeryItem po = new SurgeryItem();
        if (param.getId() != null) {
            po = this.getById(param.getId());
            Assert.isNotFound(po == null, "物品不存在，请确认是否被删除！");
        }
        BeanUtils.copyProperties(param, po);
        /* 获取手术室的机组 */
        if (param.getDeviceId() != null) {
            Machines machine = machinesMapper.selectOne(Wrappers.<Machines>lambdaQuery().like(Machines::getRoomIds, param.getDeviceId())
                                                                .last(" limit 1 "));
            if (machine != null) {
                po.setMachinesId(machine.getId());
                po.setMachinesCode(machine.getCode());
            }
        }

        this.saveOrUpdate(po);
        return po.getId();
    }

    /**
     * 手术室物品分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<SurgeryItemVO> getSurgeryItemPage(SurgeryItemQueryParam param) {
        Page<SurgeryItem> page = this.page(PagingUtils.convert(param), Wrappers.<SurgeryItem>lambdaQuery()
                                                   .eq(param.getDeviceId() != null, SurgeryItem::getDeviceId, param.getDeviceId())
                                                   .eq(param.getTenant() != null, SurgeryItem::getTenant, param.getTenant())
                                                   .like(StringUtils.isNotBlank(param.getName()), SurgeryItem::getName, param.getName())
                                                   .orderByDesc(SurgeryItem::getCreateTime)
                                          );

        return PagingUtils.convert(page, SurgeryItemVO.class);
    }

    /**
     * 移除
     *
     * @param id 主键
     */
    @Override
    public void removeSurgeryItem(Long id) {
        Assert.isNotFound(id == null, "主键不能为空！");
        this.removeById(id);
    }

    /**
     * 查询物品详情分页列表
     *
     * @param param 条件
     *
     * @return 详情的分页列表
     */
    @Override
    public Pagination<ItemDevicePageVO> getSurgeryItemDetailPage(SurgeryItemQueryParam param) {
        Map<Long, OrganizationDTO> deptMap = new HashMap<>();
        Map<Long, CenterBizDeviceDTO> deviceMap = new HashMap<>();
        // 获取处理前置数据
        List<Long> deviceIdList = this.getDeviceList(param, deptMap, deviceMap);
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return Pagination.empty();
        }
        // 获取分页数据
        param.setDeviceIdList(deviceIdList);
        Page<ItemDevicePageVO> page = surgeryItemMapper.getSurgeryItemDevicePage(PagingUtils.convert(param), param);
        // 获取实际物品数据
        Map<Long, List<SurgeryItemVO>> deviceItemMap = new HashMap<>();
        SurgeryItemQueryParam detailParam = new SurgeryItemQueryParam();
        BeanUtils.copyProperties(param, detailParam);
        detailParam.setDeviceIdList(page.getRecords().stream().map(ItemDevicePageVO::getDeviceId).collect(Collectors.toList()));
        List<SurgeryItemVO> surgeryItemList = surgeryItemMapper.getSurgeryItemOnlyList(detailParam);
        if (surgeryItemList != null) {
            deviceItemMap = surgeryItemList.stream().collect(Collectors.groupingBy(SurgeryItemVO::getDeviceId));
        }
        // 获取文件信息
        Map<Long, FileVO> fileVOMap = new HashMap<>();
        List<Long> fileList = new ArrayList<>();
        page.getRecords().forEach(p -> {
            if (StringUtils.isNotBlank(p.getFileIds())) {
                String[] fids = p.getFileIds().split(",");
                for (String fid : fids) {
                    if (StringUtils.isNotBlank(fid)) {
                        fileList.add(Long.valueOf(fid));
                    }
                }
            }
        });
        List<FileVO> fileVOList = fileClient.getByIds(fileList);
        Optional.ofNullable(fileVOList).orElse(new ArrayList<>()).forEach(f -> fileVOMap.put(f.getId(), f));
        // 扩展详细信息
        this.extDataDetail(page.getRecords(), deviceItemMap, deptMap, deviceMap, fileVOMap, true);
        return PagingUtils.convert(page);
    }

    /**
     * 下载物品模板
     *
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("deviceCode", "手术室编号");
        headers.put("name", "物品名称");
        headers.put("size", "尺寸");
        headers.put("number", "数量");
        headers.put("manufacturer", "厂家");
        headers.put("model", "型号");
        writer.setHeaderAlias(headers);
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> m1 = new HashMap<>();

        m1.put("deviceCode", "必填，请填写手术室编号。");
        m1.put("name", "必填，请输入物品名称不超过50字，同手术室下不可重复。");
        m1.put("size", "非必填，字数不超过50个字。");
        m1.put("number", "非必填，字数不超过50个字。");
        m1.put("manufacturer", "非必填，字数不超过50个字。");
        m1.put("model", "非必填，字数不超过50个字。");
        dataList.add(m1);

        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setLocked(true);
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setLocked(false);
        /* 首行样式设置 */
        CellStyle describeStyle = writer.createCellStyle(0, 0);
        // 内容水平居中
        describeStyle.setAlignment(HorizontalAlignment.LEFT);
        // 内容垂直居中
        describeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置内容字体
        Font font = writer.createFont();
        font.setBold(true);
        font.setColor(Font.COLOR_RED);
        font.setItalic(true);
        //第二个参数表示是否忽略头部样式
        writer.getStyleSet().setFont(font, true);
        // 设置列宽
        writer.setColumnWidth(-1, 30);
        writer.setRowHeight(-1, 28);

        writer.write(dataList, true);

        // 导出
        String fileName = "物品模板" + DateUtils.format(LocalDateTime.now(), "yyyyMMdd");
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 导入物品
     *
     * @param file 文件
     */
    @Override
    public ImportResult importTemplateItem(MultipartFile file) {
        ImportResult result = new ImportResult();
        result.setFail(BigDecimal.ZERO.intValue());
        result.setSuccess(BigDecimal.ZERO.intValue());
        result.setErrorMsgList(new ArrayList<>());
        ExcelReader excelReader = null;
        try {
            excelReader = ExcelUtil.getReader(file.getInputStream(), 0);
        } catch (Exception e) {
            throw new ServiceException("文件读取失败，请重新下载模板再试!");
        }
        List<List<Object>> dataSource = excelReader.read();
        Assert.isParameterError(CollectionUtils.isEmpty(dataSource), "excel文件为空！");
        /* 获取设备 */
        List<Long> deptIdList = new ArrayList<>();
        OrganizationQueryParam deptParam = new OrganizationQueryParam();
        deptParam.setEnabled(true);
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(deptParam);
        if (!CollectionUtils.isEmpty(organizationDTOList)) {
            deptIdList = organizationDTOList.stream().map(OrganizationDTO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        }
        Map<String, CenterBizDeviceDTO> deviceDTOMap = new HashMap<>();
        CenterBizDeviceParam deviceParam = new CenterBizDeviceParam();
        deviceParam.setOrganizationList(deptIdList);
        List<CenterBizDeviceDTO> deviceDTOList = deviceCenterStdClient.getCenterBizDeviceByOrganizations(deviceParam);
        if (deviceDTOList != null) {
            for (CenterBizDeviceDTO centerBizDeviceDTO : deviceDTOList) {
                deviceDTOMap.put(centerBizDeviceDTO.getDeviceCode(), centerBizDeviceDTO);
            }
        }
        /* 获取设备机组关系 */
        Map<Long, Machines> machinesMap = new HashMap<>();
        List<Machines> machinesList = machinesMapper.getMachinesAll();
        Optional.ofNullable(machinesList).orElse(new ArrayList<>()).forEach(machines -> {
            if (StringUtils.isNotBlank(machines.getRoomIds())) {
                Arrays.stream(machines.getRoomIds().split(",")).map(Long::valueOf).forEach(ids -> machinesMap.put(ids, machines));
            }
        });

        // 处理数据
        List<SurgeryItem> datatList = this.handlerExcelData(dataSource, deviceDTOMap, machinesMap, result);
        // 批量插入
        if (!CollectionUtils.isEmpty(datatList)) {
            List<SurgeryItem> insertList = new ArrayList<>();
            for (int i = 0; i < datatList.size(); i++) {
                insertList.add(datatList.get(i));
                if (i != 0 && i % 500 == 0) {
                    surgeryItemMapper.insertItemBatch(insertList);
                    insertList = new ArrayList<>();
                }
            }
            if (!CollectionUtils.isEmpty(insertList)) {
                surgeryItemMapper.insertItemBatch(insertList);
            }
        }
        // 返回处理结果
        return result;
    }

    /**
     * 下载物品详情
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void downloadItemDetail(SurgeryItemQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        Map<Long, OrganizationDTO> deptMap = new HashMap<>();
        Map<Long, CenterBizDeviceDTO> deviceMap = new HashMap<>();
        // 获取处理前置数据
        List<Long> deviceIdList = this.getDeviceList(param, deptMap, deviceMap);
        Assert.isNotFound(CollectionUtils.isEmpty(deviceIdList), "暂无数据无法导出！");
        param.setDeviceIdList(deviceIdList);
        List<ItemDevicePageVO> itemVOList = surgeryItemMapper.getSurgeryItemDeviceList(param);
        Map<Long, List<SurgeryItemVO>> deviceItemMap = new HashMap<>();
        List<SurgeryItemVO> dataList = surgeryItemMapper.getSurgeryItemOnlyList(param);
        if (dataList != null) {
            deviceItemMap = dataList.stream().collect(Collectors.groupingBy(SurgeryItemVO::getDeviceId));
        }
        // 获取文件信息
        Map<Long, FileVO> fileVOMap = new HashMap<>();
        List<Long> fileList = new ArrayList<>();
        itemVOList.forEach(p -> {
            if (StringUtils.isNotBlank(p.getFileIds())) {
                String[] fids = p.getFileIds().split(",");
                for (String fid : fids) {
                    if (StringUtils.isNotBlank(fid)) {
                        fileList.add(Long.valueOf(fid));
                    }
                }
            }
        });
        List<FileVO> fileVOList = fileClient.getByIds(fileList);
        Optional.ofNullable(fileVOList).orElse(new ArrayList<>()).forEach(f -> fileVOMap.put(f.getId(), f));
        // 处理扩充数据
        this.extDataDetail(itemVOList, deviceItemMap, deptMap, deviceMap, fileVOMap, true);
        List<SurgeryItemExportDTO> excelData = new ArrayList<>();
        itemVOList.forEach(vo -> vo.getItemList().forEach(item -> {
            SurgeryItemExportDTO dto = new SurgeryItemExportDTO();
            BeanUtils.copyProperties(item, dto);
            excelData.add(dto);
        }));
        // 获取企业名称
        TenantDTO tenantDTO = tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
        String sheetName = tenantDTO != null && tenantDTO.getName() != null ? tenantDTO.getName() : "物品列表";
        // 导出
        this.itemDetailExport(excelData, itemVOList, sheetName, request, response);
    }

    /**
     * 监听机组更新事件
     *
     * @param event 事件
     */
    @EventListener
    public void groupStaffChangeListener(final MachinesUpdateEvent event) {
        if (event == null || event.getMachines() == null) {
            return;
        }
        Machines machines = event.getMachines();
        if (!StringUtils.isNotBlank(machines.getRoomIds())) {
            return;
        }
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Principal principal = SecurityContextHolder.getPrincipal();
        ThreadPoolExecutor executor = ResourcesExecutorUtils.getDefaultExecutor();
        executor.submit(() -> {
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            var nowTime = LocalDateTime.now();
            List<Long> roomIdList = Arrays.stream(machines.getRoomIds().split(",")).filter(StringUtils::isNoneBlank).map(Long::valueOf)
                    .collect(Collectors.toList());
            // 先重置该机组物品的机组信息
            this.update(Wrappers.<SurgeryItem>lambdaUpdate()
                                .set(SurgeryItem::getMachinesId, null)
                                .set(SurgeryItem::getMachinesCode, null)
                                .eq(SurgeryItem::getMachinesId, machines.getId())
                       );
            // 重新设置机组信息
            this.update(Wrappers.<SurgeryItem>lambdaUpdate()
                                .set(SurgeryItem::getMachinesId, machines.getId())
                                .set(SurgeryItem::getMachinesCode, machines.getCode())
                                .set(SurgeryItem::getUpdateTime, nowTime)
                                .in(SurgeryItem::getDeviceId, roomIdList)
                       );
        });

    }

    private void itemDetailExport(List<SurgeryItemExportDTO> excelData, List<ItemDevicePageVO> dataList, String sheetName,
                                  HttpServletRequest request, HttpServletResponse response
                                 ) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("deviceName", "手术室名称");
        headers.put("name", "物品名称");
        headers.put("size", "尺寸");
        headers.put("number", "数量");
        headers.put("manufacturer", "厂家");
        headers.put("model", "型号");
        headers.put("deptName", "所属部门");
        headers.put("machinesCode", "机组编号");
        headers.put("arguments", "机组铭牌参数");
        headers.put("fileIds", "机组铭牌参数附件");

        writer.setHeaderAlias(headers);
        Sheet sheet = writer.getSheet();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        int index = 1;
        List<File> tempFileList = new ArrayList<>();
        for (ItemDevicePageVO vo : dataList) {
            int size = vo.getItemList().size();
            if (size > 1) {
                writer.merge(index, index + size - 1, 0, 0, vo.getDeviceName(), true);
                writer.merge(index, index + size - 1, 6, 6, vo.getDeptName(), true);
                writer.merge(index, index + size - 1, 7, 7, vo.getMachinesCode(), true);
                writer.merge(index, index + size - 1, 8, 8, vo.getArguments(), true);
            }
            List<FileVO> fileVOList = vo.getFileList();
            if (!CollectionUtils.isEmpty(fileVOList)) {
                int fx = 0;
                for (FileVO fileVO : fileVOList) {
                    // 生成临时文件名
                    String tempFileName = UUID.randomUUID() + ".jpg";
                    File tempFile = new File(tempFileName);
                    // 下载照片到临时文件
                    HttpUtil.downloadFile(fileVO.getEndpointPath(), tempFile);
                    tempFileList.add(tempFile);
                    //设置图片单元格位置
                    ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 9 + fx, index, 10 + fx, index + size);
                    //随单元格改变位置和大小
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                    //添加图片
                    int pictureIndex = 0;
                    try {
                        pictureIndex = sheet.getWorkbook().addPicture(SpringFileUtils.getFileBytes(tempFile), Workbook.PICTURE_TYPE_JPEG);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    drawing.createPicture(anchor, pictureIndex);

                    fx++;
                }
            }

            index = index + vo.getItemList().size();
        }
        // 删除零时文件
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (File file : tempFileList) {
                file.delete();
            }
        });

        // 设置列宽
        writer.setColumnWidth(-1, 30);
        writer.setRowHeight(-1, 28);
        writer.write(excelData, true);
        writer.renameSheet(0, sheetName);
        // 导出
        String fileName = "物品列表" + DateUtils.format(LocalDateTime.now(), "yyyyMMdd");
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<SurgeryItem> handlerExcelData(List<List<Object>> dataSource, Map<String, CenterBizDeviceDTO> deviceDTOMap,
                                               Map<Long, Machines> machinesMap, ImportResult result
                                              ) {
        List<SurgeryItem> dataList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataSource)) {
            return dataList;
        }
        Map<String, Integer> tenantCodeNameMap = new HashMap<>();
        List<SurgeryItem> oldItemList = surgeryItemMapper.getSurgeryItemAll();
        List<SurgeryItem> insertList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldItemList)) {
            for (SurgeryItem surgeryItem : oldItemList) {
                tenantCodeNameMap.put(String.valueOf(surgeryItem.getTenant()) + surgeryItem.getDeviceId() + surgeryItem.getName(), 0);
            }
        }
        var nowDate = LocalDateTime.now();
        int success = 0;
        int fail = 0;
        List<KevValueString> errList = new ArrayList<>();
        for (int i = 2; i < dataSource.size(); i++) {
            List<Object> objects = dataSource.get(i);
            SurgeryItem po = new SurgeryItem();
            KevValueString kevValue = new KevValueString();
            kevValue.setKey(String.valueOf(i));

            // 校验
            StringBuilder sb = new StringBuilder();
            String deviceCode = objects.get(0) == null ? "" : String.valueOf(objects.get(0));
            CenterBizDeviceDTO deviceDTO = deviceDTOMap.get(deviceCode);
            if (!StringUtils.isNotBlank(deviceCode)) {
                sb.append("手术室编码不能为空!");
            } else if (deviceDTO == null) {
                sb.append("未找到手术室(").append(deviceCode).append(")。");
                kevValue.setValue(sb.toString());
                errList.add(kevValue);
                fail++;
                continue;
            }
            Machines machines = machinesMap.get(deviceDTO.getId());
            String name = objects.get(1) == null ? "" : String.valueOf(objects.get(1));
            if (!StringUtils.isNotBlank(name)) {
                sb.append("物品名称不能为空！");
            } else if (name.length() > 50) {
                sb.append("物品名称不能超过50个字！");
            }
            String k = String.valueOf(deviceDTO.getTenant()) + deviceDTO.getId() + name;
            Integer oldIndex = tenantCodeNameMap.get(k);
            if (oldIndex != null && oldIndex > 0) {
                sb.append("与第").append(oldIndex).append("行，手术室的物品名称(").append(name).append(")重复。");
            } else if (oldIndex != null && oldIndex == 0) {
                sb.append("手术室").append(deviceCode).append(")已存在(").append(name).append(")");
            }
            tenantCodeNameMap.put(k, i);
            String size = objects.size() >= 3 && objects.get(2) == null ? null : String.valueOf(objects.get(2));
            if (StringUtils.isNotBlank(size) && size.length() > 50) {
                sb.append("尺寸不能超过50个字！");
            }
            String number = null;
            if (objects.size() >= 4) {
                number = objects.get(3) == null ? null : String.valueOf(objects.get(3));
            }
            if (StringUtils.isNotBlank(number) && number.length() > 50) {
                sb.append("数量不能超过50个字！");
            }
            String manufacturer = null;
            if (objects.size() >= 5) {
                manufacturer = objects.get(4) == null ? null : String.valueOf(objects.get(4));
            }
            if (StringUtils.isNotBlank(manufacturer) && manufacturer.length() > 50) {
                sb.append("厂家不能超过50个字！");
            }
            String model = null;
            if (objects.size() >= 6) {
                model = objects.get(5) == null ? null : String.valueOf(objects.get(5));
            }
            if (StringUtils.isNotBlank(model) && model.length() > 50) {
                sb.append("型号不能超过50个字！");
            }
            if (sb.length() > 0) {
                kevValue.setValue(sb.toString());
                errList.add(kevValue);
                fail++;
                continue;
            }

            success++;
            po.setId(IdWorker.getId());
            po.setTenant(deviceDTO.getTenant());
            po.setDeleted(false);
            po.setDeviceId(deviceDTO.getId());
            po.setDeviceCode(deviceDTO.getDeviceCode());
            po.setDeviceName(deviceDTO.getDeviceName());
            po.setName(name);
            po.setNumber(number);
            po.setSize(size);
            po.setManufacturer(manufacturer);
            po.setModel(model);
            EntityAuditor.populateAudit(po);
            if (machines != null) {
                po.setMachinesId(machines.getId());
                po.setMachinesCode(machines.getCode());
            }
            insertList.add(po);
        }
        result.setFail(fail);
        result.setSuccess(success);
        result.setErrorMsgList(errList);
        return insertList;
    }

    private List<Long> getDeviceList(SurgeryItemQueryParam param, Map<Long, OrganizationDTO> deptMap,
                                     Map<Long, CenterBizDeviceDTO> deviceMap
                                    ) {
        List<Long> deptIdList = null;
        OrganizationQueryParam deptParam = new OrganizationQueryParam();
        deptParam.setEnabled(true);
        if (param.getDeptId() != null) {
            deptIdList = Collections.singletonList(param.getDeptId());
            deptParam.setIds(new HashSet<>(deptIdList));
        }
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(deptParam);
        if (organizationDTOList != null) {
            if (deptMap == null) {
                deptMap = new HashMap<>();
            }
            deptIdList = new ArrayList<>();
            for (OrganizationDTO dept : organizationDTOList) {
                deptIdList.add(dept.getId());
                deptMap.put(dept.getId(), dept);
            }
        }

        List<Long> deviceIdList = null;
        CenterBizDeviceParam deviceParam = new CenterBizDeviceParam();
        deviceParam.setOrganizationList(deptIdList);
        List<CenterBizDeviceDTO> deviceList = deviceCenterStdClient.getCenterBizDeviceByOrganizations(deviceParam);
        if (deviceList != null) {
            deviceIdList = new ArrayList<>();
            for (CenterBizDeviceDTO device : deviceList) {
                deviceMap.put(device.getId(), device);
                deviceIdList.add(device.getId());
            }
        }

        return deviceIdList;
    }

    /**
     * 扩展详情数据
     *
     * @param list      集合数据
     * @param dataMap   子数据map
     * @param deptMap   部门map
     * @param deviceMap 设备map
     * @param detailAll 子数据是否填充外层数据
     */
    private void extDataDetail(List<ItemDevicePageVO> list, Map<Long, List<SurgeryItemVO>> dataMap, Map<Long, OrganizationDTO> deptMap,
                               Map<Long, CenterBizDeviceDTO> deviceMap, Map<Long, FileVO> fileVOMap, Boolean detailAll
                              ) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (ItemDevicePageVO vo : list) {
            CenterBizDeviceDTO device = deviceMap.get(vo.getDeviceId());
            if (device != null) {
                // 扩展设备信息
                vo.setDeviceCode(device.getDeviceCode());
                vo.setDeviceName(device.getDeviceName());
                vo.setDeptId(device.getDepartmentId());
                OrganizationDTO dept = deptMap.get(device.getDepartmentId());
                if (dept != null && StringUtils.isNotBlank(dept.getSequence())) {
                    // 扩展(设备所属)部门信息
                    List<String> detpNameList = new ArrayList<>();
                    String[] sequences = dept.getSequence().split("-");
                    for (String sequence : sequences) {
                        OrganizationDTO d = deptMap.get(Long.valueOf(sequence));
                        if (d != null && StringUtils.isNotBlank(d.getName())) {
                            detpNameList.add(d.getName());
                        }
                    }
                    vo.setDeptName(String.join("/", detpNameList));
                }
                // 扩展文件信息
                if (StringUtils.isNotBlank(vo.getFileIds())) {
                    List<FileVO> fileVOList = new ArrayList<>();
                    String[] fids = vo.getFileIds().split(",");
                    for (String fid : fids) {
                        FileVO fileVO = fileVOMap.get(Long.valueOf(fid));
                        if (fileVO != null) {
                            fileVOList.add(fileVO);
                        }
                    }
                    vo.setFileList(fileVOList);
                }
                // 扩展详情数据
                List<SurgeryItemVO> detailList = dataMap.get(vo.getDeviceId());
                if (Boolean.TRUE.equals(detailAll) && !CollectionUtils.isEmpty(detailList)) {
                    detailList.forEach(detail -> {
                        detail.setDeviceName(device.getDeviceName());
                        detail.setDeviceCode(device.getDeviceCode());
                        detail.setMachinesCode(vo.getMachinesCode());
                        detail.setFileIds(vo.getFileIds());
                        detail.setDeptId(vo.getDeptId());
                        detail.setDeptName(vo.getDeptName());
                        detail.setArguments(vo.getArguments());
                        detail.setFileList(vo.getFileList());
                    });
                }
                vo.setItemList(detailList);
            }
        }
    }
}
