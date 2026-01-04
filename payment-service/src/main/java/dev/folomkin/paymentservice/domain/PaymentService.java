package dev.folomkin.paymentservice.domain;

import dev.folomkin.api.http.payment.CreatePaymentRequestDto;
import dev.folomkin.api.http.payment.PaymentMethod;
import dev.folomkin.api.http.payment.PaymentStatus;
import dev.folomkin.paymentservice.domain.db.PaymentEntityMapper;
import dev.folomkin.paymentservice.domain.db.PaymentEntityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentService {

    private final PaymentEntityMapper mapper;
    private final PaymentEntityRepository repository;

    public dev.folomkin.api.http.payment.CreatePaymentResponseDto makePayment(CreatePaymentRequestDto request) {
        var found = repository.findByOrderId(request.orderId());
        if (found.isPresent()) {
            log.info("Payment already exists for orderId={}", request.orderId());
            return mapper.toResponseDto(found.get());
        }

        var entity = mapper.toEntity(request);
        var status = request.paymentMethod().equals(PaymentMethod.QR)
                ? PaymentStatus.PAYMENT_FILED :
                PaymentStatus.PAYMENT_SUCCEEDED;
        entity.setPaymentStatus(status);
        var savedEntity = repository.save(entity);
        return mapper.toResponseDto(savedEntity);
    }
}
