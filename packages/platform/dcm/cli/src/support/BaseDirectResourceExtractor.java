package com.guhecloud.platform.dcm.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 基于一个BaseDir, 将其视同一个文件系统，进行枚举
 */
@Slf4j
public class BaseDirectResourceExtractor extends AbstractBaseResourceExtractor {
    private final String baseDir;

    public BaseDirectResourceExtractor(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public Map<Resource, String> extractResource(String resourcePattern, boolean isRegex) throws IOException {
        List<Path> paths = new ArrayList<>();
        Path basePath = Paths.get(baseDir);
        String pathSeparator = Pattern.quote(basePath.getFileSystem().getSeparator());
        Pattern resourcePatternRegex = getRegexPattern(resourcePattern, isRegex);
        Map<Resource, String> resourcePathMap = new LinkedHashMap<>();
        Files.walk(basePath).forEach(path -> {
            String absPathUnderBase =
                    "/" + Arrays.stream(basePath.relativize(path).toString().split(pathSeparator)).collect(Collectors.joining("/"));
            if (isEntryMatch(absPathUnderBase, resourcePatternRegex)) {
                resourcePathMap.put(new PathResource(Paths.get(path.toUri())), absPathUnderBase);
            }
        });
        return resourcePathMap;
    }

    private boolean isEntryMatch(String absPathUnderBase, Pattern resourcePatternRegex) {
        return resourcePatternRegex.matcher(absPathUnderBase).find();
    }

    public static void main(String[] args) throws IOException {
        Map<Resource, String> resources = new BaseDirectResourceExtractor("output")
                .extractResource("/dcm/dcm-declaration-*.yml", false);
        resources.entrySet().stream().forEach(x -> log.info("{}: {}", x.getKey(), x.getValue()));
    }
}
