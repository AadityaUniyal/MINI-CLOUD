package com.minicloud.backend.repository;

import com.minicloud.backend.model.DynamoItem;
import com.minicloud.backend.model.DynamoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DynamoItemRepository extends JpaRepository<DynamoItem, Long> {
    Optional<DynamoItem> findByTableAndKey(DynamoTable table, String key);
    List<DynamoItem> findByTable(DynamoTable table);
}
