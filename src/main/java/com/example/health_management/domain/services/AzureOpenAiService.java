package com.example.health_management.domain.services;
import com.azure.ai.openai.OpenAIClient;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AzureOpenAiService {
    @Qualifier("azureOpenAiChatOptions")
    private final AzureOpenAiChatOptions options;
    @Qualifier("openAIClient")
    private final OpenAIClient client ;

    @Autowired
    public AzureOpenAiService(AzureOpenAiChatOptions options, OpenAIClient client ) {
        this.options = options;
        this.client = client;
    }

    public String diagnoseDiseases(int heart, String bloodPressure, int bodyTemperature, int respiratoryRate, int sp02) {
        final String promptText = """
                You are given health indicators such as heart rate, blood pressure, body temperature, respiratory rate, SpO2, age, weight, height, and other relevant data. Your task is to analyze these inputs and output a list of potential diagnoses (e.g., hypertension, arrhythmia, diabetes, cardiovascular diseases), each accompanied by a confidence score expressed as a percentage. The output should be based on pattern recognition from the input values, but it is important to note that the predictions are estimates and do not require 100% accuracy. 
                Example Input: Heart rate: 85 bpm
                               Blood pressure: 150/90 mmHg
                               Body temperature: 37°C
                Expected Output:Hypertension: 80% confidence
                                Arrhythmia: 65% confidence
                                Diabetes: 60% confidence (uncertain)
                Input:Heart rate: {heart} bpm
                      Blood pressure: {bloodPressure} mmHg
                      Body temperature: {bodyTemperature}°C
                      Respiratory Rate: {respiratoryRate} (breaths/min)
                      Sp02: {sp02} %
                """;

        final PromptTemplate promptTemplate = new PromptTemplate(promptText);
        promptTemplate.add("heart", heart);
        promptTemplate.add("bloodPressure", bloodPressure);
        promptTemplate.add("bodyTemperature", bodyTemperature);
        promptTemplate.add("respiratoryRate", respiratoryRate);
        promptTemplate.add("sp02", sp02);
        AzureOpenAiChatModel model = new AzureOpenAiChatModel(client, options);
        ChatResponse response = model.call(promptTemplate.create());
        return response.getResult().getOutput().getContent();
    }
}
