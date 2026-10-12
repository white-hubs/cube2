package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.service.impl;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.srv.gen.third.connection.server.global.vo.NameValueStringIntegerVO;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.dto.ClassInfoDTO;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.dto.NcdxEducationalDTO;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.service.NcdxVisualizationService;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.utils.NcdxThirdServiceHttpUtils;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.vo.NcdxEducationalVO;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.vo.TrainClassTreadVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NcdxVisualizationServiceImpl implements NcdxVisualizationService {
    private final DataDictionaryClient dataDictionaryClient;

    @Override
    public NcdxEducationalVO getEducational() {
        NcdxEducationalVO result = new NcdxEducationalVO();
        /* 获取字典配置的基地址*/
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode("THIRD_CONNECTION_ITEM");
        param.setCode("ncdxVisualizationBaseUrl");
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryClient.getDataDictionaryByItemAndCode(param);
        if (dataDictionaryDTO == null || StringUtils.isBlank(dataDictionaryDTO.getVariable())) {
            log.error("NcdxVisualizationServiceImpl.getEducational dataDictionaryConfig is null");
            return result;
        }
        /* 获取三方数据 */
        NcdxEducationalDTO ncdxEducationalDTO = NcdxThirdServiceHttpUtils.getNcdxEducational(dataDictionaryDTO.getVariable());
        ObjectUtils.copying(ncdxEducationalDTO, result);
        List<ClassInfoDTO> classInfoList = ncdxEducationalDTO.getClassInfo();
        if (classInfoList == null || classInfoList.isEmpty()) {
            return result;
        }
        /* 处理 */
        Integer nowYear = LocalDateTime.now().getYear();
        // 班级数量top
        List<NameValueStringIntegerVO> classTopList = classInfoList.stream()
                .filter(classInfoDTO -> classInfoDTO != null && classInfoDTO.getNum() != null &&
                                        nowYear.equals(classInfoDTO.getSchool_year()))
                .map(classInfoDTO -> NameValueStringIntegerVO.builder().name(classInfoDTO.getName()).value(classInfoDTO.getNum()).build())
                .sorted(Comparator.comparing(NameValueStringIntegerVO::getValue).reversed()).limit(5).collect(Collectors.toList());
        result.setClassTopList(classTopList);

        // 班次数量和培训人数趋势
        List<TrainClassTreadVO> treadList = new ArrayList<>();
        // 根据年份分组
        Map<Integer, List<ClassInfoDTO>> classYearMap = classInfoList.stream()
                .filter(classInfoDTO -> classInfoDTO != null && classInfoDTO.getSchool_year() != null)
                .collect(Collectors.groupingBy(ClassInfoDTO::getSchool_year));
        // 组装
        for (int i = nowYear - 4; i <= nowYear; i++) {
            List<ClassInfoDTO> list = classYearMap.get(i);
            if (CollectionUtils.isEmpty(classYearMap.get(i))) {
                treadList.add(TrainClassTreadVO.builder().year(i).classNumber(0).trainNumber(0).build());
            } else {
                TrainClassTreadVO treadVO = new TrainClassTreadVO();
                treadVO.setYear(i);
                treadVO.setClassNumber(list.stream().collect(Collectors.groupingBy(ClassInfoDTO::getBclx_text)).size());
                treadVO.setTrainNumber(list.stream().mapToInt(ClassInfoDTO::getNum).sum());
                treadList.add(treadVO);
            }
        }

        treadList.sort(Comparator.comparing(TrainClassTreadVO::getYear));
        result.setTreadList(treadList);
        return result;
    }

    @Override
    public NcdxEducationalVO getEducationalTest(String baseUrl) {
        NcdxEducationalVO result = new NcdxEducationalVO();
        /* 获取三方数据 */
        NcdxEducationalDTO ncdxEducationalDTO;
        if (StringUtils.isNotBlank(baseUrl)) {
            ncdxEducationalDTO = NcdxThirdServiceHttpUtils.getNcdxEducational(baseUrl);
        } else {
            ncdxEducationalDTO = NcdxThirdServiceHttpUtils.getNcdxEducationalTest(baseUrl);
        }
        ObjectUtils.copying(ncdxEducationalDTO, result);
        List<ClassInfoDTO> classInfoList = ncdxEducationalDTO.getClassInfo();
        if (classInfoList == null || classInfoList.isEmpty()) {
            return result;
        }
        /* 处理 */
        Integer nowYear = LocalDateTime.now().getYear();
        // 班级数量top
        List<NameValueStringIntegerVO> classTopList = classInfoList.stream()
                .filter(classInfoDTO -> classInfoDTO != null && classInfoDTO.getNum() != null &&
                                        nowYear.equals(classInfoDTO.getSchool_year()))
                .map(classInfoDTO -> NameValueStringIntegerVO.builder().name(classInfoDTO.getName()).value(classInfoDTO.getNum()).build())
                .sorted(Comparator.comparing(NameValueStringIntegerVO::getValue).reversed()).limit(5).collect(Collectors.toList());
        result.setClassTopList(classTopList);

        // 班次数量和培训人数趋势
        List<TrainClassTreadVO> treadList = new ArrayList<>();
        // 根据年份分组
        Map<Integer, List<ClassInfoDTO>> classYearMap = classInfoList.stream()
                .filter(classInfoDTO -> classInfoDTO != null && classInfoDTO.getSchool_year() != null)
                .collect(Collectors.groupingBy(ClassInfoDTO::getSchool_year));

        // 组装
        for (int i = nowYear - 4; i <= nowYear; i++) {
            List<ClassInfoDTO> list = classYearMap.get(i);
            if (CollectionUtils.isEmpty(classYearMap.get(i))) {
                treadList.add(TrainClassTreadVO.builder().year(i).classNumber(0).trainNumber(0).build());
            } else {
                TrainClassTreadVO treadVO = new TrainClassTreadVO();
                treadVO.setYear(i);
                treadVO.setClassNumber(list.stream().collect(Collectors.groupingBy(ClassInfoDTO::getBclx_text)).size());
                treadVO.setTrainNumber(list.stream().mapToInt(ClassInfoDTO::getNum).sum());
                treadList.add(treadVO);
            }
        }

        treadList.sort(Comparator.comparing(TrainClassTreadVO::getYear));
        result.setTreadList(treadList);
        return result;
    }
}
