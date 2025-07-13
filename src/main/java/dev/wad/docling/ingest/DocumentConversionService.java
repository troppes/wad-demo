package dev.wad.docling.ingest;


import dev.wad.docling.client.DocumentConversionClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

@ApplicationScoped
public class DocumentConversionService {

    private static final Logger LOG = Logger.getLogger(DocumentConversionService.class);

    @Inject
    @RestClient
    DocumentConversionClient conversionClient;

//    void onStart(@Observes StartupEvent ev) {
//        LOG.info("Processing files on startup");
//        processAllFiles();
//    }

    public void processAllFiles() {
        Path ingestDir = Paths.get("src/main/resources/docling-ingest");

        if (!Files.exists(ingestDir)) {
            LOG.warn("Files directory does not exist: " + ingestDir);
            return;
        }

        try (Stream<Path> files = Files.walk(ingestDir)) {
            files.filter(Files::isRegularFile)
                    .forEach(this::processFile);
        } catch (IOException e) {
            LOG.error("Error processing files", e);
        }
    }

    private void processFile(Path filePath) {
        try {
            File file = filePath.toFile();
            convertDocument(file);
        } catch (IOException e) {
            LOG.errorf("Error processing file %s: %s", filePath, e.getMessage());
        }
    }

    public void convertDocument(File file) throws IOException {
        LOG.infof("Converting: %s", file.getName());

        // Read file and encode to base64
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String base64Content = Base64.getEncoder().encodeToString(fileContent);

        // Create JSON payload manually or use a simple object
        String jsonPayload = createJsonPayload(base64Content, file.getName());

        try {
            String response = conversionClient.convertDocument(jsonPayload);
            LOG.infof("Conversion completed: %s", response);
        } catch (Exception e) {
            LOG.errorf("Conversion failed for %s: %s", file.getName(), e.getMessage());
        }
    }

    private String createJsonPayload(String base64Content, String filename) {
        return String.format("""
        {
          "options": {
            "to_formats": ["md"],
            "do_ocr": true,
            "do_picture_description": true,
            "picture_description_local": {
              "repo_id": "ibm-granite/granite-vision-3.2-2b",
              "generation_config": {
                "do_sample": false,
                "max_new_tokens": 200
              },
              "prompt": "Describe this image in a few sentences."
            }
          },
          "file_sources": [
            {
              "base64_string": "%s",
              "filename": "%s"
            }
          ]
        }
        """, base64Content, filename);
    }

}
