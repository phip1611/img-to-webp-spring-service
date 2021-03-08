package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.List;

import static de.phip1611.img_to_webp.IntegrationTestUtils.getTestDataFiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
public class ImgToWebPServiceApplicationTests {

    private final Base64.Decoder base64Decoder;

    @Autowired
    private TestRestTemplate restTemplate;

    public ImgToWebPServiceApplicationTests() {
        this.base64Decoder = Base64.getDecoder();
    }

    @Test
    public void testImageConversion() {
        List<ImageInput> inputs = getTestDataFiles();

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
        Assert.assertFalse(dto1.isSuccess());
        Assert.assertFalse(dto2.isSuccess());
    }
}
