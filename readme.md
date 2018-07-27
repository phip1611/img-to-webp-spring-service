# Image to WebP Spring Service

Das ist ein mit Spring Boot ersteller REST-Service, der jpeg, png, tiff, und webp Bilder annimmt 
und anhand eines Qualitätsparameters (1 - 100) eine konvertierte WebP-Datei zurück gibt.

Das Kommunikationsformat der Bild-Dateien ist Base64.

Auf dem Host-System muss "cwebp", der WebP-Compiler, installiert sein.

## Schnittstelle
### Input
```
{
    "fileExtension": string; // bspw. "jpeg"
    "base64String": string;
    "quality": integer; // 1 - 100; Default-Wert ist 82
}
```

###Output
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
- `cd target && java -jar ....jar` (startet standardmäßig auf Port 8080; ändern bspw. über `-Dserver.port=1337`)