package com.example.health_management.domain.services.exporters;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PDFExporter {
    private final SpringTemplateEngine templateEngine;

    public PDFExporter() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/pdf/");

        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    public ByteArrayResource generatePdfFromTemplate(String templateName, Map<String, Object> data) {
        try {
            Context context = new Context();
            context.setVariables(data);
            String htmlContent = templateEngine.process(templateName, context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}

