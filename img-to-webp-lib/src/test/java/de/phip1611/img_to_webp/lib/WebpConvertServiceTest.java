package de.phip1611.img_to_webp.lib;

import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
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

    @Before
    public void setUp() {
        this.service = new WebpConvertServiceImpl();
    }

    @Test
    public void testImageInputbuilder() {
        WebpConvertInput.Builder builder = new WebpConvertInput.Builder();
        builder.setData(null);
        builder.setFileExt("jpg");
        //builder.setQuality(80); // there is already a defautl value
        Assert.assertNull(builder.build());

        builder.setData(new byte[MIN_FILE_SIZE - 1]);
        Assert.assertNull(builder.build());

        builder.setData(new byte[MIN_FILE_SIZE]);
        Assert.assertNotNull(builder.build());

        builder.setQuality(-1);
        Assert.assertNull(builder.build());

        builder.setQuality(0);
        Assert.assertNull(builder.build());

        builder.setQuality(1);
        Assert.assertNotNull(builder.build());

        builder.setQuality(100);
        Assert.assertNotNull(builder.build());

        builder.setQuality(101);
        Assert.assertNull(builder.build());

        builder.setQuality(80);  // reset to valid value
        builder.setFileExt("jpg");
        Assert.assertNotNull(builder.build());

        builder.setFileExt(null);
        Assert.assertNull(builder.build());

        builder.setFileExt("");
        Assert.assertNull(builder.build());

        builder.setFileExt("a");
        Assert.assertNull(builder.build());

        builder.setFileExt("JPG");
        Assert.assertNotNull(builder.build());

        builder.setFileExt("JpG");
        Assert.assertNotNull(builder.build());

        builder.setFileExt("afjiaup89f");
        Assert.assertNull(builder.build());

        builder.setFileExt("afjiaup89f");
        Assert.assertNull(builder.build());
    }

    @Test
    public void testWebpConversion() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File sourceFile = new File(classLoader.getResource("images/testimg.jpg").getFile());
        File targetReferenceFile = new File(classLoader.getResource("images/testimg-out-reference.webp").getFile());
        byte[] referenceBytes = IOUtils.toByteArray(new FileInputStream(targetReferenceFile));

        if (!sourceFile.isFile() || !targetReferenceFile.isFile()) {
            throw new IllegalStateException("Can't locate test resource!");
        }

        byte[] data = IOUtils.toByteArray(new FileInputStream(sourceFile));

        WebpConvertInput.Builder builder = new WebpConvertInput.Builder()
                .setData(data)
                .setQuality(86)
                .setFileExt("jpg");
        WebpConvertInput input = builder.build();
        Assert.assertNotNull(input);

        WebpConvertOutput output = this.service.convert(input, this.getTmpDir());

        Assert.assertTrue(output.isSuccess());
        Assert.assertArrayEquals(referenceBytes, output.getData());
    }

    private File getTmpDir() {
        File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "img-to-webp-lib-junit-test");
        dir.mkdir();
        return dir;
    }
}
