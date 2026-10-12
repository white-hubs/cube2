package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.job;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Sets;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.EmployeeHolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ExpireCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.event.ChangeExpiryDateEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayGrantMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 清理失效假期
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClearUpExpiryHolidayTask {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String waitingOfClearUpKey = "waitingOfClearUpList:";
    private final HolidayGrantMapper grantMapper;
    private final EmployeeHolidayCommandService employeeHolidayCommandService;
    private final HolidayTypeMapper typeMapper;

    @EventListener
    public void changeExpiryDateEventHandle(final ChangeExpiryDateEvent changeExpiryDateEvent) {
        if (Objects.nonNull(changeExpiryDateEvent.getExpiryDate()) &&
            changeExpiryDateEvent.getExpiryDate().getExpiryDateType() != ExpiryDate.ExpiryDateType.ALONG) {
            // 缓存需要清理的假期表
            HolidayTypePO holidayTypePO = typeMapper.selectById(changeExpiryDateEvent.getHolidayTypeId());
            redisTemplate.opsForValue().set(waitingOfClearUpKey + changeExpiryDateEvent.getHolidayTypeId(),
                                            JSONObject.toJSONString(
                                                    new CacheValue(changeExpiryDateEvent.getExpiryDate(), changeExpiryDateEvent.getHolidayTypeId(), holidayTypePO.getType())
                                                                   )
                                           );
        }
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    @Setter
    public static class CacheValue {
        private ExpiryDate expiryDate;
        private Long holidayTypeId;
        private Holiday.HolidayType holidayType;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CacheValue that = (CacheValue) o;
            return holidayTypeId.equals(that.holidayTypeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(holidayTypeId);
        }
    }

    /**
     * @param param
     *
     * @return
     */
    public void execute(String param) {
        if (log.isDebugEnabled()) {
            log.debug("清理失效假期任务开始执行，额外参数:{}", param);
        }
        // 1. 读取需要清理的假期表
        var keys = redisTemplate.keys(waitingOfClearUpKey + "*");
        if (CollectionUtils.isEmpty(keys)) {
            log.info("无可用清理的假期");
            return;
        }
        var waitingOfCleanUp = Objects.requireNonNull(redisTemplate.opsForValue().multiGet(keys)).stream()
                .map(value -> JSONObject.parseObject(value.toString(), CacheValue.class)).collect(Collectors.toList());
        var holidayGrantPOS = grantMapper.selectExpiringListIgnoreTenant(Sets.newHashSet(waitingOfCleanUp));
        if (CollectionUtils.isEmpty(holidayGrantPOS)) {
            if (log.isDebugEnabled()) {
                log.debug("无可用失效假期");
            }
            return;
        }
        holidayGrantPOS.forEach(holidayGrantPO -> {
            SessionContextHolder.requestTenant(holidayGrantPO.getTenant());
            SecurityContextHolder.setPrincipal(Principal.builder().account("system").type(Principal.Type.CLIENT).build());
            log.info("执行用户【{}】的发放记录:【{}】的【{}】假期失效", holidayGrantPO.getUserId(), holidayGrantPO.getId(), holidayGrantPO
                    .getHolidayType().getLabel());
            var expireCommand = new ExpireCommand(holidayGrantPO.getHolidayType(), holidayGrantPO.getUserId(), holidayGrantPO.getRemainingBalance());
            employeeHolidayCommandService.expire(expireCommand);
        });
        redisTemplate.delete(keys);
    }
}
