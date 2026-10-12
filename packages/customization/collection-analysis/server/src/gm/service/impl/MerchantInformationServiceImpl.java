package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScaleScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.MerchantTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.SexEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.PurchaserExcelDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.WholesalerExcelDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.CountryAddressQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.CountryAddressResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.MerchantInformationResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.PurchaserErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.WholesalerErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.MerchantInformationMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.MerchantInformationPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.CountryAddressRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.service.MerchantInformationService;
import com.guhecloud.cube.biz.collection.analysis.gm.utils.excel.CascadeSelectTool;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.NumberUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商户画像
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInformationServiceImpl extends ServiceImpl<MerchantInformationMapper, MerchantInformationPO> implements MerchantInformationService {

    private final CountryAddressRepository countryAddressRepository;

    /**
     * 新增/修改商户画像
     *
     * @param param 商户数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateMerchantInformation(MerchantInformationParam param) {
        MerchantInformationPO po = new MerchantInformationPO();
        if (param.getId() != null) {
            po = this.getById(param.getId());
            Assert.isNotFound(po, "商户信息不存在，请检查是否被删除！");
            Principal principal = SecurityContextHolder.getPrincipal();
            po.setUpdateBy(principal == null ? "system" : principal.getAccount());
            po.setUpdateTime(LocalDateTime.now());
        } else {
            param.setCreateTime(null);
        }

        BeanUtils.copyProperties(param, po);
        if (MerchantTypeEnum.WHOLESALER.name().equals(po.getMerchantType())) {
            // 设置籍贯
            this.setNativePlaceIds(param.getNativePlaceIds(), po);
            po.setNativePlace(this.getCountryAddress(param.getNativePlaceIds()));
            // 设置上游来源
            this.setUpstreamSourceIds(param.getUpstreamSourceIds(), po);
            po.setUpstreamSource(this.getCountryAddress(param.getUpstreamSourceIds()));
        }

        this.saveOrUpdate(po);
        return po.getId();
    }

    /**
     * 删除
     *
     * @param id 主键id
     */
    @Override
    public void removeMerchantInformation(Long id) {
        this.removeById(id);
    }

    /**
     * 分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<MerchantInformationResult> pageMerchantInformation(MerchantInformationQueryParam param) {
        Page<MerchantInformationPO> page = this.page(PagingUtils.convert(param), getQueryWrapper(param));
        return PagingUtils.convert(page, MerchantInformationResult.class);
    }

    /**
     * 详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @Override
    public MerchantInformationResult detailMerchantInformation(Long id) {
        MerchantInformationResult result = new MerchantInformationResult();
        MerchantInformationPO po = this.getById(id);
        Assert.isNotFound(po, "商户信息不存在，请检查是否被删除！");
        BeanUtils.copyProperties(po, result);
        result.setNativePlaceIds(po.getNativePlaceIds());
        result.setUpstreamSourceIds(po.getUpstreamSourceIds());
        return result;
    }

    /**
     * 下载模板批发商模板
     *
     * @param dataList 数据列表
     * @param request  请求对象
     * @param response 返回对象
     */
    @Override
    public void downloadWholesalerTemplate(List<WholesalerExcelDTO> dataList, HttpServletRequest request, HttpServletResponse response) {
        List<String> headList = this.getHeaderList(MerchantTypeEnum.WHOLESALER.name());
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.writeHeadRow(ListUtils.newArrayList("不可更改"));
        writer.writeHeadRow(headList);
        if (!CollectionUtils.isEmpty(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                WholesalerExcelDTO dto = dataList.get(i);
                writer.writeCellValue(0, i + 2, dto.getName());
                writer.writeCellValue(1, i + 2, dto.getSex());
                writer.writeCellValue(2, i + 2, dto.getAge());
                writer.writeCellValue(3, i + 2, dto.getNativePlaceProvince());
                writer.writeCellValue(4, i + 2, dto.getNativePlaceCity());
                writer.writeCellValue(5, i + 2, dto.getUpstreamSourceProvince());
                writer.writeCellValue(6, i + 2, dto.getUpstreamSourceCity());
                writer.writeCellValue(7, i + 2, dto.getBusinessScaleNumber());
            }
        }

        if (!CollectionUtils.isEmpty(dataList)) {
            writer.write(dataList);
        }

        //级联下
        Map<String, List<String>> areaList = new LinkedHashMap<>();
        CountryAddressQueryParam addressQueryParam = new CountryAddressQueryParam();
        addressQueryParam.setDeep(1);
        List<CountryAddressResult> addressResultList = countryAddressRepository.getCountryAddressListTree(addressQueryParam);
        areaList.put("根节点", addressResultList.stream().map(a -> a.getName() + "_" + (a.getId())).collect(Collectors.toList()));
        for (CountryAddressResult countryAddressResult : addressResultList) {
            loopDate(countryAddressResult, areaList);
        }
        //下拉框区域
        List<Integer> nativePlaceColList = ListUtils.newArrayList(3, 4);
        List<Integer> upstreamSourceColList = ListUtils.newArrayList(5, 6);
        new CascadeSelectTool((XSSFWorkbook) writer.getWorkbook())
                .createSheet("sheet1")
                .createSelectDateList(areaList)
                .createTopName("根节点")

                .createSelectColList(nativePlaceColList)
                .createFirstRow(2)
                .setCascadeDropDownBox()

                .createSelectColList(upstreamSourceColList)
                .createFirstRow(2)
                .setCascadeDropDownBox()

                .download("批发商", request, response);
    }

    /**
     * 下载采购商模板
     *
     * @param dataList 数据
     * @param request  请求对象
     * @param response 返回对象
     */
    @Override
    public void downloadPurchaserTemplate(List<PurchaserExcelDTO> dataList, HttpServletRequest request, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.writeHeadRow(ListUtils.newArrayList("不可更改"));
        writer.writeHeadRow(this.getHeaderList(MerchantTypeEnum.PURCHASER.name()));
        //经营类型
        List<String> scopeList = Arrays.stream(BusinessScopeEnum.values()).map(BusinessScopeEnum::getDescribe).collect(Collectors.toList());
        // 下拉框范围
        CellRangeAddressList scopeCellRangeAddressList = new CellRangeAddressList(2, 65535, 4, 4);
        this.createClassesSelect(writer, scopeList, scopeCellRangeAddressList);
        //车辆类型
        List<String> vehicleList = Arrays.stream(VehicleTypeEnum.values()).map(VehicleTypeEnum::getDescribe).collect(Collectors.toList());
        // 下拉框范围
        CellRangeAddressList vehicleCellRangeAddressList = new CellRangeAddressList(2, 65535, 5, 5);
        this.createClassesSelect(writer, vehicleList, vehicleCellRangeAddressList);

        if (!CollectionUtils.isEmpty(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                PurchaserExcelDTO dto = dataList.get(i);
                writer.writeCellValue(0, i + 2, dto.getName());
                writer.writeCellValue(1, i + 2, dto.getSex());
                writer.writeCellValue(2, i + 2, dto.getAge());
                writer.writeCellValue(3, i + 2, dto.getArea());
                writer.writeCellValue(4, i + 2, dto.getBusinessScope());
                writer.writeCellValue(5, i + 2, dto.getVehicleType());
            }
        }
        ExcelUtils.downloadExcel(request, response, "采购商", writer);
    }

    /**
     * 导入批发商模板
     *
     * @param file 文件
     *
     * @return 失败集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WholesalerErrorExcelResult importWholesalerTemplate(Boolean replaceBool, MultipartFile file) {
        WholesalerErrorExcelResult result = new WholesalerErrorExcelResult();
        List<List<Object>> dataSource = this.excelRead(file);
        if (CollectionUtils.isEmpty(dataSource)) {
            return result;
        }
        Map<String, CountryAddressPO> addressPOMap = new HashMap<>();
        List<CountryAddressPO> addressPOList = countryAddressRepository.list();
        if (!CollectionUtils.isEmpty(addressPOList)) {
            addressPOMap = addressPOList.stream().collect(Collectors.toMap(k -> k.getName() + "_" + k.getId(), v -> v));
        }
        Map<String, Long> oldMerchantMap = this.getOldMerchantMap();
        List<MerchantInformationPO> insertList = new ArrayList<>();
        List<Long> deleteIdList = new ArrayList<>();
        List<WholesalerExcelDTO> errExcelList = new ArrayList<>();
        for (List<Object> rows : dataSource) {
            this.wholesalerExcelDataHandlerPO(rows, insertList, addressPOMap, oldMerchantMap, errExcelList, replaceBool, deleteIdList);
        }
        this.insertAndRemoveBath(insertList, deleteIdList);
        result.setTotal(dataSource.size());
        result.setSuccess(dataSource.size() - errExcelList.size());
        result.setErrorList(errExcelList);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertAndRemoveBath(List<MerchantInformationPO> insertList, List<Long> deleteIdList) {
        if (!CollectionUtils.isEmpty(deleteIdList)) {
            this.removeByIds(deleteIdList);
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            this.saveBatch(insertList);
        }
    }

    /**
     * 导入采购商模板
     *
     * @param file 文件
     *
     * @return 失败集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaserErrorExcelResult importPurchaserTemplate(Boolean replaceBool, MultipartFile file) {
        PurchaserErrorExcelResult result = new PurchaserErrorExcelResult();
        List<List<Object>> dataSource = this.excelRead(file);
        if (CollectionUtils.isEmpty(dataSource)) {
            return result;
        }
        Map<String, Long> oldMerchantMap = this.getOldMerchantMap();
        List<MerchantInformationPO> insertList = new ArrayList<>();
        List<Long> deleteIdList = new ArrayList<>();
        List<PurchaserExcelDTO> errExcelList = new ArrayList<>();
        for (List<Object> rows : dataSource) {
            MerchantInformationPO po = new MerchantInformationPO();
            this.excelRowHandCommonField(rows, po);
            po.setMerchantType(MerchantTypeEnum.PURCHASER.name());
            Object areaObj = rows.size() > 3 ? rows.get(3) : null;
            if (ObjectUtils.isNotEmpty(areaObj)) {
                po.setArea(String.valueOf(areaObj));
            }
            Object scopeObj = rows.size() > 4 ? rows.get(4) : null;
            if (ObjectUtils.isNotEmpty(scopeObj)) {
                po.setBusinessScope(BusinessScopeEnum.getCode(String.valueOf(scopeObj)));
            }
            Object vehicleObj = rows.size() > 5 ? rows.get(5) : null;
            if (ObjectUtils.isNotEmpty(vehicleObj)) {
                po.setVehicleType(VehicleTypeEnum.getCode(String.valueOf(vehicleObj)));
            }

            if (this.excelCheckPoHandler(po, oldMerchantMap, replaceBool, deleteIdList)) {
                insertList.add(po);
            } else {
                errExcelList.add(po.toPurchaserExcelDTO());
            }
        }

        this.insertAndRemoveBath(insertList, deleteIdList);
        result.setTotal(dataSource.size());
        result.setSuccess(dataSource.size() - errExcelList.size());
        result.setErrorList(errExcelList);
        return result;
    }

    private boolean excelCheckPoHandler(MerchantInformationPO po, Map<String, Long> oldMerchantMap, Boolean replaceBool,
                                        List<Long> deleteIdList
                                       ) {
        boolean result = true;
        if (StringUtils.isBlank(po.getName())) {
            result = false;
        } else if (StringUtils.isNotBlank(po.getSex()) && StringUtils.isBlank(SexEnum.getDescribe(po.getSex()))) {
            result = false;
        } else if (oldMerchantMap.get(po.getName() + "_" + po.getMerchantType()) != null) {
            if (Boolean.TRUE.equals(replaceBool)) {
                deleteIdList.add(oldMerchantMap.get(po.getName() + "_" + po.getMerchantType()));
            } else {
                result = false;
            }
        }
        return result;
    }

    private Map<String, Long> getOldMerchantMap() {
        Map<String, Long> oldMerchantMap = new HashMap<>();
        List<MerchantInformationPO> merchantPOList = this.list(Wrappers.<MerchantInformationPO>lambdaQuery()
                                                                       .select(MerchantInformationPO::getName, MerchantInformationPO::getId, MerchantInformationPO::getMerchantType));
        if (merchantPOList != null) {
            for (MerchantInformationPO po : merchantPOList) {
                oldMerchantMap.put(po.getName() + "_" + po.getMerchantType(), po.getId());
            }
        }
        return oldMerchantMap;
    }

    private List<List<Object>> excelRead(MultipartFile file) {
        List<List<Object>> dataSource = null;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            dataSource = reader.read(2);
        } catch (Exception e) {
            throw new ServiceException("excel读取失败,请重新下载模板后再试!");
        }
        if (CollectionUtils.isEmpty(dataSource)) {
            return Collections.emptyList();
        }
        return dataSource;
    }

    private void wholesalerExcelDataHandlerPO(List<Object> rows, List<MerchantInformationPO> insertList,
                                              Map<String, CountryAddressPO> addressPOMap, Map<String, Long> oldMerchantMap,
                                              List<WholesalerExcelDTO> errExcelList, Boolean replaceBool, List<Long> deleteIdList
                                             ) {
        MerchantInformationPO po = new MerchantInformationPO();
        this.excelRowHandCommonField(rows, po);
        po.setMerchantType(MerchantTypeEnum.WHOLESALER.name());
        StringBuilder nativePlace = new StringBuilder();
        Object nativePlaceProvinceObj = rows.size() > 3 ? rows.get(3) : null;
        if (nativePlaceProvinceObj != null) {
            CountryAddressPO nativePlaceProvincePO = addressPOMap.get(String.valueOf(nativePlaceProvinceObj));
            if (nativePlaceProvincePO != null) {
                po.setNativePlaceProvinceId(nativePlaceProvincePO.getId());
                nativePlace.append(nativePlaceProvincePO.getName()).append("-");
            }
        }
        Object nativePlaceCityObj = rows.size() > 4 ? rows.get(4) : null;
        if (nativePlaceCityObj != null) {
            CountryAddressPO nativePlaceCityPO = addressPOMap.get(String.valueOf(nativePlaceCityObj));
            if (nativePlaceCityPO != null) {
                po.setNativePlaceCityId(nativePlaceCityPO.getId());
                nativePlace.append(nativePlaceCityPO.getName()).append("-");
            }
        }
        if (!nativePlace.isEmpty()) {
            po.setNativePlace(nativePlace.deleteCharAt(nativePlace.length() - 1).toString());
        }

        StringBuilder upstreamSource = new StringBuilder();
        Object upstreamSourceProvinceObj = rows.size() > 5 ? rows.get(5) : null;
        if (upstreamSourceProvinceObj != null) {
            CountryAddressPO upstreamSourceProvincePO = addressPOMap.get(String.valueOf(upstreamSourceProvinceObj));
            if (upstreamSourceProvincePO != null) {
                po.setUpstreamSourceProvinceId(upstreamSourceProvincePO.getId());
                upstreamSource.append(upstreamSourceProvincePO.getName()).append("-");
            }
        }
        Object upstreamSourceCityObj = rows.size() > 6 ? rows.get(6) : null;
        if (upstreamSourceCityObj != null) {
            CountryAddressPO upstreamSourceCityPO = addressPOMap.get(String.valueOf(upstreamSourceCityObj));
            if (upstreamSourceCityPO != null) {
                po.setUpstreamSourceCityId(upstreamSourceCityPO.getId());
                upstreamSource.append(upstreamSourceCityPO.getName()).append("-");
            }
        }
        if (!upstreamSource.isEmpty()) {
            po.setUpstreamSource(upstreamSource.deleteCharAt(upstreamSource.length() - 1).toString());
        }
        Object businessScaleNumberObj = rows.size() > 7 ? rows.get(7) : null;
        if (businessScaleNumberObj != null && NumberUtils.isNumber(String.valueOf(businessScaleNumberObj))) {
            po.setBusinessScaleNumber(Integer.valueOf(String.valueOf(businessScaleNumberObj)));
        }

        if (this.excelCheckPoHandler(po, oldMerchantMap, replaceBool, deleteIdList)) {
            insertList.add(po);
        } else {
            errExcelList.add(po.toWholesalerExcelDTO());
        }
    }

    private void excelRowHandCommonField(List<Object> rows, MerchantInformationPO po) {
        Object nameObj = !rows.isEmpty() ? rows.get(0) : null;
        if (nameObj != null) {
            po.setName(String.valueOf(nameObj));
        }
        Object sexObj = rows.size() > 1 ? rows.get(1) : null;
        if (ObjectUtils.isNotEmpty(sexObj)) {
            po.setSex(SexEnum.getCode(String.valueOf(sexObj)));
        }
        Object ageObj = rows.size() > 2 ? rows.get(2) : null;
        if (ageObj != null && NumberUtils.isNumber(String.valueOf(ageObj))) {
            po.setAge(Integer.valueOf(String.valueOf(ageObj)));
        }
    }

    private void createClassesSelect(ExcelWriter writer, List<String> selectDataList, CellRangeAddressList cellRangeAddressList) {
        String selectSheetName = "selectSheetName";
        writer.setSheet(selectSheetName);
        Sheet sheet = writer.getSheet();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i < selectDataList.size(); i++) {
            writer.writeCellValue(i, lastRowNum + 1, selectDataList.get(i));
        }
        writer.getSheet().protectSheet("abc123");
        writer.setSheet("sheet1");
        String classesValida =
                "=" + selectSheetName + "!$A$" + (lastRowNum + 2) + ":$" + (char) (selectDataList.size() + 64) + "$" + (lastRowNum + 2);
        DataValidationHelper helper = new XSSFDataValidationHelper((XSSFSheet) writer.getSheet());
        DataValidationConstraint formulaListConstraint = helper.createFormulaListConstraint(classesValida);
        DataValidation dataValidation = helper.createValidation(formulaListConstraint, cellRangeAddressList);
        //设置只能选下拉菜单里的值不能随便输入
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setShowErrorBox(true);
        dataValidation.setEmptyCellAllowed(false);

        writer.addValidationData(dataValidation);
        // 将sheet隐藏
        writer.getWorkbook().setSheetHidden(1, true);
    }

    private void loopDate(CountryAddressResult result, Map<String, List<String>> areaList) {
        if (CollectionUtils.isEmpty(result.getChildList())) {
            return;
        }
        areaList.put(result.getName() + "_" + (result.getId()), result.getChildList().stream().map(a -> a.getName() + "_" + a.getId())
                .collect(Collectors.toList()));
        for (CountryAddressResult countryAddressResult : result.getChildList()) {
            loopDate(countryAddressResult, areaList);
        }
    }

    private List<String> getHeaderList(String merchantType) {
        List<String> headerList = new ArrayList<>();
        headerList.add("姓名");
        headerList.add("性别");
        headerList.add("年龄");
        if (MerchantTypeEnum.PURCHASER.name().equals(merchantType)) {
            headerList.add("区域");
            headerList.add("经营类型");
            headerList.add("采购车类型");
        } else if (MerchantTypeEnum.WHOLESALER.name().equals(merchantType)) {
            headerList.add("籍贯-省");
            headerList.add("籍贯-市");
            headerList.add("上游来源-省");
            headerList.add("上游来源-市");
            headerList.add("经营规模(吨)");
        }
        return headerList;
    }

    private void setNativePlaceIds(String nativePlaceIds, MerchantInformationPO po) {
        if (StringUtils.isBlank(nativePlaceIds) || po == null) {
            return;
        }
        String[] nativePlaceIdArr = nativePlaceIds.split(",");
        if (nativePlaceIdArr.length > 0) {
            po.setNativePlaceProvinceId(Long.valueOf(nativePlaceIdArr[0]));
        }
        if (nativePlaceIdArr.length > 1) {
            po.setNativePlaceCityId(Long.valueOf(nativePlaceIdArr[1]));
        }
        if (nativePlaceIdArr.length > 2) {
            po.setNativePlaceCountyId(Long.valueOf(nativePlaceIdArr[2]));
        }
    }

    private void setUpstreamSourceIds(String upstreamSourceIds, MerchantInformationPO po) {
        if (StringUtils.isBlank(upstreamSourceIds) || po == null) {
            return;
        }
        String[] upstreamSourceIdArr = upstreamSourceIds.split(",");
        if (upstreamSourceIdArr.length > 0) {
            po.setUpstreamSourceProvinceId(Long.valueOf(upstreamSourceIdArr[0]));
        }
        if (upstreamSourceIdArr.length > 1) {
            po.setUpstreamSourceCityId(Long.valueOf(upstreamSourceIdArr[1]));
        }
        if (upstreamSourceIdArr.length > 2) {
            po.setUpstreamSourceCountyId(Long.valueOf(upstreamSourceIdArr[2]));
        }
    }

    private String getCountryAddress(String ids) {
        String result = "";
        StringBuilder nameSb = new StringBuilder();
        if (StringUtils.isBlank(ids)) {
            return nameSb.toString();
        }
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            CountryAddressPO address = countryAddressRepository.getById(id);
            if (address != null) {
                nameSb.append(address.getName()).append("-");
            }
        }
        if (nameSb.length() > 0) {
            result = nameSb.deleteCharAt(nameSb.length() - 1).toString();
        }
        return result;
    }

    private LambdaQueryWrapper<MerchantInformationPO> getQueryWrapper(MerchantInformationQueryParam param) {
        LambdaQueryWrapper<MerchantInformationPO> wrapper = Wrappers.<MerchantInformationPO>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getMerchantType()), MerchantInformationPO::getMerchantType, param.getMerchantType())
                .eq(StringUtils.isNotBlank(param.getBusinessScope()), MerchantInformationPO::getBusinessScope, param.getBusinessScope())
                .eq(StringUtils.isNotBlank(param.getVehicleType()), MerchantInformationPO::getVehicleType, param.getVehicleType())
                .like(StringUtils.isNotBlank(param.getName()), MerchantInformationPO::getName, param.getName());
        if (StringUtils.isNotBlank(param.getBusinessScaleScope())) {
            if (BusinessScaleScopeEnum.THREE.name().equals(param.getBusinessScaleScope())) {
                wrapper.le(MerchantInformationPO::getBusinessScaleNumber, 3).ge(MerchantInformationPO::getBusinessScaleNumber, 0);
            } else if (BusinessScaleScopeEnum.FIVE.name().equals(param.getBusinessScaleScope())) {
                wrapper.le(MerchantInformationPO::getBusinessScaleNumber, 5).gt(MerchantInformationPO::getBusinessScaleNumber, 3);
            } else if (BusinessScaleScopeEnum.FIVE_ABOVE.name().equals(param.getBusinessScaleScope())) {
                wrapper.gt(MerchantInformationPO::getBusinessScaleNumber, 5);
            }
        }
        return wrapper;
    }
}
