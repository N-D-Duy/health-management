package com.example.health_management.domain.services.exporters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
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

    public ByteArrayResource generatePdfFromTemplate(String templateName, Map<String, Object> data, String fileName) {
        try {
            Context context = new Context();
            context.setVariables(data);
            String htmlContent = templateEngine.process(templateName, context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            String fontPath = "/fonts/DejaVuSans.ttf";
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream != null) {
                    renderer.getFontResolver().addFont(
                        Objects.requireNonNull(getClass().getResource(fontPath)).toExternalForm(),
                        true
                    );
                }
            }

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return new ByteArrayResource(outputStream.toByteArray()){
                @Override
                public String getFilename() {
                    return fileName;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
