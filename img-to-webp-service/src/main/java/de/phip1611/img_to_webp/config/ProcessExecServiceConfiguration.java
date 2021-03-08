package de.phip1611.img_to_webp.config;

import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.lib.service.impl.ProcessExecServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessExecServiceConfiguration {

    @Bean
    public ProcessExecService getProcessExecService() {
        return new ProcessExecServiceImpl();
    }
}
