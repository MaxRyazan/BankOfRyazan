package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.SystemMessage;

@Repository
public interface SystemMessageRepository extends JpaRepository<SystemMessage, Long> {
}
