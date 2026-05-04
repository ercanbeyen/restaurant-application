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
import java.io.IOException;

@Controller
@RequestMapping("/qr")
public class QRCodeController {
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCode(@RequestParam("text") String request) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(request, BarcodeFormat.QR_CODE, 250, 250);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        return ResponseEntity.ok(bufferedImage);
    }

    @PostMapping("/read")
    public ResponseEntity<String> readQRCode(@RequestParam("file") MultipartFile request) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(request.getInputStream());
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        Result result = new MultiFormatReader().decode(binaryBitmap);

        return ResponseEntity.ok(result.getText());
    }

    @GetMapping("/qr-management")
    public String showQRManagementPage() {
        return "qr-management";
    }

    @GetMapping("/generate-qr")
    public String showGenerateQRCodePage() {
        return "generate-qr";
    }

    @GetMapping("/read-qr")
    public String showReadQRCodePage() {
        return "read-qr";
    }
}
