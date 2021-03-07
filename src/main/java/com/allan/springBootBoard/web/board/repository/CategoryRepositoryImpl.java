package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.repository.mapper.CategoryMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository{

    @PersistenceContext
    EntityManager em;

    @NonNull
    private CategoryMapper categoryMapper;

    @Override
    public Long save(Category category) {
        em.persist(category);
        return category.getCategoryId();
    }

    @Override
    public Category findOne(Long categoruId) {
        return em.find(Category.class, categoruId);
    }

    @Override
    public void deleteAll() {
        categoryMapper.deleteAll();
    }
}
