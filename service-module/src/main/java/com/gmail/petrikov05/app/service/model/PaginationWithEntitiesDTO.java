package com.gmail.petrikov05.app.service.model;

import java.util.List;

public class PaginationWithEntitiesDTO<T> {

    private List<T> entities;
    private int pages;

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

}
