package com.samarthshastry.workspace.backend.controller;

import com.samarthshastry.workspace.backend.model.Block;
import com.samarthshastry.workspace.backend.service.BlockService;
import com.samarthshastry.workspace.backend.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pages/{pageId}/blocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BlockController {

    private final BlockService blockService;
    private final PageService pageService;

    @PostMapping
    public ResponseEntity<?> createBlock(
            @PathVariable UUID pageId,
            @RequestBody Map<String, String> request) {

        String type = request.get("type");
        String content = request.get("content");

        if (type == null || type.isBlank()) {
            return ResponseEntity.badRequest().body("Block type is required");
        }

        // Verify page exists
        if (pageService.getPageById(pageId).isEmpty()) {
            return ResponseEntity.badRequest().body("Page not found");
        }

        try {
            Block block = blockService.createBlock(pageId, type, content);
            return ResponseEntity.status(HttpStatus.CREATED).body(block);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Block>> getBlocksByPage(@PathVariable UUID pageId) {
        // Verify page exists
        if (pageService.getPageById(pageId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Block> blocks = blockService.getBlocksByPage(pageId);
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/{blockId}")
    public ResponseEntity<Block> getBlockById(
            @PathVariable UUID pageId,
            @PathVariable UUID blockId) {

        return blockService.getBlockByIdAndPageId(blockId, pageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<?> updateBlock(
            @PathVariable UUID pageId,
            @PathVariable UUID blockId,
            @RequestBody Map<String, String> request) {

        String content = request.get("content");
        String type = request.get("type");

        // Verify block belongs to page
        if (blockService.getBlockByIdAndPageId(blockId, pageId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Block updatedBlock = blockService.updateBlockContent(blockId, content, type);
        return ResponseEntity.ok(updatedBlock);
    }

    @PatchMapping("/{blockId}/order")
    public ResponseEntity<?> updateBlockOrder(
            @PathVariable UUID pageId,
            @PathVariable UUID blockId,
            @RequestBody Map<String, Integer> request) {

        Integer newOrder = request.get("order");

        if (newOrder == null || newOrder < 0) {
            return ResponseEntity.badRequest().body("Valid order is required");
        }

        // Verify block belongs to page
        if (blockService.getBlockByIdAndPageId(blockId, pageId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Block updatedBlock = blockService.updateBlockOrder(blockId, newOrder);
        return ResponseEntity.ok(updatedBlock);
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<?> deleteBlock(
            @PathVariable UUID pageId,
            @PathVariable UUID blockId) {

        // Verify block belongs to page
        if (blockService.getBlockByIdAndPageId(blockId, pageId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        blockService.deleteBlock(blockId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reorder")
    public ResponseEntity<?> reorderBlocks(
            @PathVariable UUID pageId,
            @RequestBody Map<String, Object> request) {

        @SuppressWarnings("unchecked")
        List<Map<String, String>> blockIds = (List<Map<String, String>>) request.get("blockIds");

        if (blockIds == null || blockIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Block IDs are required");
        }

        try {
            List<Block> blocks = blockService.getBlocksByPage(pageId);
            List<Block> reorderedBlocks = blockService.reorderBlocks(pageId, blocks);
            return ResponseEntity.ok(reorderedBlocks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
