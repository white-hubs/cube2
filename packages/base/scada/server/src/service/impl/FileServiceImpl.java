package com.guhecloud.kaihe.scada.server.service.impl;

import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.service.FileService;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImpl implements FileService {

    @Value("${files.store.dir:/data}")
    private String fileStoreDir;

    @Override
    public String getRootDir() {
        return fileStoreDir;
    }

    @PostConstruct
    public void initDirs() {
        try {
            Files.createDirectories(Paths.get(fileStoreDir));
            Files.createDirectories(Paths.get(fileStoreDir + "/" + TEMP_DIAGRAMS_FOLDER));
            Files.createDirectories(Paths.get(fileStoreDir + "/" + DIAGRAMS_FOLDER));
            Files.createDirectories(Paths.get(fileStoreDir + "/" + FIGURES_FOLDER));
            Files.createDirectories(Paths.get(fileStoreDir + "/" + IMPORT_FOLDER));
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Override
    public void saveFile(String fileName, byte[] content, String... dir) {
        try {
            Files.createDirectories(Paths.get(fileStoreDir + "/" + this.getDir(dir)));
            Path path = Paths.get(fileStoreDir + "/" + this.getDir(dir) + "/" + fileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            Files.write(path, content);
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
    }

    @Override
    public byte[] getFile(String fileName, String... dir) {
        try {
            Path path = Paths.get(fileStoreDir + "/" + this.getDir(dir) + "/" + fileName);
            return Files.exists(path) ? Files.readAllBytes(path) : null;
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
    }

    @Override
    public void removeFile(String fileName, String... dir) {
        Path path = Paths.get(fileStoreDir + "/" + getDir(dir) + "/" + fileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
    }

    @Override
    public void removeDir(String... dir) {
        Path path = Paths.get(fileStoreDir + "/" + getDir(dir));
        foreachDir(path);
    }

    private void foreachDir(Path path) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path e : stream) {
                if (e.toFile().isDirectory()) {
                    foreachDir(e);
                }
                Files.deleteIfExists(e);
            }
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
    }

    private String getDir(String[] dirs) {
        return StringUtils.join("/", (Object[]) dirs);
    }

}
