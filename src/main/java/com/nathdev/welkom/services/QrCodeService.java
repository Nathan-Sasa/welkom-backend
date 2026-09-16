package com.nathdev.welkom.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service
public class QrCodeService {
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    public byte[] generateQrCode(UUID qrToken) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    qrToken.toString(),
                    BarcodeFormat.QR_CODE,
                    QR_WIDTH,
                    QR_HEIGHT
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    matrix,
                    "PNG",
                    outputStream
            );
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate QR code", e);
        }
    }
}
