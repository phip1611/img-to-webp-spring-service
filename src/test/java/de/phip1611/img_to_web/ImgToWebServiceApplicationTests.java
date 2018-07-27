package de.phip1611.img_to_web;

import de.phip1611.img_to_web.input.ImageInput;
import de.phip1611.img_to_web.service.api.ImageService;
import de.phip1611.img_to_web.util.ImageType;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static de.phip1611.img_to_web.util.ImageType.getTypeByString;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImgToWebServiceApplicationTests {

    @Autowired
    private ImageService imageService;


    public ImgToWebServiceApplicationTests() {
    }

    @Test
    public void imageServiceTest() {
        List<ImageInput> inputs = new ArrayList<>();
        Resource[] imgResources;
        try {
             imgResources = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
                    .getResources("images/*");
            for (Resource imgResource : imgResources) {
                if (imgResource.isFile() && imgResource.isReadable()) {
                    byte[] bytes = IOUtils.toByteArray(imgResource.getInputStream());

                    Optional<ImageType> type = getTypeByString(getFileEnding(imgResource.getFilename()));
                    if (!type.isPresent()) {
                        continue;
                    }
                    ImageInput input = new ImageInput()
                    .setImageType(type.get())
                    .setImageBase64(Base64.getEncoder().encodeToString(bytes));

                    inputs.add(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(inputs);
    }

    private String getFileEnding(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

}
