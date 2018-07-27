package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.util.ImageType;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static de.phip1611.img_to_webp.util.ImageType.getTypeByString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ImgToWebPServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    public ImgToWebPServiceApplicationTests() {
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

                    ImageType type = getTypeByString(getFileEnding(imgResource.getFilename()));
                    if (type == null) {
                        continue;
                    }
                    ImageInput input = new ImageInput()
                    .setFileExtension(getFileEnding(imgResource.getFile().toString()))
                    .setBase64String(Base64.getEncoder().encodeToString(bytes));

                    inputs.add(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputs.forEach(input -> {
            ResponseEntity<ImageDto> dto = restTemplate.postForEntity("/convert", input, ImageDto.class);
            Assert.assertEquals(200, dto.getStatusCodeValue());
            // Die nächste Zeile deckt den fehler ab den ich hatte, dass ich das ursprungsbild im
            // original wieder rausgegeben habe!
            Assert.assertNotEquals(input.getBase64String(), dto.getBody().getBase64String());
            Assert.assertTrue(dto.getBody().isSuccess());
        });
    }

    private String getFileEnding(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

}
