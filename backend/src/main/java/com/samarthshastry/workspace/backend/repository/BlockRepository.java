package com.samarthshastry.workspace.backend.repository;

import com.samarthshastry.workspace.backend.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {
    List<Block> findByPageIdOrderByBlockOrder(UUID pageId);
    Optional<Block> findByIdAndPageId(UUID blockId, UUID pageId);
    void deleteAllByPageId(UUID pageId);
}
