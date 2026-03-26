package com.minicloud.repository;

import com.minicloud.model.DynamoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DynamoTableRepository extends JpaRepository<DynamoTable, Long> {
    Optional<DynamoTable> findByTableName(String tableName);
    List<DynamoTable> findByOwner(String owner);
}
