package com.minicloud.repository;

import com.minicloud.model.DynamoItem;
import com.minicloud.model.DynamoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DynamoItemRepository extends JpaRepository<DynamoItem, Long> {
    Optional<DynamoItem> findByTableAndKey(DynamoTable table, String key);
    List<DynamoItem> findByTable(DynamoTable table);
}
