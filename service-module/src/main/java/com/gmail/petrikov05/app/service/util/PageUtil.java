package com.gmail.petrikov05.app.service.util;

public class PageUtil {

    public static int getStartPosition(int page, int countByPage) {
        return (page - 1) * countByPage;
    }

    public static int getCountOfPage(Long countOfEntities, int countByPage) {
        int pages = (int) (countOfEntities / countByPage);
        if (countOfEntities % countByPage != 0) {
            pages++;
        }
        return pages;
    }

}
