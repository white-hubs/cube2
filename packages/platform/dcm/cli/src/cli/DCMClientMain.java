/**
 * Declarative Configuration Management Client
 */

package com.guhecloud.platform.dcm.cli;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.dcm.auth.AuthTokenRunner;
import com.guhecloud.platform.dcm.command.AbstractCliCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
@SpringBootApplication(scanBasePackages = { "com.guhecloud.platform" })
public class DCMClientMain implements CommandLineRunner {
    private static final String USAGE_HEAD = "USAGE: dcm-cli [options] <command>\n";
    private static final String USAGE_COMMANDS = "Commands:\n"
                                                 + "  local_extract  patterns \tExtract resource from jar or \n"
                                                 + "  extract                 \tExtract builtin configuration from remote DCM server \n"
                                                 +
                                                 "  rt_extract [system] [[[tenant1] tenant2] ... tenantN] \tExtract runtime configuration from remote DCM server \n"
                                                 + "  diff [system] [tenants] \tdiff with remote DCM server \n"
                                                 + "  apply [system] [tenant] \tapply from remote DCM server \n"
                                                 + "  help                    \tshow this help message\n";

    private AuthTokenRunner runner;

    private YAMLMapper yamlMapper = new YAMLMapper();

    public static void main(String[] args) {
        SpringApplication.run(DCMClientMain.class, args);
    }

    private void showUsage() {
        HelpFormatter.builder().setPrintWriter(new PrintWriter(System.out)).get().printHelp(USAGE_HEAD,
                                                                                            CommandOptionParser.OPTIONS);
        System.out.print(USAGE_COMMANDS);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("DCM CLI Args: \"{}\"", String.join("\", \"", args));
        try {
            CommandOptionParser.CommandOption option = CommandOptionParser.parseFromCommandLine(args);
            log.info("Parsed Options:\n{}", yamlMapper.writeValueAsString(option));
            if (option.getCommands() != null && !option.getCommands().isEmpty()) {
                String command = option.getCommands().get(0);
                log.info("running command: {}, args: {}", command, option.getCommands().subList(1, option.getCommands().size()));
                switch (command) {
                    case "local_extract":
                    case "extract":
                    case "rt_extract":
                    case "diff":
                    case "apply":
                        try {
                            Objects.requireNonNull(AbstractCliCommand.createCommand(command, option)).execute();
                        } catch (RuntimeException e) {
                            log.error("Error execute {}, error: {}", command, e.getMessage());
                            log.debug("", e);
                        }
                        return;
                    case "help":
                        break;
                    default:
                        // leave to show usage.
                        log.info("unknown command {}", command);
                }
            } else {
                log.warn("no command specified.");
            }
            showUsage();
        } catch (ParseException pe) {
            log.info("CLI option parse error: {}", pe.getMessage());
            showUsage();
        } catch (Throwable e) {
            log.info("DCM CLI Terminate Abnormally: error message: {}",
                     e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }
}
