package com.example.health_management.common.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
@Configuration
public class OpenAIClientConfig {

    @Value("${spring.ai.azure.openai.api-key}")
    private String azureOpenAiKey;

    @Value("${spring.ai.azure.openai.endpoint}")
    private String azureOpenAiEndpoint;

    @Value("${spring.ai.azure.openai.chat.options.deployment-name}")
    private String azureOpenAiModel;

    @Bean(name = "openAIClient")
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenAiKey))
                .endpoint(azureOpenAiEndpoint)
                .buildClient();
    }

    @Bean(name = "azureOpenAiChatOptions")
    public AzureOpenAiChatOptions azureOpenAiChatOptions() {
        AzureOpenAiChatOptions options = new AzureOpenAiChatOptions();
        options.setDeploymentName(azureOpenAiModel);
        options.setMaxTokens(50);
        options.setN(3);
        options.setTopP(1.0f);
        return options;
    }
}
