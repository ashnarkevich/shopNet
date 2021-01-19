package com.gmail.petrikov05.app.service.model;

import java.util.List;

public class PaginationWithEntitiesDTO<T> {

    private List<T> entities;
    private int pages;

    public PaginationWithEntitiesDTO(List<T> entities, int pages) {
        this.entities = entities;
        this.pages = pages;
    }

    public List<T> getEntities() {
        return entities;
    }

    public int getPages() {
        return pages;
    }

}
