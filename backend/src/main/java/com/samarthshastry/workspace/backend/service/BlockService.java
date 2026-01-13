package com.samarthshastry.workspace.backend.service;

import com.samarthshastry.workspace.backend.model.Block;
import com.samarthshastry.workspace.backend.model.Page;
import com.samarthshastry.workspace.backend.repository.BlockRepository;
import com.samarthshastry.workspace.backend.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final PageRepository pageRepository;

    public Block createBlock(UUID pageId, String type, String content) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));

        Block block = new Block();
        block.setPage(page);
        block.setType(type);
        block.setContent(content != null ? content : "");

        // Set order based on existing blocks
        List<Block> existingBlocks = blockRepository.findByPageIdOrderByBlockOrder(pageId);
        block.setBlockOrder(existingBlocks.size());

        return blockRepository.save(block);
    }

    public Optional<Block> getBlockById(UUID blockId) {
        return blockRepository.findById(blockId);
    }

    public Optional<Block> getBlockByIdAndPageId(UUID blockId, UUID pageId) {
        return blockRepository.findByIdAndPageId(blockId, pageId);
    }

    public List<Block> getBlocksByPage(UUID pageId) {
        return blockRepository.findByPageIdOrderByBlockOrder(pageId);
    }

    public Block updateBlockContent(UUID blockId, String content, String type) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));
        block.setContent(content);
        if (type != null) {
            block.setType(type);
        }
        return blockRepository.save(block);
    }

    public Block updateBlockOrder(UUID blockId, Integer newOrder) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));
        block.setBlockOrder(newOrder);
        return blockRepository.save(block);
    }

    public void deleteBlock(UUID blockId) {
        blockRepository.deleteById(blockId);
    }

    public void deleteAllBlocksByPage(UUID pageId) {
        blockRepository.deleteAllByPageId(pageId);
    }

    public List<Block> reorderBlocks(UUID pageId, List<Block> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setBlockOrder(i);
        }
        return blockRepository.saveAll(blocks);
    }
}
