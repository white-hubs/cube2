package com.guhecloud.platform.license.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 获取windows服务器信息
 *
 * @author lanchun
 * @date 2025/4/8 10:37
 */
@Slf4j
public class WindowsServerInfos extends AbstractServerInfos {

    @Override
    protected List<String> getIpAddress() throws Exception {
        var inetAddresses = getLocalAllInetAddress();
        if (inetAddresses != null && !inetAddresses.isEmpty()) {
            return inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    protected List<String> getMacAddress() throws Exception {
        var inetAddresses = getLocalAllInetAddress();
        if (inetAddresses != null && !inetAddresses.isEmpty()) {
            //获取所有网络接口的Mac地址
            return inetAddresses.stream().map(this::getMacByInetAddress).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    protected String getCPUSerial() {
        try {
            return getWmicResult(new String[]{ "wmin", "cpu", "get", "processorid" });
        } catch (Exception e) {
            log.error("wmin cpu get processor id error", e);
        }
        return null;
    }

    @Override
    protected String getMainBoardSerial() {
        try {
            return getWmicResult(new String[]{ "wmic", "baseboard", "get", "serialnumber" });
        } catch (Exception e) {
            log.error("wmic get main board serial number error", e);
        }
        return null;
    }

    private String getWmicResult(String[] command) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        try (Scanner scanner = new Scanner(process.getInputStream(), "GBK")) {
            if (scanner.hasNext()) {
                scanner.next();
            }
            String serialNumber = "";
            if (scanner.hasNext()) {
                serialNumber = scanner.next().trim();
            }
            return serialNumber;
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }
}
