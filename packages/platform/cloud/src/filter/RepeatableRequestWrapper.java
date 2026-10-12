package com.guhecloud.platform.cloud.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RepeatableRequestWrapper extends HttpServletRequestWrapper {

    /* 声明字节数组 */
    private byte[] requestBody;

    /* 声明request对象 */
    private HttpServletRequest request;

    /* 构造器将形参传递到类对象HttpServletRequest中 */
    public RepeatableRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    /**
     * @return ServletInputStream
     *
     * @throws IOException
     * @description: 重写Request中getInputStream方法
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == requestBody) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), baos);
            /* 转字节数组 */
            this.requestBody = baos.toByteArray();
        }

        /* 创建ServletInputStream,将复制的字节流存入特定方法中 */
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
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
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    /**
     * @return BufferedReader
     *
     * @throws IOException
     * @description: 创建输入流Reader对象, 调用已重写的输入流对象
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
