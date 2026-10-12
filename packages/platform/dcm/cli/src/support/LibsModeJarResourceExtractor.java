package com.guhecloud.platform.dcm.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class LibsModeJarResourceExtractor extends AbstractBaseResourceExtractor {

    private final String basePath;
    private final String outCachePath;

    public LibsModeJarResourceExtractor(String basePath, String outCachePath) {
        this.basePath = basePath;
        this.outCachePath = outCachePath;
    }

    @Override
    public Map<Resource, String> extractResource(String resourcePattern, boolean isRegex) {
        Path libPath = getLibPath();
        Path outputCache = Paths.get(this.outCachePath);
        Map<String, Path> extractedResourcesInOutputCache = new HashMap<>();
        Pattern resourcePatternRegEx = getRegexPattern(resourcePattern, isRegex);
        String resourcePathPrefix = extractLongestDeterministicPath(resourcePattern, isRegex);
        try (DirectoryStream<Path> libPathStream = Files.newDirectoryStream(libPath)) {
            for (Path libEntryPath : libPathStream) {
                if (libEntryPath.toString().endsWith(".jar")) {
                    processEmbeddedJar(libEntryPath, outputCache, extractedResourcesInOutputCache, resourcePatternRegEx, resourcePathPrefix);
                }
            }
        } catch (IOException ioe) {
            log.error("error enumerate directory", ioe);
        }
        return extractedResourcesInOutputCache.values().stream()
                .collect(Collectors.toMap(path -> new FileSystemResource(path), path -> pathUnder(path, outputCache), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    protected Path getLibPath() {
        return Paths.get(this.basePath);
    }

    /**
     * 将指定的file path作为jar包打开，并匹配其中的路径， 将匹配后的文件复制到指定输出 同时记录到资源名称到输出路径间的map.
     *
     * @param jarPath
     * @param outputDir
     * @param outPathMap
     * @param resourcePatternRegEx
     * @param resourcePathPrefix
     *
     * @throws IOException
     */
    private static void processEmbeddedJar(Path jarPath, Path outputDir, Map<String, Path> outPathMap, Pattern resourcePatternRegEx,
                                           String resourcePathPrefix
                                          ) throws IOException {
        log.debug("================== Process Jar Entry: {}, Pattern: {}, ResourcePathPrefix: {} =================",
                  jarPath, resourcePatternRegEx, resourcePathPrefix);
        Path tempJar;
        tempJar = Files.createTempFile("temp-", ".jar");
        try {
            Files.copy(jarPath, tempJar, StandardCopyOption.REPLACE_EXISTING);
            try (FileSystem jarFs = FileSystems.newFileSystem(tempJar, (ClassLoader) null)) {
                Files
                        .walk(jarFs.getPath(resourcePathPrefix))
                        .forEach(jarEntryPath -> copyMatchedEntryAndRecord(jarEntryPath, outputDir, outPathMap, resourcePatternRegEx));
            }
        } catch (IOException ioe) {
            log.debug("no entry found in jar.", jarPath);
        } finally {
            try {
                Files.delete(tempJar);
            } catch (IOException ioe) {
                log.warn("remove temp file {} failed, error: {}", tempJar, ioe.getMessage());
            }
        }
    }

    private static void copyMatchedEntryAndRecord(Path jarEntryPath, Path outputDir, Map<String, Path> outPathMap,
                                                  Pattern resourcePatternRegEx
                                                 ) {
        if (!resourcePatternRegEx.matcher(jarEntryPath.toString()).matches()) {
            return;
        }
        try {
            log.debug("copy {}", jarEntryPath);
            Path targetPath = outputDir.resolve(jarEntryPath.toString().substring(1));
            Files.createDirectories(targetPath.getParent());
            Files.copy(jarEntryPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            outPathMap.put(jarEntryPath.getFileName().toString(), targetPath);
        } catch (IOException e) {
            log.warn("Failed to copy {} to {}", jarEntryPath, outputDir, e);
        }
    }

    private static String pathUnder(Path path, Path root) {
        return root.relativize(path).toString();
    }

    public static void main(String args[]) {
        FileSystem fs = FileSystems.getDefault();

        System.out.println(pathUnder(fs.getPath("a", "b"), fs.getPath("a")));
    }
}
