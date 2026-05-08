package com.ercanbeyen.restaurantapplication.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface QRCodeService {
    BufferedImage generateQRCodeImage(String request);
    String readQRCodeImage(MultipartFile request);
    Map<String, String> generateQRCodeBase64(String request);
    Map<String, String> readQRCodeBase64(String request);
}
