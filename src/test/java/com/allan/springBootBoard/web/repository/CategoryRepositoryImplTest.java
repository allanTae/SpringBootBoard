package com.allan.springBootBoard.web.repository;

import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryRepositoryImplTest {

    @Autowired
    CategoryRepository categoryRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 카테고리_등록() throws Exception {
        //given
        Category cat = Category.builder()
                .name("자유게시판")
                .createdBy("관리자")
                .createdDate(LocalDateTime.now())
                .build();

        //when
        Long categoryId = categoryRepository.save(cat);
        em.flush();
        em.clear();

        Category findCategory = categoryRepository.findOne(categoryId);

        //then
        assertEquals(findCategory.getName(), "자유게시판");
        assertEquals(findCategory.getCreatedBy(), "관리자");
    }
}