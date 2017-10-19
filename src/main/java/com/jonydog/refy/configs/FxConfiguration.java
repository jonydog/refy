package com.jonydog.refy.configs;

import com.jonydog.refy.util.StageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FxConfiguration {

    @Bean
    public StageManager stageManager() throws IOException {
        return StageManager.getInstance();
    }
}
