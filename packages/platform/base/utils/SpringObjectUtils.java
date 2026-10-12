package com.guhecloud.platform.base.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SpringObjectUtils {
    private SpringObjectUtils() {}

    /**
     * 查找工程内部的对象类型
     *
     * @param pack      包名
     * @param condition 类对象条件判断
     *
     * @return 类对象列表
     */
    public static List<Class<?>> lookupClasses(String pack, Predicate<Class<?>> condition) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = (pack = pack.trim()).isEmpty() ? "**/*.class" :
                         (ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pack) +
                          "/**/*.class");

        // 获取对象资源文件
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将资源文件转换为类对象
        Stream.Builder<Class<?>> classes = Stream.builder();
        MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        ClassLoader classLoader = Objects.requireNonNull(ClassUtils.getDefaultClassLoader(), "Class loader not found");
        for (Resource resource : resources) {
            try {
                // 加载类对象
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                Class<?> clazz = classLoader.loadClass(reader.getClassMetadata().getClassName());
                if (clazz != null && condition.test(clazz)) {
                    classes.add(clazz);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException();
            }
        }
        return classes.build().distinct().collect(Collectors.toList());
    }

}
