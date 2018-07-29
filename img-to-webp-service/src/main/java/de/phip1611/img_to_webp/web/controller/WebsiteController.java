package de.phip1611.img_to_webp.web.controller;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.api.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Controller
public class WebsiteController {

    private final ImageService imageService;

    @Autowired
    public WebsiteController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "")
    public String index(Model model) {
        model.addAttribute("page", "index");
        return "index";
    }

    @GetMapping(path = "upload")
    public String upload(Model model) {
        model.addAttribute("page", "upload");
        return "index";
    }

    @GetMapping(path = "impressum")
    public String impressum(Model model) {
        model.addAttribute("page", "impressum");
        return "index";
    }

    @GetMapping(path = "datenschutz")
    public String datenschutz(Model model) {
        model.addAttribute("page", "datenschutz");
        return "index";
    }

    @GetMapping(path = "about-me")
    public String aboutMe(Model model) {
        model.addAttribute("page", "about-me");
        return "index";
    }

    @PostMapping(path = "/upload")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("quality") int quality,
                                 // reqired = false, because checkboxes are not send when being not checked
                                 @RequestParam(value = "consent", required = false) boolean consent,
                                 HttpServletResponse response) throws IOException {
        if (!consent) {
            response.getOutputStream().write("Check consent checkmark.".getBytes());
            return;
        }

        response.setContentType("image/webp");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() + ".webp\"");

        String[] split = file.getContentType().split("/");
        String fileExt = split[split.length - 1];

        ImageInput input = new ImageInput();
        input.setQuality((byte)quality);
        input.setBase64String(Base64.getEncoder().encodeToString(file.getBytes()));
        input.setFileExtension(fileExt);

        ImageDto dto = this.imageService.convert(input);
        byte[] data = Base64.getDecoder().decode(dto.getBase64String());
        OutputStream os = response.getOutputStream();
        for (byte datum : data) {
            os.write(datum);
        }
        response.flushBuffer();
    }

}
