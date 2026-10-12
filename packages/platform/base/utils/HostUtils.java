package com.guhecloud.platform.base.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 主机信息工具类
 *
 * @author Woody
 * @date 2021/5/27
 */
public final class HostUtils {
    private HostUtils() {}

    /**
     * 本地主机地址
     */
    public static final String LOCAL_ADDRESS;

    /**
     * 未知地址
     */
    public static final String UNKNOWN_ADDRESS = "unknown";

    /**
     * 本地主机默认名称
     */
    public static final String DEFAULT_LOCAL_NAME = "localhost";

    /**
     * 本地主机默认地址
     */
    public static final String DEFAULT_LOCAL_ADDRESS = "127.0.0.1";

    /**
     * 本地主机默认地址IPV6形式
     */
    public static final String DEFAULT_LOCAL_ADDRESS_IPV6 = "0:0:0:0:0:0:0:1";

    static {
        LOCAL_ADDRESS = getLocalhostAddress();
    }

    /**
     * 获取本地主机地址
     *
     * @return 主机地址
     */
    private static String getLocalhostAddress() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Get localhost address failed: " + e.getMessage());
        }
        return DEFAULT_LOCAL_ADDRESS;
    }
}
