package com.samarthshastry.workspace.backend.controller;

import com.samarthshastry.workspace.backend.model.Page;
import com.samarthshastry.workspace.backend.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/pages")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PageController {

    private final PageService pageService;

    @PostMapping
    public ResponseEntity<?> createPage(
            @PathVariable UUID workspaceId,
            @RequestBody Map<String, Object> request) {

        String title = (String) request.get("title");
        Object parentPageIdObj = request.get("parentPageId");

        if (title == null || title.isBlank()) {
            return ResponseEntity.badRequest().body("Page title is required");
        }

        UUID parentPageId = null;
        if (parentPageIdObj != null) {
            try {
                parentPageId = UUID.fromString(parentPageIdObj.toString());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid parentPageId format");
            }
        }

        try {
            Page page = pageService.createPage(workspaceId, title, parentPageId);
            return ResponseEntity.status(HttpStatus.CREATED).body(page);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Page>> getPagesByWorkspace(@PathVariable UUID workspaceId) {
        List<Page> pages = pageService.getPagesByWorkspace(workspaceId);
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<Page> getPageById(
            @PathVariable UUID workspaceId,
            @PathVariable UUID pageId) {

        return pageService.getPageByIdAndWorkspace(pageId, workspaceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{pageId}/children")
    public ResponseEntity<List<Page>> getChildPages(@PathVariable UUID pageId) {
        List<Page> children = pageService.getChildPages(pageId);
        return ResponseEntity.ok(children);
    }

    @PutMapping("/{pageId}")
    public ResponseEntity<?> updatePage(
            @PathVariable UUID workspaceId,
            @PathVariable UUID pageId,
            @RequestBody Map<String, String> request) {

        String title = request.get("title");

        if (title == null || title.isBlank()) {
            return ResponseEntity.badRequest().body("Page title cannot be empty");
        }

        // Verify page belongs to workspace
        if (pageService.getPageByIdAndWorkspace(pageId, workspaceId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page updatedPage = pageService.updatePage(pageId, title);
        return ResponseEntity.ok(updatedPage);
    }

    @PatchMapping("/{pageId}/order")
    public ResponseEntity<?> reorderPage(
            @PathVariable UUID workspaceId,
            @PathVariable UUID pageId,
            @RequestBody Map<String, Integer> request) {

        Integer newOrder = request.get("order");

        if (newOrder == null || newOrder < 0) {
            return ResponseEntity.badRequest().body("Valid order is required");
        }

        // Verify page belongs to workspace
        if (pageService.getPageByIdAndWorkspace(pageId, workspaceId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page reorderedPage = pageService.reorderPage(pageId, newOrder);
        return ResponseEntity.ok(reorderedPage);
    }

    @DeleteMapping("/{pageId}")
    public ResponseEntity<?> deletePage(
            @PathVariable UUID workspaceId,
            @PathVariable UUID pageId) {

        // Verify page belongs to workspace
        if (pageService.getPageByIdAndWorkspace(pageId, workspaceId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pageService.deletePage(pageId);
        return ResponseEntity.noContent().build();
    }
}
