package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelReader;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectSceneTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointExcelTemplate;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectSceneTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.BatchOptResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

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
public class ScenePointTempleHandlerImpl {

    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final InspectSceneTypeMapper inspectSceneTypeMapper;
    private final InspectScenePointService inspectScenePointService;

    public Map<String, ExcelColumnSelectionProvider> buildExcelColumnSelectionProvider() {
        Map<String, ExcelColumnSelectionProvider> map = new HashMap<>();
        map.put(ScenePointExcelTemplate.DEPARTMENT_TYPE_KEY, buildDepartmentType());
        map.put(ScenePointExcelTemplate.AREA_TYPE_KEY, buildAreaType());
        map.put(ScenePointExcelTemplate.POINT_TYPE_KEY, buildPointType());
        return map;
    }

    private ExcelColumnSelectionProvider buildPointType() {
        List<InspectSceneTypePO> sceneTypePOList = inspectSceneTypeMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isEmpty(sceneTypePOList)) {
            return Collections::emptyList;
        }
        List<ExcelColumnSelectionItem> result = sceneTypePOList.stream().map(po -> {
            ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
            item.setValue(String.valueOf(po.getId()));
            item.setName(po.getName());
            return item;
        }).collect(Collectors.toList());

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
        List<AreaDTO> areaList = areaClient.listAreas();
        if (Objects.isNull(areaList)) {
            return Collections::emptyList;
        }

        Map<String, String> map = new HashMap<>();
        areaList.forEach(po -> map.put(String.valueOf(po.getId()), po.getName()));

        List<ExcelColumnSelectionItem> result = new ArrayList<>();
        for (AreaDTO area : areaList) {
            ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
            List<String> sequenceList = Arrays.asList(area.getSequence().split("-"));
            item.setValue(String.join(",", sequenceList));
            StringBuilder stringBuilder = new StringBuilder();
            sequenceList.forEach(s -> {
                String name = map.get(s);
                if (StringUtils.isNotBlank(name)) {
                    stringBuilder.append(name).append("-");
                }
            });
            item.setName(StringUtils.isNotBlank(stringBuilder) ? stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString() : "");
            result.add(item);
        }
        return () -> result;
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

    /**
     * 批量导入数据
     *
     * @param inputStream excel流
     *
     * @return BatchOptResult errMsg
     */
    public BatchOptResult importFromTemplate(InputStream inputStream) {
        Requester requester = SessionContextHolder.getRequester();
        Principal principal = SecurityContextHolder.getPrincipal();

        List<String> saveModelErrorMsg = new CopyOnWriteArrayList<>();
        List<String> parsedErrorMsg = new ArrayList<>();//解析失败的消息
        long rowSize = 0;//总行数

        Map<String, ExcelColumnSelectionProvider> providerMap = buildExcelColumnSelectionProvider();
        try (ExcelReader<ScenePointExcelTemplate> excelReader = ExcelReader.<ScenePointExcelTemplate>builder()
                .selectionProviders(providerMap)
                .autClose(true)
                .pageSize(10)
                .modelClz(ScenePointExcelTemplate.class)
                .inputStream(inputStream)
                .synWaitConsumerDown(true)
                .modelConsumers(modelList -> {
                    SessionContextHolder.setRequester(requester);//租户信息
                    SecurityContextHolder.setPrincipal(principal);//设置用户信息
                    saveModelErrorMsg.addAll(inspectScenePointService.excelSave(modelList, providerMap));//批量保存
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
