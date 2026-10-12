package com.guhecloud.cube.base.flow.flowable.resolver.tool;

import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.ValueTypeEnum;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.param.UserBelongsInstitutionParam;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 22:13
 */
@Slf4j
@Component("exUtils")
public class ExUtils {

    public Float timeConvert(Float time, String timeUnit) {
        if ("HOUR".equals(timeUnit)) {
            return time / 8;
        }
        return time;
    }

    public Boolean strEquals(String controlId, String... values) {
        log.info("ExUtils:controlId:{};是否为空{};是否为空串：{}", controlId, ObjectUtils.isNull(controlId), "".equals(controlId));
        List<String> list = Arrays.asList(values);
        if (ObjectUtils.isEmpty(controlId) || list.size() > 1) {
            return Boolean.FALSE;
        } else {
            String s = list.get(0);
            if (controlId.contains("name=")) {
                //todo:临时方案直接截取字符串name属性判断
                String[] split = controlId.split("name=");
                return split[1].startsWith(s);
            }
            return s.equals(controlId);
        }
    }

    public Boolean strContains(String controlId, Object... values) {
        log.info("controlId:{}", controlId);
        List<String> list = Arrays.stream(values).map(String::valueOf).collect(Collectors.toList());
        if (controlId.contains("name=")) {
            List<String> strings = Arrays.asList(list.get(0).split(","));
            //todo:临时方案直接截取字符串name属性判断
            String[] split = controlId.split("name=");
            final String replace = split[1].split(", ")[0].replace("}", "");
            return strings.contains(replace);
        }
        return list.contains(controlId);
    }

    /**
     * 判断当前用户是否属于某个部门、角色、岗位、职位
     *
     * @param controlId
     * @param values
     *
     * @return
     */
    public Boolean userContains(String controlId, String... values) {
        log.info("controlId:{}，values：{}", controlId, values);

        if (Objects.isNull(values) || StringUtils.isEmpty(controlId)) {
            return false;
        }
        long checkId;
        try {
            checkId = Long.parseLong(controlId);
        } catch (Exception e) {
            List<String> objects = JsonUtils.deserializeToList(controlId, String.class);
            checkId = Long.parseLong(objects.getFirst());
        }

        Long type = Long.parseLong(values[0]);
        List<Long> institutionIds = Arrays.stream(values).map(Long::parseLong).filter(t -> !t.equals(type)).collect(Collectors.toList());
        if (ValueTypeEnum.getContainsValueTypes().contains(type)) {
            return institutionIds.contains(checkId);
        } else {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            UserBelongsInstitutionParam param = new UserBelongsInstitutionParam();
            param.setInstitutionIds(institutionIds);
            param.setUserId(checkId);
            param.setConditionType("OR");
            param.setType(ValueTypeEnum.getTypeName(type));
            return userClient.userBelongsInstitution(param);
        }
    }

    public Boolean numberContains(Number controlId, Number... values) {
        List<Number> list = Arrays.asList(values);
        return list.contains(controlId);
    }

    public Boolean b(Number controlId, Number... values) {
        List<Number> numbers = Arrays.asList(values);
        // 小数
        if (controlId.doubleValue() > numbers.get(0).doubleValue() && controlId.doubleValue() < numbers.get(1).doubleValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean ab(Number controlId, Number... values) {
        List<Number> numbers = Arrays.asList(values);
        if (controlId.doubleValue() >= numbers.get(0).doubleValue() && controlId.doubleValue() < numbers.get(1).doubleValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean ba(Number controlId, Number... values) {
        List<Number> numbers = Arrays.asList(values);
        if (controlId.doubleValue() > numbers.get(0).doubleValue() && controlId.doubleValue() <= numbers.get(1).doubleValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean aba(Number controlId, Number... values) {
        List<Number> numbers = Arrays.asList(values);
        if (controlId.doubleValue() >= numbers.get(0).doubleValue() && controlId.doubleValue() <= numbers.get(1).doubleValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断发起人是否是内部人员
     *
     * @param controlId
     *
     * @return
     */
    public Boolean isInsider(String controlId, String... values) {
        log.info("controlId:{}，values：{}", controlId);

        if (Objects.isNull(controlId) || StringUtils.isEmpty(controlId) || StringUtils.isEmpty(values[0])) {
            return false;
        }
        return Long.valueOf(controlId).equals(Long.valueOf(values[0]));
    }
}
