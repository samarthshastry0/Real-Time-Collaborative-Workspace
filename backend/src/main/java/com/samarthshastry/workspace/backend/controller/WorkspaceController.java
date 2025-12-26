package com.samarthshastry.workspace.backend.controller;

import com.samarthshastry.workspace.backend.model.Workspace;
import com.samarthshastry.workspace.backend.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable UUID id) {
        return workspaceService.getWorkspaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Workspace>> getWorkspacesByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByOwner(ownerId));
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<List<Workspace>> getWorkspacesByMember(@PathVariable UUID userId) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByMember(userId));
    }

    @PostMapping
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String description = request.get("description");
            UUID ownerId = UUID.fromString(request.get("ownerId"));

            if (name == null || name.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            Workspace workspace = workspaceService.createWorkspace(name, description, ownerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{workspaceId}/members/{userId}")
    public ResponseEntity<Workspace> addMember(
            @PathVariable UUID workspaceId,
            @PathVariable UUID userId) {
        try {
            Workspace workspace = workspaceService.addMember(workspaceId, userId);
            return ResponseEntity.ok(workspace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{workspaceId}/members/{userId}")
    public ResponseEntity<Workspace> removeMember(
            @PathVariable UUID workspaceId,
            @PathVariable UUID userId) {
        try {
            Workspace workspace = workspaceService.removeMember(workspaceId, userId);
            return ResponseEntity.ok(workspace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

@PatchMapping("/{id}/name")
    public ResponseEntity<Workspace> updateWorkspaceName(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            if (name == null || name.isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            
            Workspace workspace = workspaceService.updateWorkspaceName(id, name);
            return ResponseEntity.ok(workspace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<Workspace> updateWorkspaceDescription(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String description = request.get("description");
            if (description == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Workspace workspace = workspaceService.updateWorkspaceDescription(id, description);
            return ResponseEntity.ok(workspace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
}