package com.samarthshastry.workspace.backend.service;

import com.samarthshastry.workspace.backend.model.Workspace;
import com.samarthshastry.workspace.backend.model.User;
import com.samarthshastry.workspace.backend.repository.WorkspaceRepo;
import com.samarthshastry.workspace.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepo workspaceRepository;
    private final UserRepo userRepository;

    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    public Optional<Workspace> getWorkspaceById(UUID id) {
        return workspaceRepository.findById(id);
    }

    public List<Workspace> getWorkspacesByOwner(UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        return workspaceRepository.findByOwner(owner);
    }

    public List<Workspace> getWorkspacesByMember(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return workspaceRepository.findByMembersContaining(user);
    }

    @Transactional
    public Workspace createWorkspace(String name, String description, UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (workspaceRepository.existsByNameAndOwner(name, owner)) {
            throw new RuntimeException("Workspace with this name already exists for this owner");
        }

        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setDescription(description);
        workspace.setOwner(owner);
        
        workspace.addMember(owner);

        return workspaceRepository.save(workspace);
    }

    @Transactional
    public Workspace addMember(UUID workspaceId, UUID userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        workspace.addMember(user);
        return workspaceRepository.save(workspace);
    }

    @Transactional
    public Workspace removeMember(UUID workspaceId, UUID userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent removing the owner
        if (workspace.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Cannot remove the owner from the workspace");
        }

        workspace.removeMember(user);
        return workspaceRepository.save(workspace);
    }

    @Transactional
    public Workspace updateWorkspaceName(UUID workspaceID, String name) {
        Workspace workspace = workspaceRepository.findById(workspaceID)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!name.equals(workspace.getName())) {
            workspace.setName(name);
            return workspaceRepository.save(workspace);
        } else {
            return workspace;
        }
    }

    @Transactional
    public Workspace updateWorkspaceDescription(UUID workspaceID, String description) {
        Workspace workspace = workspaceRepository.findById(workspaceID)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!description.equals(workspace.getDescription())) {
            workspace.setDescription(description);
            return workspaceRepository.save(workspace);
        } else {
            return workspace;
        }
    }

    public void deleteWorkspace(UUID id) {
        workspaceRepository.deleteById(id);
    }
}