package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.http.HttpConstants;
import com.guhecloud.platform.base.model.Requester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * author: ht
 * create: 2025-05-23 10:22
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DoorRequesterHelper {
    public boolean rebuildTenant(Integer tenant)  {
        Requester requester = SessionContextHolder.getRequester();
        Class<? extends Requester> requesterClass = requester.getClass();
        Field tenantField;
        try {

            tenantField = requesterClass.getDeclaredField(HttpConstants.HEADER_TENANT);
            tenantField.setAccessible(true);
            tenantField.set(requester, tenant);
            return true;
        } catch (NoSuchFieldException e) {
            log.error("tenant字段不存在");
        } catch (IllegalAccessException e) {
            log.error("tenant字段设置失败");
        }
        return false;
    }
}
