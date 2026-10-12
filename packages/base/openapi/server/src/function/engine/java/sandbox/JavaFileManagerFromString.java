package com.guhecloud.cube.base.openapi.server.function.engine.java.sandbox;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class JavaFileManagerFromString extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, byte[]> compiledBytes;

    /**
     * Creates a new instance of {@code ForwardingJavaFileManager}.
     *
     * @param fileManager delegate to this file manager
     */
    public JavaFileManagerFromString(JavaFileManager fileManager, Map<String, byte[]> compiledBytes) {
        super(fileManager);
        this.compiledBytes = compiledBytes;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
                                               FileObject sibling
                                              ) {
        return new SimpleJavaFileObject(URI.create("string:///" + className + kind.extension), kind) {
            @Override
            public OutputStream openOutputStream() {
                return new ByteArrayOutputStream() {
                    public void close() throws IOException {
                        compiledBytes.put(className, this.toByteArray());
                        super.close();
                    }
                };
            }
        };
    }
}
