/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.web;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.lib.service.data.WebpConvertInput;
import de.phip1611.img_to_webp.service.api.ImageService;
import de.phip1611.img_to_webp.service.api.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class WebsiteController {

    private final ImageService imageService;

    private final RateLimitService rateLimitService;

    @Autowired
    public WebsiteController(ImageService imageService,
                             RateLimitService rateLimitService) {
        this.imageService = imageService;
        this.rateLimitService = rateLimitService;
    }

    @GetMapping(path = "")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("page", "index");
        return "index";
    }

    @GetMapping(path = "upload")
    public String upload(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("page", "upload");
        return "index";
    }

    @GetMapping(path = "legal-privacy")
    public String legal(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("page", "legal-privacy");
        return "index";
    }

    @GetMapping(path = "rest-api")
    public String restApi(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("page", "rest-api");
        return "index";
    }

    @GetMapping(path = "about-me")
    public String aboutMe(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("page", "about-me");
        return "index";
    }

    @PostMapping(path = "/upload")
    public void handleFileUpload(@RequestParam("file")
                                 MultipartFile file,
                                 @RequestParam("quality")
                                 int quality,
                                 // reqired = false, because checkboxes are not send when being not checked
                                 @RequestParam(value = "consent", required = false)
                                 boolean consent,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        if (!consent) {
            writeBadRequest(response, "Check consent checkmark!");
            return;
        }
        if (file.isEmpty()) {
            writeBadRequest(response, "No file specified!");
            return;
        }
        if (quality < 1 || quality > 100) {
            writeBadRequest(response, "Invalid quality!");
            return;
        }

        this.rateLimitService.assertRequest(request);

        if (file.getContentType() == null) {
            writeBadRequest(response, "Invalid Content Type!");
            return;
        }
        String[] split = file.getContentType().split("/");
        String fileExt = split[split.length - 1];
        if (!WebpConvertInput.AllowedFileType.isAllowed(fileExt)) {
            writeBadRequest(response, "Unsupported image type!");
            return;
        }

        ImageInput input = new ImageInput();
        input.setQuality((byte)quality);
        input.setBase64String(Base64.getEncoder().encodeToString(file.getBytes()));
        input.setFileExtension(fileExt);

        ImageDto dto = this.imageService.convert(input);
        if (!dto.isSuccess()) {
            writeBadRequest(response, "Image conversion failed!");
            return;
        }
        byte[] data = Base64.getDecoder().decode(dto.getBase64String());
        response.setContentType(MediaType.valueOf("image/webp").toString());
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                        .filename(safeDownloadFileName(file.getOriginalFilename()))
                        .build()
                        .toString()
        );
        OutputStream os = response.getOutputStream();
        os.write(data);
        response.flushBuffer();
    }

    private void writeBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
    }

    private String safeDownloadFileName(String originalFilename) {
        String fileName = originalFilename == null ? "" : originalFilename.replace('\\', '/');
        int lastPathSeparator = fileName.lastIndexOf('/');
        if (lastPathSeparator >= 0) {
            fileName = fileName.substring(lastPathSeparator + 1);
        }
        fileName = fileName.replaceAll("[^A-Za-z0-9._-]", "_");
        if (fileName.isBlank() || ".".equals(fileName) || "..".equals(fileName)) {
            fileName = "converted";
        }
        if (fileName.length() > 100) {
            fileName = fileName.substring(0, 100);
        }
        return fileName + ".webp";
    }

    /*@ExceptionHandler(RateLimitException.class)
    public String databaseError() {
        return "error/429.html";
    }*/

}
