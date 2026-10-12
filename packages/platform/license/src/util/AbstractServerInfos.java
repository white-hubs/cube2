package com.guhecloud.platform.license.util;

import com.guhecloud.platform.license.model.LicenseCheckModel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public abstract class AbstractServerInfos {

    /**
     * 组装需要额外校验的License参数
     *
     * @return demo.LicenseCheckModel
     *
     * @since 1.0.0
     */
    public LicenseCheckModel getServerInfos() {
        LicenseCheckModel result = new LicenseCheckModel();

        try {
            result.setIpAddress(this.getIpAddress());
            result.setMacAddress(this.getMacAddress());
            result.setCpuSerial(this.getCPUSerial());
            result.setMainBoardSerial(this.getMainBoardSerial());
        } catch (Exception e) {
            log.error("获取服务器硬件信息失败", e);
        }

        return result;
    }

    /**
     * 获取IP地址
     *
     * @return java.util.List<java.lang.String>
     *
     * @since 1.0.0
     */
    protected abstract List<String> getIpAddress() throws Exception;

    /**
     * 获取Mac地址
     *
     * @return java.util.List<java.lang.String>
     *
     * @since 1.0.0
     */
    protected abstract List<String> getMacAddress() throws Exception;

    /**
     * 获取CPU序列号
     *
     * @return java.lang.String
     *
     * @since 1.0.0
     */
    protected abstract String getCPUSerial() throws Exception;

    /**
     * 获取主板序列号
     *
     * @return java.lang.String
     *
     * @since 1.0.0
     */
    protected abstract String getMainBoardSerial() throws Exception;

    /**
     * 获取当前服务器所有符合条件的InetAddress
     *
     * @return java.util.List<java.net.InetAddress>
     *
     * @since 1.0.0
     */
    protected List<InetAddress> getLocalAllInetAddress() throws Exception {
        List<InetAddress> result = new ArrayList<>(4);

        // 遍历所有的网络接口
        for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
            NetworkInterface iface = (NetworkInterface) networkInterfaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration<InetAddress> inetAddresses = iface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                InetAddress inetAddr = inetAddresses.nextElement();

                //排除LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress类型的IP地址
                if (!inetAddr.isLoopbackAddress() /*&& !inetAddr.isSiteLocalAddress()*/
                    && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()) {
                    result.add(inetAddr);
                }
            }
        }

        return result;
    }

    /**
     * 获取某个网络接口的Mac地址
     *
     * @param
     *
     * @return void
     *
     * @since 1.0.0
     */
    protected String getMacByInetAddress(InetAddress inetAddr) {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    stringBuilder.append("-");
                }

                //将十六进制byte转化为字符串
                String temp = Integer.toHexString(mac[i] & 0xff);
                if (temp.length() == 1) {
                    stringBuilder.append("0").append(temp);
                } else {
                    stringBuilder.append(temp);
                }
            }
            return stringBuilder.toString().toUpperCase();
        } catch (SocketException e) {
            log.error("getMacByInetAddress error", e);
        }
        return null;
    }
}
