package com.guhecloud.cube.base.openapi.server.function.engine.java;

import com.guhecloud.cube.base.openapi.metadata.enumeration.FCEngineType;
import com.guhecloud.cube.base.openapi.server.entity.FunctionDefinition;
import com.guhecloud.cube.base.openapi.server.function.engine.ExecutorEngine;
import com.guhecloud.cube.base.openapi.server.function.engine.java.sandbox.JavaFileManagerFromString;
import com.guhecloud.cube.base.openapi.server.function.engine.java.sandbox.JavaSourceFromString;
import com.guhecloud.cube.base.openapi.server.function.engine.java.sandbox.RestrictedClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class JavaExecutorEngine implements ExecutorEngine {

    private final ConcurrentHashMap<String, byte[]> compiledBytes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Class<?>> loadedClasses = new ConcurrentHashMap<>();

    public JavaExecutorEngine() {
        // TODO: faked for test
        var legacyResponseWrapperPackage = "magic.cube.openapi.builtin";
        var legacyResponseWrapperClasspath = legacyResponseWrapperPackage + ".LegacyResponseWrapper";
        var packageDefinition = String.format("package %s;\n", legacyResponseWrapperPackage);
        Map<String, FunctionDefinition> functionDefinitions = Map.of(legacyResponseWrapperPackage, FunctionDefinition
                .builder()
                .classpath(legacyResponseWrapperClasspath)
                .FCEngineType(FCEngineType.JAVA)
                .sourceCode(
                        packageDefinition +
                        "import java.util.HashMap;\n" +
                        "import java.util.Map;\n" +
                        "import com.guhecloud.platform.base.util.StringUtils;\n" +
                        "public final class LegacyResponseWrapper {\n" +
                        "    public static String wrapperResponse(String responseBody) { \n" +
                        "       return String.format(\"{\\\"code\\\":200,\\\"data\\\": %s}\", responseBody); \n" +
                        "    }\n" +
                        "}")
                .build());
        var compiler = ToolProvider.getSystemJavaCompiler();
        compilerSource(compiler, functionDefinitions.get(legacyResponseWrapperPackage));
    }

    private void compilerSource(JavaCompiler compiler, FunctionDefinition fc) {
        var javaFileObject = new JavaSourceFromString(fc.getClasspath(), fc.getSourceCode());
        try (StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, Locale.getDefault(), StandardCharsets.UTF_8)) {
            JavaFileManager fileManager = new JavaFileManagerFromString(stdManager, compiledBytes);
            // 编译
            boolean success = compiler.getTask(null, fileManager, null, null, null, List.of(javaFileObject)).call();
            if (success) {
                log.info("Java source compiled successfully");
                ClassLoader loader = new RestrictedClassLoader(ClassLoader.getSystemClassLoader(),
                                                               List.of(
                                                                       "com.guhecloud.platform.base.util.StringUtils",
                                                                       "java.util",
                                                                       fc.getClasspath()),
                                                               compiledBytes);
                Class<?> clazz = loader.loadClass(fc.getClasspath());
                loadedClasses.put(fc.getClasspath(), clazz);
            } else {
                log.error("Java source compiled failed");
            }
        } catch (Exception ex) {
            log.error("compilerSource error", ex);
        }
    }

    @Override
    public Object call(String classpath, String methodName, Class<?>[] parameterTypes, Object[] args) {
        var executor = loadedClasses.get(classpath);
        if (executor == null) {
            throw new IllegalStateException("Class not loaded: " + classpath);
        }
        try {
            Method method = executor.getMethod(methodName, parameterTypes);
            return method.invoke(null, args);
        } catch (Exception ex) {
            log.error("call error", ex);
            throw new RuntimeException(ex);
        }
    }
}
