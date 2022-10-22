package ru.maxryazan.bankofryazan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.SystemMessage;
import ru.maxryazan.bankofryazan.repository.SystemMessageRepository;

@Service
@RequiredArgsConstructor
public class SystemMessageService {

    private final SystemMessageRepository messageRepository;

    public void save(SystemMessage message){
        messageRepository.save(message);
    }
}
