package com.psp.qrcodeservice.service;

import com.psp.qrcodeservice.model.QrCode;
import com.psp.qrcodeservice.repository.QrCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class QrCodeService {

    @Autowired
    private QrCodeRepository qrCodeRepository;

    public void save(QrCode qrCode) {
        qrCodeRepository.save(qrCode);
    }

    public ResponseEntity<?> getQrCodeImage(String imageName) {
        try {
            String path = "./src/main/resources/qr-codes/" + imageName + ".png";
            String filePath = new File(path).getAbsolutePath();
            UrlResource resource = new UrlResource("file:" + filePath);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaTypeFactory
                            .getMediaType(resource.getFile().getAbsolutePath())
                            .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
