package com.samarthshastry.workspace.backend.repository;

import com.samarthshastry.workspace.backend.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PageRepository extends JpaRepository<Page, UUID> {
    List<Page> findByWorkspaceIdOrderByPageOrder(UUID workspaceId);
    List<Page> findByParentPageIdOrderByPageOrder(UUID parentPageId);
    Optional<Page> findByIdAndWorkspaceId(UUID pageId, UUID workspaceId);
    List<Page> findAllByWorkspaceId(UUID workspaceId);
}
