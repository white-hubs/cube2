//package com.guhecloud.cube.deliver.project.merchant.commons.utils;
//
//import cn.hutool.http.useragent.Browser;
//import com.guhecloud.cube.deliver.project.merchant.entity.DeviceType;
//import org.springframework.util.FileCopyUtils;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//public class ServletUtils {
//
//	public static String getIp(HttpServletRequest request) {
//		String ip = null;
//		ip = request.getHeader("x-forwarded-for");
//		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
//			ip = request.getRemoteAddr();
//			if (ip.equals("127.0.0.1")) {
//				InetAddress inet = null;
//				try {
//					inet = InetAddress.getLocalHost();
//				} catch (UnknownHostException e) {
//					throw new RuntimeException(e);
//				}
//				ip = inet.getHostAddress();
//			}
//		}
//		if ((ip != null) && (ip.length() > 15)) {
//			if (ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}
//		return ip;
//	}
//
//	public static String getBrowserName(HttpServletRequest request) {
//		String header = request.getHeader("User-Agent");
//		UserAgent userAgent = UserAgent.parseUserAgentString(header);
//		Browser browser = userAgent.getBrowser();
//		return browser.getName();
//	}
//
//	public static String getBrowserVersion(HttpServletRequest request) {
//		String header = request.getHeader("User-Agent");
//		UserAgent userAgent = UserAgent.parseUserAgentString(header);
//		Browser browser = userAgent.getBrowser();
//		Version version = browser.getVersion(header);
//		return version == null ? "Unknown" : version.getVersion();
//	}
//
//	public static String getOsName(HttpServletRequest request) {
//		String header = request.getHeader("User-Agent");
//		UserAgent userAgent = UserAgent.parseUserAgentString(header);
//		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
//		return operatingSystem.getName();
//	}
//
//	public static String getDeviceType(HttpServletRequest request) {
//		String agentString = request.getHeader("User-Agent");
//		UserAgent userAgent = UserAgent.parseUserAgentString(agentString);
//		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
//		DeviceType deviceType = operatingSystem.getDeviceType();
//		switch (deviceType) {
//		case COMPUTER:
//			return "PC";
//		case TABLET: {
//			if (agentString.contains("Android"))
//				return "Android Pad";
//			if (agentString.contains("iOS"))
//				return "iPad";
//			return "Unknown";
//		}
//		case MOBILE: {
//			if (agentString.contains("Android"))
//				return "Android";
//			if (agentString.contains("iOS"))
//				return "IOS";
//			if (agentString.contains("iPhone"))
//				return "IOS";
//			return "Unknown";
//		}
//		default:
//			return "Unknown";
//		}
//	}
//
//	public static String getWebRootPath(HttpServletRequest req, String path) {
//		return req.getSession().getServletContext().getRealPath(path).replace('\\', '/');
//	}
//
//	public static void respondFileInfo(String fileName, int contentLength, HttpServletResponse resp) {
//		resp.setContentLength(contentLength);
//		resp.setContentType(ContentTypeUtils.getContentType(fileName));
//		resp.setCharacterEncoding("UTF-8");
//		resp.addHeader("Pragma", "no-cache");
//		resp.addHeader("Cache-Control", "no-cache");
//		resp.addHeader("Expires", "0");
//		try {
//			resp.addHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + "\"");
//		} catch (UnsupportedEncodingException e) {
//			resp.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
//		}
//	}
//
//	public static void respondBytes(byte[] bytes, HttpServletResponse response) throws IOException {
//		FileCopyUtils.copy(bytes, response.getOutputStream());
//	}
//
//}
