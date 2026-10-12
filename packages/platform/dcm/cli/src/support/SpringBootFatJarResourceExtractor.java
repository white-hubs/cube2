package com.guhecloud.platform.dcm.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;

@Slf4j
public class SpringBootFatJarResourceExtractor extends LibsModeJarResourceExtractor {

    private final String basePath;

    public SpringBootFatJarResourceExtractor(String basePath, String outCachePath) {
        super(basePath, outCachePath);
        this.basePath = basePath;
    }

    @Override
    protected Path getLibPath() {
        Path basePath = Paths.get(this.basePath);
        Path libPath;
        try {
            FileSystem manager = FileSystems.newFileSystem(basePath, (ClassLoader) null);
            libPath = manager.getPath("BOOT-INF", "lib");
        } catch (ProviderNotFoundException e) {
            try {
                libPath = basePath.resolve("BOOT-INF/lib/");
            } catch (InvalidPathException invalidPathException) {
                libPath = basePath;
            }
        } catch (IOException ioException) {
            log.error("IOException when access {}, error: {}", this.basePath, ioException.getMessage());
            throw new RuntimeException("IOException during access to libpath", ioException);
        }
        log.warn("using library path: {}", libPath);

        return libPath;
    }

    public static void main(String[] args) {
        SpringBootFatJarResourceExtractor extractor = new SpringBootFatJarResourceExtractor("E:\\sbfat", "output");
        Map<Resource, String> resources = extractor.extractResource("^/dcm/dcm-declaration-.*\\.ya?ml$", true);
        for (Resource resource : resources.keySet()) {
            try {
                log.info("Resource: {}", resource.getURI());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
