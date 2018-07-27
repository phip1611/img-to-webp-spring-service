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

    private final Base64.Decoder base64Decoder;

    @Autowired
    private TestRestTemplate restTemplate;


    public ImgToWebPServiceApplicationTests() {
        this.base64Decoder = Base64.getDecoder();
    }

    @Test
    public void testImageConversion() {
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
            ResponseEntity<ImageDto> response = restTemplate.postForEntity("/convert", input, ImageDto.class);
            ImageDto dto = response.getBody();

            Assert.assertEquals(200, response.getStatusCodeValue());
            Assert.assertNotNull(dto);
            Assert.assertTrue(dto.isSuccess());
            Assert.assertNotNull(dto.getBase64String());
            // Die nächste Zeile deckt den fehler ab den ich hatte, dass ich das ursprungsbild im
            // original wieder rausgegeben habe!
            Assert.assertNotEquals(input.getBase64String(), dto.getBase64String());
            Assert.assertTrue(base64Decoder.decode(dto.getBase64String()).length > 10);
            Assert.assertTrue(response.getBody().isSuccess());
        });
    }

    @Test
    public void testImageConversionFailure() {
        ImageInput input = new ImageInput().setBase64String("izf8aafagz3ofjhaof").setFileExtension("jpg");
        ImageInput input2 = new ImageInput().setBase64String("izf8aafafgagofjhaof").setFileExtension("FOO");

        ResponseEntity<ImageDto> res1 = restTemplate.postForEntity("/convert", input, ImageDto.class);
        ResponseEntity<ImageDto> res2 = restTemplate.postForEntity("/convert", input2, ImageDto.class);

        ImageDto dto1 = res1.getBody();
        ImageDto dto2 = res2.getBody();

        Assert.assertEquals(200, res1.getStatusCodeValue());
        Assert.assertEquals(200, res2.getStatusCodeValue());
        Assert.assertNotNull(dto1);
        Assert.assertNotNull(dto2);
        Assert.assertTrue(!dto1.isSuccess());
        Assert.assertTrue(!dto2.isSuccess());
    }

    private String getFileEnding(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

}
