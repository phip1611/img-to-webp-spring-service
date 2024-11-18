# Image to WebP

This project consists of a Java library for images (jpeg, png, tiff, webp) to WebP conversion
and a Spring Service. The Spring service offers both, a REST API and a traditional web
interface to the library.

You can use the lib standalone. The lib is a wrapper around the "cwebp"-executable (the official webp
converter) with easy input and output interface and error/success feedback. You call the converter
with a byte[] array and a working directory of your choice. The library creates the file out of the
byte[]-array (which is for example a jpeg, png or tiff image, or even another webp image) and writes
it to the given working directory. Then cwebp is executed in this directory with a chosen quality factor
(1 - 100).

The website and the REST API can be found here: https://webp.phip1611.dev

## REST-Interface

### Input: `POST-Request` to `/convert`
```
{
    "fileExtension": string; // e.g. "jpeg"
    "base64String": string;
    [optional] "quality": integer; // 1 - 100; Default-Value is 82
}
```

### Output: `Response` from `/convert`
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

## Build and Run

### Minimum Supported Java Version

The minimum supported Java version to build and run the project is Java 17.
<!-- Spring Boot requests that version. -->

### Nix

To build the project via the Nix flake:

- Spring Service Jar: \
  `$ nix build .#\jar`
- Spring Service run script (with runtime dependencies): \
  `$ nix build .#\serviceScript`
  - as binary package: \
    `$ nix build .#\serviceScriptBin` or
    `$ nix run .#\serviceScriptBin`
- Docker Container: \
  `$ nix build .#\dockerImage`

### Regular

- install `cwebp` on your machine, e.g. via https://developers.google.com/speed/webp/docs/precompiled
   and make sure it's in your PATH variable, or `$ sudo apt install webp`
- `mvn clean install`
- `cd target && java -jar ....jar` starts by default on Port 8080; change for
  example with `-Dserver.port=1337` (or any other spring configuration method
  you like)
