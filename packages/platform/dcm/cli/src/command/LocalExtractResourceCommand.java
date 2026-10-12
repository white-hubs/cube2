package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.dcm.cli.CommandOptionParser;
import com.guhecloud.platform.dcm.support.BaseDirectResourceExtractor;
import com.guhecloud.platform.dcm.support.LibsModeJarResourceExtractor;
import com.guhecloud.platform.dcm.support.ResourceExtractor;
import com.guhecloud.platform.dcm.support.SpringBootFatJarResourceExtractor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Slf4j
class LocalExtractResourceCommand extends AbstractCliCommand implements ICliCommand {

    // default regex pattern for dcm resources
    public LocalExtractResourceCommand(CommandOptionParser.CommandOption option) {
        super("local-extract", option);
    }

    @Override
    public void execute() {
        String resourcePattern = StringUtils.isBlank(getOption().getResourcePattern()) ?
                                 DEFAULT_RESOURCE_PATTERN : getOption().getResourcePattern();
        String sourceFp = getOption().getFilePath();
        String fileMode = getOption().getFileMode();
        String outPath = getOption().getOutputPath();
        if (outPath == null || outPath.isEmpty()) {
            outPath = ".";
        }
        ResourceExtractor re = null;
        if (CommandOptionParser.FM_SBT_FAT.equalsIgnoreCase(fileMode)) {
            re = new SpringBootFatJarResourceExtractor(sourceFp, System.getProperty("java.io.tmpdir"));
        } else if (CommandOptionParser.FM_BASE_DIR.equalsIgnoreCase(fileMode)) {
            re = new BaseDirectResourceExtractor(sourceFp);
        } else {
            re = new LibsModeJarResourceExtractor(sourceFp, System.getProperty("java.io.tmpdir"));
        }
        try {
            Map<Resource, String> resources = re.extractResource(resourcePattern, true);
            Path targetPath = null;
            Path targetDir = Paths.get(outPath);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            for (Map.Entry<Resource, String> entry : resources.entrySet()) {
                try {
                    targetPath = Paths.get(outPath, entry.getValue());
                    if (!Files.exists(targetPath.getParent())) {
                        Files.createDirectories(targetPath.getParent());
                    }
                    Files.copy(entry.getKey().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    log.warn("copy resource failed: from: {}, to: {}, error: {} ", entry.getKey().getURI(), targetPath,
                             ioe.toString());
                }
            }
        } catch (IOException e) {
            log.error("extract resources failed: from {}, error: {}", sourceFp, e.toString());
        }
    }
}
