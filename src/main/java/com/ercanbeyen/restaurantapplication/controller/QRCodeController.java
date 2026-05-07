package com.ercanbeyen.restaurantapplication.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/qr")
public class QRCodeController {
    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;
    private static final String IMAGE_FORMAT = "PNG";

    @GetMapping(value = "/generate/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCodeImage(@RequestParam("text") String request) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(request, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        return ResponseEntity.ok(bufferedImage);
    }

    @PostMapping("/read/image")
    public ResponseEntity<String> readQRCodeImage(@RequestParam("file") MultipartFile request) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(request.getInputStream());
        return ResponseEntity.ok(decodeBufferedImage(bufferedImage));
    }

    @GetMapping(value = "/generate/base64")
    public ResponseEntity<Map<String, String>> generateQRCodeBase64(@RequestParam("text") String request) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(request, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedText = Base64.getEncoder().encodeToString(imageBytes);

        return ResponseEntity.ok(Map.of("image", encodedText));
    }

    @PostMapping("/read/base64")
    public ResponseEntity<Map<String, String>> readQRCodeBase64(@RequestParam("text") String request) throws IOException, NotFoundException {
        String base64 = request.contains(",") ? request.split(",")[1] : request; // remove "data:image/png;base64," section

        byte[] imageBytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

        String qrContent = decodeBufferedImage(bufferedImage);

        return ResponseEntity.ok(Map.of("content", qrContent));
    }

    @GetMapping("/qr-management")
    public String showQRCodeManagementPage() {
        return "qr-management";
    }

    @GetMapping("/generate-qr/image")
    public String showGenerateQRCodePage() {
        return "generate-qr";
    }

    @GetMapping("/read-qr/image")
    public String showReadQRCodePage() {
        return "read-qr";
    }

    private static String decodeBufferedImage(BufferedImage bufferedImage) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result =  new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }
}
