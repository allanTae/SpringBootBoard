package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Category;

public interface CategoryRepository {
    public Long save(Category category);
    public Category findOne(Long categoryId);
    public void deleteAll();
}
