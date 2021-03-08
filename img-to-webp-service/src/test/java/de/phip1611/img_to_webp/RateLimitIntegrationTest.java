package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.config.RateLimitConfiguration;
import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"rate-limit-test", "test"})
@DirtiesContext
public class RateLimitIntegrationTest {

    private static Logger LOGGER = LoggerFactory.getLogger(RateLimitIntegrationTest.class);

    @Autowired
    private RateLimitConfiguration rateLimitConfiguration;

    @Autowired
    private TestRestTemplate restTemplate;

    public RateLimitIntegrationTest() {
    }

    @Test
    public void testRateLimit() throws InterruptedException {
        var inputs = getTestDataFiles();

        for (int i = 0; i < rateLimitConfiguration.getRateLimitRequestsPerInterval(); i++) {
            ResponseEntity<ImageDto> response = restTemplate.postForEntity("/convert", inputs.get(0), ImageDto.class);
            Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
            Assert.assertNotNull(response.getBody());
            Assert.assertTrue(response.getBody().isSuccess());
        }

        ResponseEntity<ImageDto> response = restTemplate.postForEntity("/convert", inputs.get(0), ImageDto.class);
        Assert.assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());

        // sleep until scheduling job is done
        LOGGER.debug("Sleep until scheduling job is done...");
        // + 3 for more safety
        Thread.sleep(1000L * (3 + rateLimitConfiguration.getRateLimitIntervalSeconds()));

        response = restTemplate.postForEntity("/convert", inputs.get(0), ImageDto.class);
        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().isSuccess());
    }

    private String getFileEnding(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

    private List<ImageInput> getTestDataFiles() {
        List<ImageInput> inputs = new ArrayList<>();
        Resource[] imgResources;
        try {
            imgResources = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
                    .getResources("images/*");
            for (Resource imgResource : imgResources) {
                if (imgResource.isFile() && imgResource.isReadable()) {
                    byte[] bytes = IOUtils.toByteArray(imgResource.getInputStream());

                    ImageInput input = new ImageInput()
                            .setFileExtension(getFileEnding(imgResource.getFile().toString()))
                            .setBase64String(Base64.getEncoder().encodeToString(bytes));

                    inputs.add(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputs;
    }

}
