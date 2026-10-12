package com.guhecloud.cube.iot.mgt.base.bus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.commons.compiler.io.InputStreams;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class InitSystemCategoryDic {
    private static JSONArray STD_PROD_CATEGORY_ARRAY;

    static {
        try (InputStream inputStream = InitSystemCategoryDic.class.getResourceAsStream("/std-prod-category.json")) {
            if (Objects.nonNull(inputStream)) {
                byte[] bytes = InputStreams.readAll(inputStream);
                String json = new String(bytes, StandardCharsets.UTF_8);

                if (StringUtils.isBlank(json)) {
                    log.error("没有读取到标准品类");
                }

                STD_PROD_CATEGORY_ARRAY = JSON.parseArray(json);
            }
        } catch (IOException e) {
            throw new IllegalStateException("不能正确加载标准品类", e);
        }
    }

    @Resource
    private SysDictItemService sysDictItemService;
    @Resource
    private CommonDictItemService commonDictItemService;
    @Resource
    private PlatformEvnCom platformEvnCom;

    /**
     * 确保租户在系统中拥有标准品类
     *
     * @param tenantDTO 租户
     */
    public void ensureTenantCategoryDic(TenantDTO tenantDTO) {
        //Level-Name
        Map<String, SysDictItem> tempMap = new HashMap<>();

        List<SysDictItem> sysDictItemList = sysDictItemService.queryDictItemByIdAndTenantId(InnerDicEnum.prod_category.id, tenantDTO.getCode());
        if (Objects.nonNull(sysDictItemList)) {
            sysDictItemList.forEach(item -> tempMap.put(item.getItemLevel(), item));
        }

        SessionContextHolder.requestTenant(tenantDTO.getCode());
        List<SysDictItem> allCategoryDictItem = buildAllCategoryDictItem(tenantDTO);

        for (SysDictItem sysDictItem : allCategoryDictItem) {
            String tempMapKey = sysDictItem.getItemLevel();
            SysDictItem existItem = tempMap.get(tempMapKey);

            if (Objects.isNull(existItem)) {
                //c001002001 ==> c001 002 001
                String[] strings = platformEvnCom.hierarchicalNumbering.recoveryStrId(sysDictItem.getItemLevel(), true);

                //非顶层设置parentId
                if (strings.length > 1) {
                    String[] parentString = new String[strings.length];
                    System.arraycopy(strings, 0, parentString, 0, parentString.length - 1);

                    SysDictItem parentItem = tempMap.get(join(parentString));
                    sysDictItem.setItemParentId(parentItem.getId());
                }

                sysDictItemService.save(sysDictItem);
                commonDictItemService.updateSysDictItemTempTable(sysDictItem);

                tempMap.put(tempMapKey, sysDictItem);
            }
        }
    }

    private List<SysDictItem> buildAllCategoryDictItem(TenantDTO tenantDTO) {
        Deque<JSONObject> deque = new LinkedList<>();
        List<SysDictItem> list = new ArrayList<>();

        //丢进去
        for (Object object : STD_PROD_CATEGORY_ARRAY) {
            JSONObject jsonObject = (JSONObject) object;

            deque.push(jsonObject);
        }

        //广度优先
        while (!deque.isEmpty()) {
            JSONObject jsonObject = deque.pop();
            SysDictItem sysDictItem = new SysDictItem();
            sysDictItem.setId(platformEvnCom.nextIotPlatformSnowflakeId());
            sysDictItem.setDictId(InnerDicEnum.prod_category.id);
            sysDictItem.setItemLevel(jsonObject.getString("level"));
            sysDictItem.setItemLabel(jsonObject.getString("categoryName"));
            sysDictItem.setItemParentId(0L);
            sysDictItem.setItemValue(jsonObject.getString("categoryName"));
            sysDictItem.setTenant(tenantDTO.getCode());
            EntityAuditor.populateAudit(sysDictItem, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
            sysDictItem.setStatus(0);

            list.add(sysDictItem);

            JSONArray jsonArray = jsonObject.getJSONArray("sub");
            if (Objects.nonNull(jsonArray) && !jsonArray.isEmpty()) {
                //丢进去
                for (Object object : jsonArray) {
                    jsonObject = (JSONObject) object;

                    deque.push(jsonObject);
                }
            }
        }

        return list;
    }

    private String join(String[] strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {

            if (StringUtils.isNotBlank(str)) {
                sb.append(str);
            }
        }
        return sb.toString();
    }
}
