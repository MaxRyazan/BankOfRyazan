package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;

@Repository
public interface EmailCodeSenderRepository extends JpaRepository<EmailCodeSender, Long> {
}
