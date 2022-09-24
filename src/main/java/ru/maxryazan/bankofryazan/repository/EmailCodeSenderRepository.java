package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;

import java.util.List;


@Repository
public interface EmailCodeSenderRepository extends JpaRepository<EmailCodeSender, Long> {

    void deleteByPassRestorer(Client client);

}
