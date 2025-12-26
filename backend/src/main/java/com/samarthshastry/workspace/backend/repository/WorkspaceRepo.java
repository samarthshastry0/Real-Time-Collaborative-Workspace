package com.samarthshastry.workspace.backend.repository;

import com.samarthshastry.workspace.backend.model.Workspace;
import com.samarthshastry.workspace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepo extends JpaRepository<Workspace, UUID> {
    
    List<Workspace> findByOwner(User owner);
    
    List<Workspace> findByMembersContaining(User user);
    
    boolean existsByNameAndOwner(String name, User owner);
}