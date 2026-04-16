package de.phip1611.img_to_webp.lib;

import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
import de.phip1611.img_to_webp.lib.service.data.ImmutableWebpConvertInput;
import de.phip1611.img_to_webp.lib.service.data.WebpConvertInput;
import de.phip1611.img_to_webp.lib.service.data.WebpConvertOutput;
import de.phip1611.img_to_webp.lib.service.impl.WebpConvertServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static de.phip1611.img_to_webp.lib.service.data.WebpConvertInput.MIN_FILE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebpConvertServiceTest {

    private WebpConvertService service;

    private ImmutableWebpConvertInput.Builder builder;

    private File jpegTestFile = null;

    @BeforeEach
    public void setUp() {
        this.service      = new WebpConvertServiceImpl();
        this.jpegTestFile = this.getJpegTestFile();
        this.builder      = ImmutableWebpConvertInput.builder();
    }

    @Test
    public void testNullPointerInputs() {
        assertThrows(NullPointerException.class, () -> this.builder.fileExt(null));
        assertThrows(NullPointerException.class, () -> this.builder.data(null));

        this.builder.data(); // empty array, not a NPE

        assertThrows(NullPointerException.class, () -> this.builder.uuid(null));

    }

    @Test
    public void testWrongInput() {
        this.builder.fileExt("jpg");
        //this.builder.data(null); Nonsense, immutables builder throws a NPE here!
        //this.buildAndCatchIllegalInputExceptionHelper();


        this.builder.data();
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.data(new byte[MIN_FILE_SIZE - 1]);
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.data(new byte[MIN_FILE_SIZE]);
        assertNotNull(this.builder.build());

        this.builder.quality(-1);
        assertThrows(IllegalStateException.class, () -> this.builder.build());

        this.builder.quality(0);
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.quality(1);
        assertNotNull(this.builder.build());

        this.builder.quality(100);
        assertNotNull(this.builder.build());

        this.builder.quality(101);
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.quality(80);  // reset to valid value
        this.builder.fileExt("jpg");
        assertNotNull(this.builder.build());

        this.builder.fileExt("");
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.fileExt("a");
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.fileExt("afjiaup89f");
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.fileExt("afjiaup89f");
        this.buildAndCatchIllegalInputExceptionHelper();
    }

    @Test
    public void testCorrectFileExtString() {
        // set up other data properly
        this.builder.data(new byte[MIN_FILE_SIZE]);

        this.builder.fileExt("jpg");

        assertNotNull(this.builder.build());

        this.builder.fileExt("JPG");
        assertNotNull(this.builder.build());

        this.builder.fileExt("JpG");
        assertNotNull(this.builder.build());

        this.builder.fileExt("tiFF");
        assertNotNull(this.builder.build());

        this.builder.fileExt("PNG");
        assertNotNull(this.builder.build());
    }

    private void buildAndCatchIllegalInputExceptionHelper() {
        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    public void testWebpConversion() throws IOException {
        byte[] data = IOUtils.toByteArray(new FileInputStream(this.jpegTestFile));
        this.builder.data(data).quality(86).fileExt("jpg");
        WebpConvertInput input = this.builder.build();
        assertNotNull(input); // actually I'm testing immutables lib here!

        WebpConvertOutput output = this.service.convert(input, this.getTmpDir());

        assertTrue(output.isSuccess());
    }

    @Test
    public void testWebpConversionWithDefaultQuality() throws IOException {
        byte[] data = IOUtils.toByteArray(new FileInputStream(this.jpegTestFile));

        this.builder.data(data).fileExt("jpg");
        WebpConvertInput input = this.builder.build();
        assertNotNull(input); // actually I'm testing immutables lib here!
        assertEquals(WebpConvertInput.DEFAULT_QUALITY, input.getQuality());

        WebpConvertOutput output = this.service.convert(input, this.getTmpDir());

        assertTrue(output.isSuccess());
    }

    /**
     * Returns the JPEG Test File.
     *
     * @return JPEG Test File
     */
    private File getJpegTestFile() {
        return new File(getClass().getClassLoader().getResource("images/testimg.jpg").getFile());
    }

    private File getTmpDir() {
        File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "img-to-webp-lib-junit-test");
        // TODO make a check if it is already there or if mkdir worked?!
        dir.mkdir();
        return dir;
    }
}
