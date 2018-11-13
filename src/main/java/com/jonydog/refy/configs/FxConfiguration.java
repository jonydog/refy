package com.jonydog.refy.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonydog.refy.util.StageManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class FxConfiguration {

    @Getter
    @Value("${refy.working-mode}")
    private String workingMode;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public StageManager stageManager() throws IOException {
        return StageManager.getInstance(this.workingMode,this.resourceLoader);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }


}
