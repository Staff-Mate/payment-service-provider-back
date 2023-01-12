package com.psp.qrcodeservice.controller;

import com.psp.qrcodeservice.service.QrCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/qr-codes")
@Slf4j
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping(value = "/{image}")
    public @ResponseBody ResponseEntity<?> getImage(@PathVariable("image") String imageName){
        return qrCodeService.getQrCodeImage(imageName);
    }
}
