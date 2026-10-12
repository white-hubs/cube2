package com.guhecloud.platform.license.util;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取linux服务器信息
 *
 * @author lanchun
 * @date 2025/4/8 10:36
 */
@Slf4j
public class LinuxServerInfos extends AbstractServerInfos {
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
            //使用dmidecode命令获取CPU序列号
            String[] shell = { "/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1" };
            return getDmideCodeCmdResult(shell);
        } catch (Exception e) {
            log.error("get linux cpu serial error", e);
        }
        return null;
    }

    @Override
    protected String getMainBoardSerial() {
        try {
            //使用dmidecode命令获取主板序列号
            String[] shell = { "/bin/bash", "-c", "dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1" };
            return getDmideCodeCmdResult(shell);
        } catch (Exception e) {
            log.error("get linux main board serial number error", e);
        }
        return null;
    }

    public String getDmideCodeCmdResult(String[] shell) throws IOException {
        var process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            if (StringUtils.isNotEmpty(reader.readLine())) {
                var line = reader.readLine().trim();
                var serialNumber = "";
                if (StringUtils.isNotBlank(line)) {
                    serialNumber = line;
                }
                return serialNumber;
            }
        }
        return null;
    }
}
