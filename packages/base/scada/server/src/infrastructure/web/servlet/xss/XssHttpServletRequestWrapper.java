package com.guhecloud.kaihe.scada.server.infrastructure.web.servlet.xss;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] streamBody;
    private static final int BUFFER_SIZE = 4096;

    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        byte[] bytes = new byte[0];
        String uri = request.getRequestURI();
        // request.getInputStream在框架层被消耗了，所以在这两个接口处获取request.getInputStream()为空，则在此指定这两个接口处理，详解请百度
        if ("/Home/SaveDiagram".equals(uri) || "/Home/Load".equals(uri)) {
            if (Objects.isNull(request.getHeader("Content-Type")) ||
                request.getHeader("Content-Type").contains("application/x-www-form-urlencoded;")) {
                bytes = inputStream2Byte(request.getInputStream());
            } else if (isFormPost(request)) {
                // 从ParameterMap获取参数，并保存以便多次获取
                inputStream2Byte(request.getInputStream());
                bytes = params2Byte(request);
            } else {
                bytes = inputStream2Byte(request.getInputStream());
            }
        }
        streamBody = bytes;
    }

    @Override
    public String getHeader(String name) {
        return xssEncode(super.getHeader(name));
    }

    @Override
    public String getParameter(String name) {
        return xssEncode(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = xssEncode(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> returnMap = new HashMap<>();
        Iterator<Entry<String, String[]>> entries = super.getParameterMap().entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String[]> entry = entries.next();
            String name = entry.getKey();
            String[] originalValues = entry.getValue();
            String[] values = new String[originalValues.length];
            for (int i = 0; i < originalValues.length; i++) {
                values[i] = xssEncode(originalValues[i]);
            }
            returnMap.put(name, values);
        }
        return returnMap;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(streamBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    private static String xssEncode(String str) {
        return StringUtils.isEmpty(str) ? str : JsoupHelper.clean(str);
    }

    private boolean isFormPost(HttpServletRequest request) {
        return RequestMethod.POST.name().equals(request.getMethod());
    }

    private byte[] params2Byte(HttpServletRequest request) {
        byte[] bytes = request.getParameterMap().entrySet().stream().map(entry -> {
            String result;
            String[] value = entry.getValue();
            if (value != null && value.length > 1) {
                result = Arrays.stream(value).map(s -> entry.getKey() + "=" + s).collect(Collectors.joining("&"));
            } else {
                result = entry.getKey() + "=" + value[0];
            }

            return result;
        }).collect(Collectors.joining("&")).getBytes();
        return bytes;
    }

    private byte[] inputStream2Byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[BUFFER_SIZE];
        int length;
        while ((length = inputStream.read(bytes, 0, BUFFER_SIZE)) != -1) {
            outputStream.write(bytes, 0, length);
        }

        return outputStream.toByteArray();
    }

}
