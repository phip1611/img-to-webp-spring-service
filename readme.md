# Image to WebP Spring Service

This is a REST-Service created with Java and Spring. The service takes images (jpeg, png, tiff, webp) and converts them to webp with a desired quality parameter (1-100). The coding for the communication of the images is base64.

You need to install "cwebp" on the Host-System that is running this service. 

## Build
### Master
[![Build Status](https://travis-ci.com/phip1611/img-to-webp-spring-service.svg?branch=master)](https://travis-ci.com/phip1611/img-to-webp-spring-service)
### Dev
[![Build Status](https://travis-ci.com/phip1611/img-to-webp-spring-service.svg?branch=dev)](https://travis-ci.com/phip1611/img-to-webp-spring-service)

## Schnittstelle
### Input
```
{
    "fileExtension": string; // e.g. "jpeg"
    "base64String": string;
    "quality": integer; // 1 - 100; Default-Value is 82
}
```

### Output
```
{
    "success": boolean;
    "size": number;
    "oldSize": number;
    "savingsInPercent": number;
    "base64String": string;
    "quality": integer;
}
```

## Bauen und starten
- `git clone`
- `mvn clean install`
- `cd target && java -jar ....jar` (starts by default on Port 8080; change for example with `-Dserver.port=1337` (or any other spring configuration method you like)
