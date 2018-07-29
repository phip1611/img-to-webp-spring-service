package de.phip1611.img_to_webp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ImgToWebPServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgToWebPServiceApplication.class, args);
    }
}
