package com.guhecloud.cube.base.license.server.cli;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guhecloud.cube.base.license.server.cli.command.CreateLicenseCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.PrintWriter;

@Slf4j
@SpringBootApplication(scanBasePackages = { "com.guhecloud.cube.base.license.server.cli" },
                       exclude = { DataSourceAutoConfiguration.class }
)
public class LicenseClientMain implements CommandLineRunner {
    private static final String USAGE_HEAD = "USAGE: license-cli [options] <command>\n";

    private static final int DEFAULT_FAIL_EXIT_CODE = 1;

    private YAMLMapper yamlMapper = new YAMLMapper();

    public static void main(String[] args) {
        new SpringApplicationBuilder(LicenseClientMain.class).web(WebApplicationType.NONE).run(args);
    }

    private void showUsage() {
        HelpFormatter.builder().setPrintWriter(new PrintWriter(System.out)).get().printHelp(USAGE_HEAD,
                                                                                            CommandOptionParser.OPTIONS);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("License CLI Args: \"{}\"", String.join("\", \"", args));
        try {
            CommandOptionParser.CommandOption option = CommandOptionParser.parseFromCommandLine(args);
            yamlMapper.registerModule(new JavaTimeModule());
            log.info("Parsed Options:\n{}", yamlMapper.writeValueAsString(option));
            try {
                boolean success = CreateLicenseCommand.execute(option);
                log.info("create license result:{}", success);
            } catch (RuntimeException e) {
                log.error("Error execute, error: {}", e.getMessage());
                if (log.isDebugEnabled()) {
                    log.debug("", e);
                }
                System.exit(DEFAULT_FAIL_EXIT_CODE);
            }
        } catch (ParseException pe) {
            log.error("CLI option parse error: {}", pe.getMessage());
            System.exit(DEFAULT_FAIL_EXIT_CODE);
        } catch (Throwable e) {
            log.error("License CLI Terminate Abnormally: error message: {}",
                      e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            System.exit(DEFAULT_FAIL_EXIT_CODE);
        } finally {
            showUsage();
        }
    }
}
