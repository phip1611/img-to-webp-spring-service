package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.input.ImageInput;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class IntegrationTestUtils {

    public static List<ImageInput> getTestDataFiles() {
        List<ImageInput> inputs = new ArrayList<>();
        Resource[] imgResources;
        try {
            imgResources = new PathMatchingResourcePatternResolver(IntegrationTestUtils.class.getClassLoader())
                    .getResources("images/*");
            for (Resource imgResource : imgResources) {
                if (imgResource.isFile() && imgResource.isReadable()) {
                    byte[] bytes = IOUtils.toByteArray(imgResource.getInputStream());

                    ImageInput input = new ImageInput()
                            .setFileExtension(getFileEnding(imgResource.getFile().toString()))
                            .setBase64String(Base64.getEncoder().encodeToString(bytes))
                            .setQuality((byte) 1); // faster integration test

                    inputs.add(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputs;
    }



    private static String getFileEnding(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

}
