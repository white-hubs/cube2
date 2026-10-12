package com.guhecloud.cube.base.openapi.server.function.engine.java.sandbox;

import java.util.List;
import java.util.Map;

public class RestrictedClassLoader extends ClassLoader {
    private final List<String> allowedPackages;

    private final Map<String, byte[]> compiledBytes;

    public RestrictedClassLoader(ClassLoader parent, List<String> allowedPackages, Map<String, byte[]> compiledBytes) {
        super(parent);
        this.allowedPackages = allowedPackages;
        this.compiledBytes = compiledBytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = compiledBytes.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // 检查是否允许加载这个类
        if (!isAllowed(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name, resolve);
    }

    private boolean isAllowed(String className) {
        // 检查类名是否以允许的包名开头
        for (String pkg : allowedPackages) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        // 另外，允许加载Java核心类（如java.lang ）？这里我们也可以将java.lang 加入白名单
        // 或者，我们可以设置一个规则：所有以"java."开头的类都允许加载，因为它们是JRE核心类
        if (className.startsWith("java.")) {
            return true;
        }
        // 还可以加入其他必要的包，比如动态加载的类本身所在的包
        return false;
    }
}