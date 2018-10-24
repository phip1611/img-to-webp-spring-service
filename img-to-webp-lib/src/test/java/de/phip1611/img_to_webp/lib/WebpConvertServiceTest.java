package de.phip1611.img_to_webp.lib;

import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
import de.phip1611.img_to_webp.lib.service.api.metadata.ImmutableWebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertOutput;
import de.phip1611.img_to_webp.lib.service.impl.WebpConvertServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertInput.MIN_FILE_SIZE;

public class WebpConvertServiceTest {

    private WebpConvertService service;

    private ImmutableWebpConvertInput.Builder builder;

    private File jpegTestFile = null;

    @Before
    public void setUp() {
        this.service      = new WebpConvertServiceImpl();
        this.jpegTestFile = this.getJpegTestFile();
        this.builder      = ImmutableWebpConvertInput.builder();
    }

    @Test
    public void testNullPointerInputs() {
        try {
            this.builder.fileExt(null);
            Assert.fail();
        } catch (NullPointerException ignored) {}

        try {
            this.builder.data(null);
            Assert.fail();
        } catch (NullPointerException ignored) {}

        this.builder.data(); // empty array, not a NPE

        try {
            this.builder.uuid(null);
            Assert.fail();
        } catch (NullPointerException ignored) {}

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
        Assert.assertNotNull(this.builder.build());

        this.builder.quality(-1);
        try {
            Assert.assertNull(this.builder.build());
            Assert.fail();
        } catch (IllegalStateException ignored) {}

        this.builder.quality(0);
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.quality(1);
        Assert.assertNotNull(this.builder.build());

        this.builder.quality(100);
        Assert.assertNotNull(this.builder.build());

        this.builder.quality(101);
        this.buildAndCatchIllegalInputExceptionHelper();

        this.builder.quality(80);  // reset to valid value
        this.builder.fileExt("jpg");
        Assert.assertNotNull(this.builder.build());

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

        Assert.assertNotNull(this.builder.build());

        this.builder.fileExt("JPG");
        Assert.assertNotNull(this.builder.build());

        this.builder.fileExt("JpG");
        Assert.assertNotNull(this.builder.build());

        this.builder.fileExt("tiFF");
        Assert.assertNotNull(this.builder.build());

        this.builder.fileExt("PNG");
        Assert.assertNotNull(this.builder.build());
    }

    private void buildAndCatchIllegalInputExceptionHelper() {
        try {
            Assert.assertNull(builder.build());
            Assert.fail();
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void testWebpConversion() throws IOException {
        byte[] data = IOUtils.toByteArray(new FileInputStream(this.jpegTestFile));
        this.builder.data(data).quality(86).fileExt("jpg");
        WebpConvertInput input = this.builder.build();
        Assert.assertNotNull(input); // actually I'm testing immutables lib here!

        WebpConvertOutput output = this.service.convert(input, this.getTmpDir());

        Assert.assertTrue(output.isSuccess());
    }

    @Test
    public void testWebpConversionWithDefaultQuality() throws IOException {
        byte[] data = IOUtils.toByteArray(new FileInputStream(this.jpegTestFile));

        this.builder.data(data).fileExt("jpg");
        WebpConvertInput input = this.builder.build();
        Assert.assertNotNull(input); // actually I'm testing immutables lib here!
        Assert.assertEquals(WebpConvertInput.DEFAULT_QUALITY, input.getQuality());

        WebpConvertOutput output = this.service.convert(input, this.getTmpDir());

        Assert.assertTrue(output.isSuccess());
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
