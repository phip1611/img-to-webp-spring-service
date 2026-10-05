package de.phip1611.img_to_webp.service.impl;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public class ImageServiceImplTest {

    private final WebpConvertService webpConvertService = mock(WebpConvertService.class);

    private final ImageServiceImpl service = new ImageServiceImpl(this.webpConvertService);

    @Test
    public void testMalformedBase64IsRejected() {
        ImageInput input = new ImageInput()
                .setFileExtension("jpg")
                .setBase64String("not-base64!");

        ImageDto dto = this.service.convert(input);

        assertFalse(dto.isSuccess());
        verifyNoInteractions(this.webpConvertService);
    }
}
