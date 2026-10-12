package com.guhecloud.cube.biz.ouaa.server.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.AliasConfigEnum;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/23 17:13
 * @Description:
 */

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomConfigUtil {
    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 访问端设置自定义配置
     *
     * @param terminalVOList
     */

    public void terminalCustomConfigResolving(List<TerminalVO> terminalVOList) {
        Map<String, DataDictionaryDTO> dataDictionaryDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(terminalVOList)) {
            Set<String> collect = terminalVOList.stream().map(TerminalVO::getCode).collect(Collectors.toSet());
            DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
            dictionaryaramParam.setItemCode(AliasConfigEnum.TERMINAL_ALIAS.name());
            dictionaryaramParam.setCodes(collect);
            List<DataDictionaryDTO> dictionaryByItem = dataDictionaryAdapter.getDataDictionaryByItem(dictionaryaramParam);
            if (!CollectionUtils.isEmpty(dictionaryByItem)) {
                dataDictionaryDTOMap = dictionaryByItem.stream().collect(Collectors.toMap(DataDictionaryDTO::getCode, Function.identity()));
            }
        }
        for (TerminalVO terminalVO : terminalVOList) {
            terminalVO.setAlias(dataDictionaryDTOMap.getOrDefault(terminalVO.getCode(), new DataDictionaryDTO()).getVariable());
        }
    }

    /**
     * 应用设置自定义配置
     *
     * @param applicationPageVOS
     * @param alisaFlag          是否配置解析
     */
    public void applicationCustomConfigResolving(List<ApplicationPageVO> applicationPageVOS, boolean alisaFlag) {
        Map<String, DataDictionaryDTO> dataDictionaryDTOMap = new HashMap<>();
        if (CollectionUtils.isEmpty(applicationPageVOS)) {
            return;
        }
        Set<String> collect = applicationPageVOS.stream().map(ApplicationPageVO::getCode).collect(Collectors.toSet());
        DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
        dictionaryaramParam.setItemCode(AliasConfigEnum.APPLICATION_ALIAS.name());
        dictionaryaramParam.setCodes(collect);
        List<DataDictionaryDTO> dictionaryByItem = dataDictionaryAdapter.getDataDictionaryByItem(dictionaryaramParam);
        if (!CollectionUtils.isEmpty(dictionaryByItem)) {
            dataDictionaryDTOMap = dictionaryByItem.stream().collect(Collectors.toMap(DataDictionaryDTO::getCode, Function.identity()));
        }
        for (ApplicationPageVO applicationPageVO : applicationPageVOS) {
            DataDictionaryDTO orDefault = dataDictionaryDTOMap.getOrDefault(applicationPageVO.getCode(), new DataDictionaryDTO());
            applicationPageVO.setAlias(orDefault.getVariable());
            if (alisaFlag) {
                this.replaceConfig(applicationPageVO, orDefault);
            }
        }
    }

    /**
     * 菜单设置自定义配置
     *
     * @param applicationMap
     * @param menuVOList
     * @param alisaFlag      是否配置解析
     */
    public void menuCustomConfigResolving(Map<Long, String> applicationMap, List<MenuVO> menuVOList, boolean alisaFlag) {
        DataDictionaryaramParam dictionaryParam = new DataDictionaryaramParam();
        dictionaryParam.setItemCode(AliasConfigEnum.MENU_ALIAS.name());
        Set<String> codes = applicationMap.values().stream().map(code -> SessionContextHolder.getRequester().getTerminal() + "::" + code)
                .collect(Collectors.toSet());
        dictionaryParam.setCodes(codes);
        List<DataDictionaryDTO> dictionaryByItem = dataDictionaryAdapter.getDataDictionaryByItem(dictionaryParam);
        if (CollectionUtils.isEmpty(dictionaryByItem)) {
            return;
        }
        Map<String, MenuVO> menuVOMap = menuVOList.stream().collect(Collectors.toMap(t ->
                                                                                             SessionContextHolder.getRequester()
                                                                                                     .getTerminal() + "::" +
                                                                                             applicationMap.get(t.getApplicationId()) +
                                                                                             "_" + t.getCode()
                , Function.identity(), (k1, k2) -> k1));
        for (DataDictionaryDTO dataDictionaryDTO : dictionaryByItem) {
            List<Map<String, String>> deserializeMapList = JsonUtils.deserialize(dataDictionaryDTO.getVariable(), new TypeReference<List<Map<String, String>>>() {
            });
            for (Map<String, String> stringStringMap : deserializeMapList) {
                String code = stringStringMap.getOrDefault("code", "");
                //字典编码加code确定唯一
                MenuVO menuVO = menuVOMap.get(dataDictionaryDTO.getCode() + "_" + code);
                if (Objects.isNull(menuVO)) {
                    continue;
                }
                menuVO.setAlias(stringStringMap);
                if (alisaFlag) {
                    String name = StringUtils.isNotBlank(stringStringMap.get("name")) ? stringStringMap.get("name") : menuVO.getName();
                    Float sort = StringUtils.isNotBlank(stringStringMap.get("sort")) ? Float.valueOf(stringStringMap.get("sort"))
                                                                                     : menuVO.getSort();
                    String icon = StringUtils.isNotBlank(stringStringMap.get("icon")) ? stringStringMap.get("icon") : menuVO.getIcon();
                    menuVO.setName(name);
                    menuVO.setSort(sort);
                    menuVO.setIcon(icon);
                }

            }
        }
        menuVOList.sort(Comparator.comparing(MenuVO::getSort));
    }

    /**
     * 自定义配置覆盖
     *
     * @param applicationPageVO
     * @param orDefault
     */
    private void replaceConfig(ApplicationPageVO applicationPageVO, DataDictionaryDTO orDefault) {
        if (StringUtils.isNotBlank(orDefault.getVariable())) {
            Map<String, String> configMap = JsonUtils.deserialize(orDefault.getVariable(), new TypeReference<Map<String, String>>() {
            });
            String name = StringUtils.isNotBlank(configMap.get("name")) ? configMap.get("name") : applicationPageVO.getName();
            String icon = StringUtils.isNotBlank(configMap.get("icon")) ? configMap.get("icon") : applicationPageVO.getIcon();
            applicationPageVO.setName(name);
            applicationPageVO.setIcon(icon);
        }
    }
}
