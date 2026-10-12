package com.guhecloud.kaihe.scada.server.infrastructure.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServletUtils {

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getWebRootPath(HttpServletRequest req, String path) {
        return req.getSession().getServletContext().getRealPath(path).replace('\\', '/');
    }

    public static void respondFileInfo(String fileName, int contentLength, HttpServletResponse resp) {
        resp.setContentLength(contentLength);
        resp.setContentType(ContentTypeUtils.getContentType(fileName));
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Pragma", "no-cache");
        resp.addHeader("Cache-Control", "no-cache");
        resp.addHeader("Expires", "0");
        try {
            resp.addHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + "\"");
        } catch (UnsupportedEncodingException e) {
            resp.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        }
    }

    public static void respondBytes(byte[] bytes, HttpServletResponse response) throws IOException {
        FileCopyUtils.copy(bytes, response.getOutputStream());
    }

}
