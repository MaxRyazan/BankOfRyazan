package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
