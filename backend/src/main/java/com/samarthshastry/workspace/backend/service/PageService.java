package com.samarthshastry.workspace.backend.service;

import com.samarthshastry.workspace.backend.model.Page;
import com.samarthshastry.workspace.backend.model.Workspace;
import com.samarthshastry.workspace.backend.repository.PageRepository;
import com.samarthshastry.workspace.backend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final WorkspaceRepository workspaceRepository;

    public Page createPage(UUID workspaceId, String title, UUID parentPageId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));

        Page page = new Page();
        page.setTitle(title);
        page.setWorkspace(workspace);

        if (parentPageId != null) {
            Page parentPage = pageRepository.findById(parentPageId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent page not found"));
            page.setParentPage(parentPage);
        }

        // Set order based on existing pages
        List<Page> existingPages = parentPageId != null
                ? pageRepository.findByParentPageIdOrderByPageOrder(parentPageId)
                : pageRepository.findByWorkspaceIdOrderByPageOrder(workspaceId);
        page.setPageOrder(existingPages.size());

        return pageRepository.save(page);
    }

    public Optional<Page> getPageById(UUID pageId) {
        return pageRepository.findById(pageId);
    }

    public Optional<Page> getPageByIdAndWorkspace(UUID pageId, UUID workspaceId) {
        return pageRepository.findByIdAndWorkspaceId(pageId, workspaceId);
    }

    public List<Page> getPagesByWorkspace(UUID workspaceId) {
        return pageRepository.findByWorkspaceIdOrderByPageOrder(workspaceId);
    }

    public List<Page> getChildPages(UUID parentPageId) {
        return pageRepository.findByParentPageIdOrderByPageOrder(parentPageId);
    }

    public Page updatePage(UUID pageId, String title) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));
        page.setTitle(title);
        return pageRepository.save(page);
    }

    public void deletePage(UUID pageId) {
        pageRepository.deleteById(pageId);
    }

    public Page reorderPage(UUID pageId, Integer newOrder) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));
        page.setPageOrder(newOrder);
        return pageRepository.save(page);
    }

    public List<Page> getAllPagesInWorkspace(UUID workspaceId) {
        return pageRepository.findAllByWorkspaceId(workspaceId);
    }
}
