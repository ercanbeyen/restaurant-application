package com.ercanbeyen.restaurantapplication.service.impl;

import com.ercanbeyen.restaurantapplication.exception.BadRequestException;
import com.ercanbeyen.restaurantapplication.exception.InternalServerErrorException;
import com.ercanbeyen.restaurantapplication.service.QRCodeService;
import com.ercanbeyen.restaurantapplication.util.QRCodeUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class QRCodeServiceImpl implements QRCodeService {
    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;
    private static final String IMAGE_FORMAT = MediaType.IMAGE_PNG_VALUE.split("/")[1];

    @Override
    public BufferedImage generateQRCodeImage(String request) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(request, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

    @Override
    public String readQRCodeImage(MultipartFile request) {
        try {
            BufferedImage bufferedImage = ImageIO.read(request.getInputStream());
            String decodedText = decodeBufferedImage(bufferedImage);

            QRCodeUtil.validateDecodedText(decodedText);
            QRCodeUtil.validateQRContent(decodedText);

            return decodedText;
        } catch (IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        } catch (NotFoundException _) {
            throw new BadRequestException("Invalid QR code!");
        }
    }

    @Override
    public Map<String, String> generateQRCodeBase64(String request) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(request, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, byteArrayOutputStream);

            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String encodedText = Base64.getEncoder().encodeToString(imageBytes);

            return Map.of("image", encodedText);
        } catch (WriterException | IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

    @Override
    public Map<String, String> readQRCodeBase64(String request) {
        String base64 = request.contains(",") ? request.split(",")[1] : request; // remove "data:image/png;base64," section

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            String decodedText = decodeBufferedImage(bufferedImage);

            QRCodeUtil.validateDecodedText(decodedText);
            QRCodeUtil.validateQRContent(decodedText);

            return Map.of("content", decodedText);
        } catch (IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        } catch (NotFoundException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    private static String decodeBufferedImage(BufferedImage bufferedImage) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }
}
