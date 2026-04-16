package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;
import java.util.List;

import static de.phip1611.img_to_webp.IntegrationTestUtils.getTestDataFiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
@DirtiesContext
public class ImgToWebPServiceIntegrationTest {

    private final Base64.Decoder base64Decoder;

    @Autowired
    private TestRestTemplate restTemplate;

    public ImgToWebPServiceIntegrationTest() {
        this.base64Decoder = Base64.getDecoder();
    }

    @Test
    public void testImageConversion() {
        List<ImageInput> inputs = getTestDataFiles();

        inputs.forEach(input -> {
            ResponseEntity<ImageDto> response = restTemplate.postForEntity("/convert", input, ImageDto.class);
            ImageDto dto = response.getBody();

            assertEquals(200, response.getStatusCode().value());
            assertNotNull(dto);
            assertTrue(dto.isSuccess());
            assertNotNull(dto.getBase64String());
            // Die nächste Zeile deckt den fehler ab den ich hatte, dass ich das ursprungsbild im
            // original wieder rausgegeben habe!
            assertNotEquals(input.getBase64String(), dto.getBase64String());
            assertTrue(base64Decoder.decode(dto.getBase64String()).length > 10);
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

        assertEquals(200, res1.getStatusCode().value());
        assertEquals(200, res2.getStatusCode().value());
        assertNotNull(dto1);
        assertNotNull(dto2);
        assertFalse(dto1.isSuccess());
        assertFalse(dto2.isSuccess());
    }
}
