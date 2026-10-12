package com.guhecloud.cube.biz.mdata.server.stall.service.impl;

import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelReader;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.param.ExcelTemplate;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallCategoryService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.stall.vo.BatchOptResult;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallBatchOptService {

    private final AreaService areaService;

    private final StallService stallService;

    private final StallCategoryService stallCategoryService;

    private final OrganizationClient organizationClient;

    /**
     * build excel drop list provider<br/>
     * key: {@link com.guhecloud.cube.biz.mdata.server.excel.ExcelColumn # selectionType() selectionType}, val: drop list provider
     *
     * @return provider
     */
    public Map<String, ExcelColumnSelectionProvider> buildExcelColumnSelectionProvider() {
        Map<String, ExcelColumnSelectionProvider> map = new HashMap<>();
        map.put(ExcelTemplate.DEPARTMENT_TYPE_KEY, buildDepartmentType());
        map.put(ExcelTemplate.AREA_TYPE_KEY, buildAreaType());
        map.put(ExcelTemplate.STALL_TYPE_KEY, buildStallType());
        map.put(ExcelTemplate.LEASE_TYPE_KEY, buildStallLeaseType());
        map.put(ExcelTemplate.LEASED_TYPE_KEY, buildStallLeased());
        return map;
    }

    private ExcelColumnSelectionProvider buildStallType() {
        List<StallCategory> stallCategories = this.stallCategoryService.list();

        if (Objects.isNull(stallCategories)) {
            return Collections::emptyList;
        }

        List<ExcelColumnSelectionItem> result = stallCategories.stream()
                .map(stallCategory -> {
                    ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
                    item.setValue(String.valueOf(stallCategory.getId()));
                    item.setName(stallCategory.getName());
                    return item;
                })
                .collect(Collectors.toList());

        return () -> result;
    }

    /**
     * 区域类型<br/>
     * val: 2341-341-234(sequence)<br/>
     * name: 区域名
     *
     * @return provider
     */
    private ExcelColumnSelectionProvider buildAreaType() {
        List<Area> areaList = areaService.getList(new AreaQueryParam());
        if (Objects.isNull(areaList)) {
            return Collections::emptyList;
        }

        List<ExcelColumnSelectionItem> result = new ArrayList<>();
        for (Area area : areaList) {
            ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
            item.setValue(Arrays.asList(area.getSequence().split("-")).stream().collect(Collectors.joining(",")));
            item.setName(fillAreaName(area.getSequence()));
            result.add(item);
        }
        return () -> result;
    }

    /**
     * 填充区域名<br/>
     * 将 2323-2323-434 翻译为 北京-朝阳-望京
     *
     * @param areaSequence 区域序列
     *
     * @return 区域名
     */
    private String fillAreaName(String areaSequence) {
        String[] areaIds = null;
        if (!areaSequence.contains("-")) {
            areaIds = new String[]{ areaSequence };
        } else {
            areaIds = areaSequence.split("-");
        }

        List<Area> areaList = areaService.getByIds(Arrays.stream(areaIds)
                                                           .map(Long::parseLong).collect(Collectors.toList()));

        if (Objects.isNull(areaList)) {
            return "";
        }

        Map<Long, String> areaIdNameMap = new HashMap<>();
        for (Area area : areaList) {
            areaIdNameMap.put(area.getId(), area.getName());
        }

        StringBuilder fillAreaName = new StringBuilder();
        for (String areaId : areaIds) {
            fillAreaName.append(areaIdNameMap.get(Long.parseLong(areaId))).append("-");
        }

        return fillAreaName.substring(0, fillAreaName.length() - 1);
    }

    /**
     * 部门类型
     *
     * @return biz provider
     */
    private ExcelColumnSelectionProvider buildDepartmentType() {
        OrganizationQueryParam param = new OrganizationQueryParam();
        param.setEnabled(true);
        List<OrganizationDTO> organizationDTOS = organizationClient.getOrganizations(param);
        if (Objects.isNull(organizationDTOS) || organizationDTOS.isEmpty()) {
            return Collections::emptyList;
        }

        List<ExcelColumnSelectionItem> items = organizationDTOS.stream()
                .map(dto -> {
                    ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
                    item.setValue(Arrays.asList(dto.getSequence().split("-")).stream().collect(Collectors.joining(",")));
                    item.setName(dto.getName());
                    return item;
                }).collect(Collectors.toList());

        return () -> items;
    }

    private ExcelColumnSelectionProvider buildStallLeaseType() {
        List<ExcelColumnSelectionItem> items = new ArrayList<>();
        items.add(new ExcelColumnSelectionItem(Boolean.TRUE.toString(), "可租"));
        items.add(new ExcelColumnSelectionItem(Boolean.FALSE.toString(), "不可租"));
        return () -> items;
    }

    private ExcelColumnSelectionProvider buildStallLeased() {
        List<ExcelColumnSelectionItem> items = new ArrayList<>();
        items.add(new ExcelColumnSelectionItem(Boolean.TRUE.toString(), "已租"));
        items.add(new ExcelColumnSelectionItem(Boolean.FALSE.toString(), "未租"));
        return () -> items;
    }

    /**
     * 批量导入数据
     *
     * @param inputStream excel流
     *
     * @return errMsg
     */
    public BatchOptResult importFromTemplate(InputStream inputStream) {
        Requester requester = SessionContextHolder.getRequester();
        Principal principal = SecurityContextHolder.getPrincipal();

        List<String> saveModelErrorMsg = new CopyOnWriteArrayList<>();
        List<String> parsedErrorMsg = new ArrayList<>();//解析失败的消息
        long rowSize = 0;//总行数

        Map<String, ExcelColumnSelectionProvider> providerMap = buildExcelColumnSelectionProvider();
        try (ExcelReader<ExcelTemplate> excelReader = ExcelReader.<ExcelTemplate>builder()
                .selectionProviders(providerMap)
                .autClose(true)
                .pageSize(10)
                .modelClz(ExcelTemplate.class)
                .inputStream(inputStream)
                .synWaitConsumerDown(true)
                .modelConsumers(modelList -> {
                    SessionContextHolder.setRequester(requester);//租户信息
                    SecurityContextHolder.setPrincipal(principal);//设置用户信息
                    saveModelErrorMsg.addAll(stallService.excelSave(modelList, providerMap));//批量保存
                }).build()) {

            //等待读取
            excelReader.doRead();

            //获取解析错误的消息
            parsedErrorMsg = excelReader.fetchParseErrorMsgIfParsed();
            rowSize = excelReader.fetchCurtReadRowSize();
        } catch (Exception e) {
            log.error("close excel Reader fail", e);
            BatchOptResult batchOptResult = new BatchOptResult();
            batchOptResult.setErrorMsg(Collections.singletonList("导入失败，原因：请根据档位模板样式上传"));
            batchOptResult.setSuccess(0);
            batchOptResult.setFail(0);
            return batchOptResult;
        }
        parsedErrorMsg.addAll(saveModelErrorMsg);
        //信息汇总
        BatchOptResult batchOptResult = new BatchOptResult();
        batchOptResult.setErrorMsg(parsedErrorMsg);
        batchOptResult.setSuccess(rowSize - parsedErrorMsg.size());
        batchOptResult.setFail(parsedErrorMsg.size());
        return batchOptResult;
    }

}
