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
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<?> createWorkspace(@RequestBody Map<String, String> request) {

        String name = request.get("name");
        String description = request.get("description");
        String ownerIdRaw = request.get("ownerId");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Workspace name is required");
        }

        if (ownerIdRaw == null) {
            return ResponseEntity.badRequest().body("ownerId is required");
        }

        UUID ownerId;
        try {
            ownerId = UUID.fromString(ownerIdRaw);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ownerId format");
        }

        Workspace workspace = workspaceService.createWorkspace(
                name,
                description,
                ownerId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
    }

    @PostMapping("/{workspaceId}/members/{userId}")
    public ResponseEntity<?> addMember(
            @PathVariable UUID workspaceId,
            @PathVariable UUID userId) {

        Workspace workspace = workspaceService.addMember(workspaceId, userId);
        return ResponseEntity.ok(workspace);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateWorkspaceName(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {

        String name = request.get("name");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Name cannot be empty");
        }

        Workspace workspace = workspaceService.updateWorkspaceName(id, name);
        return ResponseEntity.ok(workspace);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateWorkspaceDescription(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {

        String description = request.get("description");

        if (description == null) {
            return ResponseEntity.badRequest().body("Description is required");
        }

        Workspace workspace = workspaceService.updateWorkspaceDescription(id, description);
        return ResponseEntity.ok(workspace);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
}
