# Image to WebP

This project consists of a Java Library and a Spring REST-Service that uses the lib. You can use the lib standalone. The lib is a wrapper around the "cwebp"-executable (the official webp converter) with easy input and output interface and error/success feedback. You call the converter with a byte[] array and a working directory of your choice. The library creates the file out of the byte[]-array (which is for example a jpeg, png or tiff image, or even another webp image) and writes it to the given working directory. Then cwebp is executed in this directory with a chosen quality factor (1 - 100).

In additional this repository provides a REST-Service created with Java and Spring. The service takes images (jpeg, png, tiff, webp) encoded as base64 and responds with a converted webp build also base64 encoded. You need to install "cwebp" on the Host-System that is running this service. 

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

## build and run
- install `cwebp` on your machine, e.g. via https://developers.google.com/speed/webp/docs/precompiled
and make sure it's in your PATH variable
- `git clone`
- `mvn clean install`
- `cd target && java -jar ....jar` (starts by default on Port 8080; change for example with `-Dserver.port=1337` (or any other spring configuration method you like)
