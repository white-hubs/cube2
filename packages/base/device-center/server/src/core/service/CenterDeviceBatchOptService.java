package com.guhecloud.cube.base.device.center.core.service;

import com.guhecloud.cube.base.device.center.core.excel.ExcelColumn;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.base.device.center.core.excel.ExcelReader;
import com.guhecloud.cube.base.device.center.core.vo.BatchImportExcelTemplate;
import com.guhecloud.cube.base.device.center.core.vo.BatchOptResult;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

@Component
@Slf4j
public class CenterDeviceBatchOptService {

    @Resource
    private OrganizationClient organizationClient;

    @Resource
    private CenterDeviceService centerDeviceService;

    @Resource
    private UserClient userClient;
    @Resource
    private AreaClient areaClient;

    @Resource
    private SysDictionaryClient sysDictionaryClient;

    private static final String VIDEO_CODE = "video_code";

    /**
     * build excel drop list provider<br/>
     * key: {@link ExcelColumn#selectionType() selectionType}, val: drop list provider
     *
     * @return provider
     */
    public Map<String, ExcelColumnSelectionProvider> buildExcelColumnSelectionProvider() {
        Map<String, ExcelColumnSelectionProvider> map = new HashMap<>();
        //        map.put(BatchImportExcelTemplate.DEVICE_BIZ_TAG_KEY, buildDeviceBizTag());
        map.put(BatchImportExcelTemplate.DEPARTMENT_TYPE_KEY, buildDepartmentType());
        map.put(BatchImportExcelTemplate.AREA_TYPE_KEY, buildAreaType());
        map.put(BatchImportExcelTemplate.MANAGER_TYPE_KEY, buildManagerType());

        return map;
    }

    private ExcelColumnSelectionProvider buildManagerType() {
        Integer tenantId = SessionContextHolder.getRequester().getTenant();
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setEnabled(true);
        userQueryParam.setPageSize((int) Short.MAX_VALUE);
        List<UserDTO> userList = userClient.listUserByTenant(tenantId, userQueryParam).getRecords();

        if (Objects.isNull(userList)) {
            return Collections::emptyList;
        }

        List<ExcelColumnSelectionItem> result = userList.stream()
                .map(user -> {
                    ExcelColumnSelectionItem item = new ExcelColumnSelectionItem();
                    item.setValue(String.valueOf(user.getId()));
                    item.setName(user.getName());
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
        List<AreaListDTO> areaList = areaClient.listInnerArea(new AreaQueryParam());
        if (Objects.isNull(areaList)) {
            return Collections::emptyList;
        }

        // 构建 ID -> Name 映射
        Map<String, String> idToNameMap = areaList.stream()
                .collect(Collectors.toMap(area -> String.valueOf(area.getId()), AreaListDTO::getName));

        // 使用串行流处理映射转换
        List<ExcelColumnSelectionItem> result = areaList.stream()
                .map(area -> {
                    if (area.getParentId() == null) {
                        return new ExcelColumnSelectionItem(area.getSequence(), area.getName());
                    } else {
                        String[] sequenceArr = area.getSequence().split("-");
                        String sequenceArrName = Arrays.stream(sequenceArr)
                                .map(idToNameMap::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining("-"));
                        return new ExcelColumnSelectionItem(area.getSequence(), sequenceArrName);
                    }
                })
                .collect(Collectors.toList());
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

        List<AreaDTO> areaList = areaClient.getByIds(Arrays.stream(areaIds)
                                                             .map(Long::parseLong).collect(Collectors.toList()));

        if (Objects.isNull(areaList)) {
            return "";
        }

        Map<Long, String> areaIdNameMap = new HashMap<>();
        for (AreaDTO areaDTO : areaList) {
            areaIdNameMap.put(areaDTO.getId(), areaDTO.getName());
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
                    item.setValue(String.valueOf(dto.getId()));
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
     * @return errMsg
     */
    public BatchOptResult importFromTemplate(InputStream inputStream) {
        Requester requester = SessionContextHolder.getRequester();
        Principal principal = SecurityContextHolder.getPrincipal();

        List<String> saveModelErrorMsg = new CopyOnWriteArrayList<>();
        List<String> parsedErrorMsg = new ArrayList<>();//解析失败的消息
        long rowSize = 0;//总行数
        //获取视频的标签tagCode
        String tagCode;
        SysDictionaryDTO sysDictionaryByCode = sysDictionaryClient.getSysDictionaryByCode(VIDEO_CODE);
        if (!Objects.isNull(sysDictionaryByCode)) {
            tagCode = sysDictionaryByCode.getVariable();
        } else {
            tagCode = "";
        }

        Map<String, ExcelColumnSelectionProvider> providerMap = buildExcelColumnSelectionProvider();
        try (ExcelReader<BatchImportExcelTemplate> excelReader = ExcelReader.<BatchImportExcelTemplate>builder()
                .selectionProviders(providerMap)
                .autClose(true)
                .pageSize(10)
                .modelClz(BatchImportExcelTemplate.class)
                .inputStream(inputStream)
                .synWaitConsumerDown(true)
                .modelConsumers(modelList -> {
                    SessionContextHolder.setRequester(requester);//租户信息
                    SecurityContextHolder.setPrincipal(principal);//设置用户信息
                    saveModelErrorMsg.addAll(centerDeviceService.batchImportDevice(modelList, providerMap, tagCode));//批量保存
                }).build()) {

            //等待读取
            excelReader.doRead();

            //获取解析错误的消息
            parsedErrorMsg = excelReader.fetchParseErrorMsgIfParsed();
            rowSize = excelReader.fetchCurtReadRowSize();
        } catch (Exception e) {
            log.error("close excel Reader fail", e);
            BatchOptResult batchOptResult = new BatchOptResult();
            batchOptResult.setErrorMsg(Collections.singletonList("导入失败，原因：请根据设备模板样式上传"));
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
