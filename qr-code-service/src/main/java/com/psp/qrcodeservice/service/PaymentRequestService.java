package com.psp.qrcodeservice.service;


import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.psp.qrcodeservice.dto.*;
import com.psp.qrcodeservice.model.PaymentRequest;
import com.psp.qrcodeservice.model.QrCode;
import com.psp.qrcodeservice.repository.PaymentRequestRepository;
import com.psp.qrcodeservice.util.QrCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> createPaymentRequest(ServicePaymentDto servicePaymentDto) {
        PaymentRequest paymentRequest = modelMapper.map(servicePaymentDto, PaymentRequest.class);
        paymentRequest.setMerchantId(servicePaymentDto.getCredentialsId());
        paymentRequest.setMerchantTimestamp(servicePaymentDto.getTimestamp());
        paymentRequest.setMerchantOrderId(String.format("%.0f", (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L)));

        paymentRequestRepository.save(paymentRequest);
        log.debug("Payment request with id {} created. Merchant id : {}, merchant Order id: {}",
                paymentRequest.getId(), paymentRequest.getMerchantId(), paymentRequest.getMerchantOrderId());

        PaymentRequestDto paymentRequestDto = modelMapper.map(paymentRequest, PaymentRequestDto.class);
        paymentRequestDto.setMerchantPassword(servicePaymentDto.getCredentialsSecret());
        paymentRequestDto.setIsBankCardPayment(false);
        ResponseEntity<String> response = restTemplate.postForEntity(servicePaymentDto.getMerchantBankUrl() + "/payments/", paymentRequestDto, String.class);
        try {
            generateQrCode(Objects.requireNonNull(response.getBody()).substring(response.getBody().length() - 10), servicePaymentDto);
        } catch (IOException | WriterException ex) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    private void generateQrCode(String paymentId, ServicePaymentDto servicePaymentDto) throws IOException, WriterException {
        ResponseEntity<PaymentInfoDto> response = restTemplate.getForEntity(servicePaymentDto.getMerchantBankUrl() + "/merchants/" + servicePaymentDto.getCredentialsId(), PaymentInfoDto.class);
        PaymentInfoDto paymentInfoDto = Objects.requireNonNull(response.getBody());
        paymentInfoDto.setAmount(servicePaymentDto.getAmount());
        String qrText = formQrText(paymentInfoDto);
        String path = "./src/main/resources/qr-codes/" + paymentId + ".png";
        String filePath = new File(path).getAbsolutePath();
        QrCodeGenerator.generateQRCodeImage(qrText, filePath);
        if (isQrValid(filePath, paymentInfoDto)) {
            QrCode qrCode = new QrCode(paymentId, paymentId + ".png");
            qrCodeService.save(qrCode);
        }
    }

    private boolean isQrValid(String filePath, PaymentInfoDto paymentInfoDto) {
        try {
            String qrText = QrCodeGenerator.readQrCode(filePath);
            String[] parts = qrText.split("\\|");
            if (parts.length == 4) {
                return parts[0].toLowerCase(Locale.ROOT).equals("recipient:" + paymentInfoDto.getMerchantCompanyName().toLowerCase(Locale.ROOT))
                        && parts[1].toLowerCase(Locale.ROOT).equals("account:" + paymentInfoDto.getMerchantAccountNumber())
                        && parts[2].toLowerCase(Locale.ROOT).equals("amount:" + paymentInfoDto.getAmount())
                        && parts[3].toLowerCase(Locale.ROOT).equals("currency:usd");
            } else {
                return false;
            }
        } catch (NotFoundException | IOException e) {
            return false;
        }
    }

    private String formQrText(PaymentInfoDto paymentInfoDto) {
        return "recipient:" + paymentInfoDto.getMerchantCompanyName() + "|account:" + paymentInfoDto.getMerchantAccountNumber()
                + "|amount:" + paymentInfoDto.getAmount() + "|currency:USD";
    }


    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PaymentRequest> requests = paymentRequestRepository.findByMerchantIdAndActive(historyFilterDto.getCredentialsId(), true, pageable);
        Page<HistoryResponseDto> requestsDto = requests.map(paymentRequest -> new HistoryResponseDto(historyFilterDto.getServiceName(), "ACTIVE", paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp()));
        return requestsDto.getContent();
    }

    public void updateActiveStatus(PaymentResponseDto paymentResponseDto) {
        PaymentRequest paymentRequest = paymentRequestRepository.findByMerchantOrderId(paymentResponseDto.getMerchantOrderId());
        paymentRequest.setActive(false);
        paymentRequestRepository.save(paymentRequest);
    }
}
