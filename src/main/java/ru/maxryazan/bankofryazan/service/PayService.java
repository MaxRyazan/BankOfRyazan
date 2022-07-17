package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.repository.PayRepository;


@Service
public class PayService {

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }


    public void save(Pay pay){
        payRepository.save(pay);
    }


}
