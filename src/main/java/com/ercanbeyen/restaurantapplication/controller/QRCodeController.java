package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.service.QRCodeService;
import com.ercanbeyen.restaurantapplication.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/qr")
public class QRCodeController {
    private final QRCodeService qrCodeService;

    @GetMapping(value = "/generate/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCodeImage(@RequestParam("text") String request) {
        QRCodeUtil.validateQRContent(request);
        return ResponseEntity.ok(qrCodeService.generateQRCodeImage(request));
    }

    @PostMapping("/read/image")
    public ResponseEntity<String> readQRCodeImage(@RequestParam("file") MultipartFile request) {
        return ResponseEntity.ok(qrCodeService.readQRCodeImage(request));
    }

    @GetMapping(value = "/generate/base64")
    public ResponseEntity<Map<String, String>> generateQRCodeBase64(@RequestParam("text") String request) {
        QRCodeUtil.validateQRContent(request);
        return ResponseEntity.ok(qrCodeService.generateQRCodeBase64(request));
    }

    @PostMapping("/read/base64")
    public ResponseEntity<Map<String, String>> readQRCodeBase64(@RequestParam("text") String request) {
        return ResponseEntity.ok(qrCodeService.readQRCodeBase64(request));
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
}
