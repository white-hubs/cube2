package com.guhecloud.platform.license.util;

import com.guhecloud.platform.license.model.LicenseCheckModel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerInfoUtils {

    private static LicenseCheckModel SERVER_INFO;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        // 初始化 SERVER_INFO
        refreshServerInfo();
        // 每隔10分钟刷新一次
        scheduler.scheduleAtFixedRate(ServerInfoUtils::refreshServerInfo, 10, 10, TimeUnit.MINUTES);
    }

    private static void refreshServerInfo() {
        SERVER_INFO = getServerInfos();
    }

    private static LicenseCheckModel getServerInfos() {
        // 操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
        return getServerInfos(osName);
    }

    public static LicenseCheckModel getServerInfos(String osName) {
        try {
            // 操作系统类型
            AbstractServerInfos abstractServerInfos = null;
            // 根据不同操作系统类型选择不同的数据获取方法
            if (osName.startsWith("windows")) {
                abstractServerInfos = new WindowsServerInfos();
            } else if (osName.startsWith("linux")) {
                abstractServerInfos = new LinuxServerInfos();
            } else { // 其他服务器类型
                abstractServerInfos = new LinuxServerInfos();
            }
            return abstractServerInfos.getServerInfos();
        } catch (Exception e) {
            log.error("getServerInfos error", e);
        }
        return null;
    }

    public static LicenseCheckModel getCheckModel() {
        return SERVER_INFO;
    }
}
