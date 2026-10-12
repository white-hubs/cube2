package com.guhecloud.cube.biz.ouaa.server.permission.init;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.ouaa.server.permission.cache.UserPermissionCache;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EventBusListen(order = 1)
public class UserPermissionCacheRunnerInit {

    private final UserPermissionCache userPermissionCache;

    @Subscribe
    @AllowConcurrentEvents
    public void loadPermissionCache(SysReadEvent event) throws Exception {
        this.userPermissionCache.loadUserPermissions();
    }
}
