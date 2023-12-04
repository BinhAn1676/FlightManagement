package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.PaymentDto;
import com.binhan.flightmanagement.dto.TransactionStatusDto;
import com.binhan.flightmanagement.service.PaymentService;
import com.binhan.flightmanagement.util.PaymentConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    @GetMapping()
    public ResponseEntity<?> createPayment(@RequestParam("price") Long price,
                                           @RequestParam("reservationId")Long id) throws UnsupportedEncodingException {

        String vnp_IpAddr = "127.0.0.1";
        Long amount = price *100;
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", PaymentConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        //vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_TxnRef", String.valueOf(id));//id reservation
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", PaymentConfig.orderType);
        vnp_Params.put("vnp_ReturnUrl",PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        //vnp_Params.put("vnp_reservationId", String.valueOf(id));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStatus("OK");
        paymentDto.setMessage("Successfully");
        paymentDto.setURL(paymentUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDto);
    }

    @GetMapping("/info")
    public ResponseEntity<?> transaction(@RequestParam("vnp_Amount") String amount,
                                         @RequestParam("vnp_BankCode") String bankCode,
                                         @RequestParam("vnp_ResponseCode") String responseCode,
                                         @RequestParam("vnp_TxnRef") Long reservationId){
        TransactionStatusDto transactionStatusDto = new TransactionStatusDto();
        if(!responseCode.equals("00")){
            transactionStatusDto.setStatus("NO");
            transactionStatusDto.setMessage("Failed");
            transactionStatusDto.setData("");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transactionStatusDto);
        }
        paymentService.savePayment(amount,bankCode,reservationId);


        if(responseCode.equals("00")){
            transactionStatusDto.setStatus("OK");
            transactionStatusDto.setMessage("Successfully");
            transactionStatusDto.setData("");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDto);
    }
}
