package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dao.MachinesMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.Machines;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.events.MachinesUpdateEvent;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.MachinesService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.KevValueString;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesRoomVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 机组
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MachinesServiceImpl extends ServiceImpl<MachinesMapper, Machines> implements MachinesService {
    private final FileClient fileClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final TenantClient tenantClient;
    private final MachinesMapper machinesMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 机组分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MachinesVO> getMachinesPage(MachinesQueryParam param) {
        /* 查询分页数据 */
        Page<Machines> page = this.page(PagingUtils.convert(param), Wrappers.<Machines>lambdaQuery()
                                                .eq(param.getTenant() != null, Machines::getTenant, param.getTenant())
                                                .like(StringUtils.isNotBlank(param.getCode()), Machines::getCode, param.getCode())
                                                .orderByDesc(Machines::getCreateTime)
                                       );
        /* 获取文件 */
        Map<Long, FileVO> fileVOMap = new HashMap<>();
        List<Long> fileIdList = new ArrayList<>();
        page.getRecords().forEach(po -> {
            if (StringUtils.isNotBlank(po.getFileIds())) {
                fileIdList.addAll(Stream.of(po.getFileIds().split(",")).map(Long::valueOf).toList());
            }
        });
        List<FileVO> fileVOList = fileClient.getByIds(fileIdList);
        Optional.ofNullable(fileVOList).orElse(new ArrayList<>()).forEach(f -> fileVOMap.put(f.getId(), f));
        /* 填充文件 */
        return PagingUtils.convert(page, page.getRecords().stream().map(po -> {
            MachinesVO vo = new MachinesVO();
            BeanUtils.copyProperties(po, vo);
            if (StringUtils.isNotBlank(vo.getFileIds())) {
                List<FileVO> list = new ArrayList<>();
                String[] fids = vo.getFileIds().split(",");
                Arrays.stream(fids).forEach(s -> {
                    FileVO fv = fileVOMap.get(Long.valueOf(s));
                    if (fv != null) {
                        list.add(fv);
                    }
                });
                vo.setFileList(list);
            }
            return vo;
        }).collect(Collectors.toList()));
    }

    /**
     * 新增或修改机组
     *
     * @param param 机组
     *
     * @return 主键
     */
    @Override
    public Long saveOrUpdateMachines(MachinesParam param) {
        // 校验code重复
        checkCodeRepeat(param.getId(), param.getCode(), param.getTenant());
        Machines po = new Machines();
        // 有id表示编辑
        if (param.getId() != null) {
            po = this.getById(param.getId());
            Assert.isNotFound(po == null, "机组不存在，请检查是否被删除！");
        }
        BeanUtils.copyProperties(param, po);
        this.saveOrUpdate(po);
        return po.getId();
    }

    /**
     * 机组详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @Override
    public MachinesVO getMachinesDetail(MachinesParam param) {
        MachinesVO result = new MachinesVO();
        Machines po = this.getOne(Wrappers.<Machines>lambdaQuery()
                                          .eq(param.getId() != null, Machines::getId, param.getId())
                                          .eq(StringUtils.isNotBlank(param.getCode()), Machines::getCode, param.getCode())
                                          .eq(param.getTenant() != null, Machines::getTenant, param.getTenant())
                                          .last(" limit 1 ")
                                 );
        Assert.isNotFound(po == null, "机组不存在");
        BeanUtils.copyProperties(po, result);
        // 如果存在文件id，获取文件
        if (StringUtils.isNotBlank(po.getFileIds())) {
            List<Long> fileIdList = new ArrayList<>();
            Arrays.stream(po.getFileIds().split(",")).forEach(fid -> fileIdList.add(Long.valueOf(fid)));
            if (CollectionUtils.isNotEmpty(fileIdList)) {
                List<FileVO> fileVOList = fileClient.getByIds(fileIdList);
                result.setFileList(fileVOList);
            }
        }

        return result;
    }

    /**
     * 移除
     *
     * @param id 主键
     */
    @Override
    public void removeMachines(Long id) {
        this.remove(Wrappers.<Machines>lambdaQuery().eq(Machines::getId, id));
    }

    /**
     * 机组手术室
     *
     * @param param 条件 机组id
     *
     * @return 手术室列表(机组下的)
     */
    @Override
    public Pagination<MachinesRoomVO> getMachinesRoomPage(MachinesParam param) {
        Machines po = this.getById(param.getId());
        Assert.isNotFound(po == null, "机组不存在，请检查是否被删除！");
        if (!StringUtils.isNotBlank(po.getRoomIds())) {
            return Pagination.empty();
        }
        List<Long> deviceIdList = Arrays.stream(po.getRoomIds().split(",")).filter(StringUtils::isNoneBlank).map(Long::valueOf)
                .collect(Collectors.toList());
        DeviceFilterPageParam deviceParam = new DeviceFilterPageParam();
        BeanUtils.copyProperties(param, deviceParam);
        deviceParam.setMatchedDeviceIds(deviceIdList);
        Pagination<DevicePageInfoVO> devicePage = deviceCenterStdClient.deviceInfoPage(deviceParam);
        return PagingUtils.convert(devicePage, devicePage.getRecords().stream().map(device -> {
            MachinesRoomVO vo = new MachinesRoomVO();
            vo.setDeviceId(device.getId());
            vo.setDeviceCode(device.getCode());
            vo.setDeviceName(device.getName());
            if (device.getDepartment() != null) {
                vo.setDeptId(device.getDepartment().getId());
                vo.setDeptName(device.getDepartment().getName());
            }
            if (device.getProductInfo() != null) {
                vo.setProductType(device.getProductInfo().getId());
                vo.setProductTypeName(device.getProductInfo().getName());
            }
            if (device.getArea() != null) {
                vo.setAreaPathName(device.getArea().getPathName());
                vo.setAreaId(device.getArea().getId());
                vo.setAreaPathId(device.getArea().getPathId());
            }
            return vo;
        }).collect(Collectors.toList()));
    }

    /**
     * 编辑机组手术室
     *
     * @param param 入参
     */
    @Override
    public void saveOrUpdateMachinesRooms(MachinesParam param) {
        Machines po = this.getById(param.getId());
        Assert.isNotFound(po == null, "机组不存在，请检查是否被删除！");
        po.setRoomIds(param.getRoomIds());
        this.updateById(po);
        // 发出机组更新事件
        applicationEventPublisher.publishEvent(new MachinesUpdateEvent(this, po));
    }

    /**
     * 移除机组手术室
     *
     * @param param 条件
     */
    @Override
    public void removeMachinesRooms(MachinesParam param) {
        Assert.isParameterError(param == null || param.getId() == null || !StringUtils.isNotBlank(param.getRoomIds()), "参数错误！");
        Machines po = this.getById(param.getId());
        Assert.isNotFound(po == null, "机组不存在，请检查是否被删除！");
        List<String> roomIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(po.getRoomIds())) {
            roomIdList.addAll(Arrays.asList(po.getRoomIds().split(",")));
            List<String> paramList = new ArrayList<>(Arrays.asList(param.getRoomIds().split(",")));
            roomIdList.removeAll(paramList);
        }
        String roomIds = String.join(",", roomIdList);
        po.setRoomIds(StringUtils.isNotBlank(roomIds) ? roomIds : null);
        this.updateById(po);
        // 发出机组更新事件
        applicationEventPublisher.publishEvent(new MachinesUpdateEvent(this, po));
    }

    /**
     * 下载机组模板
     *
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("tenantName", "企业名称");
        headers.put("code", "机组编号");
        headers.put("arguments", "机组铭牌参数");
        writer.setHeaderAlias(headers);
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> m1 = new HashMap<>();
        m1.put("tenantName", "请填写已开通的企业名称，名称不超过50个字");
        m1.put("code", "请填写机组编号，编号不超过50个字。同企业下机组编号不可重复");
        m1.put("arguments", "请填写机组铭牌参数，不超过1000个字");
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
        String fileName = "机组模板" + DateUtils.format(LocalDateTime.now(), "yyyyMMdd");
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 导入机组
     *
     * @param file 文件
     */
    @Override
    public ImportResult importTemplateMachines(MultipartFile file) {
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
        // 获取企业数据
        Map<String, Integer> tenantMap = new HashMap<>();
        List<TenantDTO> tenantDTOList = tenantClient.listTenants("");
        Optional.ofNullable(tenantDTOList).orElse(new ArrayList<>()).forEach(t -> tenantMap.put(t.getName(), t.getCode()));
        // 处理数据
        List<Machines> datatList = this.handlerExcelData(dataSource, tenantMap, result);
        // 批量插入
        if (!CollectionUtils.isEmpty(datatList)) {
            List<Machines> insertList = new ArrayList<>();
            for (int i = 0; i < datatList.size(); i++) {
                insertList.add(datatList.get(i));
                if (i != 0 && i % 500 == 0) {
                    machinesMapper.insertMachinesBatch(insertList);
                    insertList = new ArrayList<>();
                }
            }
            if (!CollectionUtils.isEmpty(insertList)) {
                machinesMapper.insertMachinesBatch(insertList);
            }
        }
        // 返回处理结果
        return result;
    }

    private List<Machines> handlerExcelData(List<List<Object>> dataSource, Map<String, Integer> tenantMap, ImportResult result) {
        List<Machines> insertList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataSource)) {
            return insertList;
        }
        var nowDate = LocalDateTime.now();
        String loginAccount = SecurityContextHolder.getPrincipal() == null ? "" : SecurityContextHolder.getPrincipal().getAccount();
        String loginUserId = SecurityContextHolder.getPrincipal() == null ? "" : SecurityContextHolder.getPrincipal().getId().toString();
        ActorTypeEnum loginUserType = SecurityContextHolder.getPrincipal() == null ? ActorTypeEnum.SYSTEM
                                                                                   : ActorTypeEnum.safeValueOf(SecurityContextHolder
                                                                                                                       .getPrincipal()
                                                                                                                       .getType().name());
        Map<String, Integer> tenantMachinesCodeMap = new HashMap<>();
        List<Machines> oldMachinesList = machinesMapper.getMachinesAll();
        if (oldMachinesList != null) {
            oldMachinesList.forEach(m -> tenantMachinesCodeMap.put(m.getTenant() + m.getCode(), 0));
        }
        int success = 0;
        int fail = 0;
        List<KevValueString> errList = new ArrayList<>();
        for (int i = 2; i < dataSource.size(); i++) {
            List<Object> objects = dataSource.get(i);
            Machines po = new Machines();
            KevValueString kevValue = new KevValueString();
            kevValue.setKey(String.valueOf(i));

            StringBuilder sb = new StringBuilder();
            String tenantName = objects.get(0) == null ? "" : String.valueOf(objects.get(0));
            Integer tenant = tenantMap.get(tenantName);
            if (!StringUtils.isNotBlank(tenantName)) {
                sb.append("租户不能为空！");
            } else if (tenantName.length() > 50) {
                sb.append("租户名称不能大于50字。");
            } else if (tenant == null || tenant == 0) {
                sb.append("租户(").append(tenantName).append(")不存在！");
            }
            String machinesCode = objects.get(1) == null ? "" : String.valueOf(objects.get(1));
            if (!StringUtils.isNotBlank(machinesCode)) {
                sb.append("机组编号不能为空！");
            } else if (machinesCode.length() > 50) {
                sb.append("机组编号不能大于50个字");
            }
            Integer oldIndex = tenantMachinesCodeMap.get(tenant + machinesCode);
            if (oldIndex != null && oldIndex > 0) {
                sb.append("与第").append(oldIndex).append("行，机组编号重复。");
            } else if (oldIndex != null && oldIndex == 0) {
                sb.append("租户").append(tenantName).append(")已存在机组编号(").append(machinesCode).append(")");
            }
            tenantMachinesCodeMap.put(tenant + machinesCode, i);
            String arguments = objects.size() >= 3 && objects.get(2) != null ? String.valueOf(objects.get(2)) : null;
            if (StringUtils.isNotBlank(arguments) && arguments.length() > 1000) {
                sb.append("机组参数字数不能大于1000");
            }
            if (!sb.isEmpty()) {
                kevValue.setValue(sb.toString());
                errList.add(kevValue);
                fail++;
                continue;
            }
            success++;
            po.setId(IdWorker.getId());
            po.setTenant(tenant);
            po.setCode(machinesCode);
            po.setArguments(arguments);
            po.setDeleted(false);

            EntityAuditor.populateAudit(po, loginAccount, loginUserId, loginUserType, nowDate);
            insertList.add(po);
        }
        result.setFail(fail);
        result.setSuccess(success);
        result.setErrorMsgList(errList);
        return insertList;
    }

    /**
     * 校验编码重复
     *
     * @param id     主键
     * @param code   编码
     * @param tenant 租户
     */
    private void checkCodeRepeat(Long id, String code, Integer tenant) {
        long count = this.count(Wrappers.<Machines>lambdaQuery()
                                        .eq(Machines::getCode, code)
                                        .eq(tenant != null, Machines::getTenant, tenant)
                                        .ne(id != null, Machines::getId, id)
                               );
        Assert.isConflict(count > BigDecimal.ZERO.intValue(), "机组编码已存在！");
    }
}
