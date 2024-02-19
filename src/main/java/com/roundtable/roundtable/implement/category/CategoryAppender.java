package com.roundtable.roundtable.implement.category;

import static com.roundtable.roundtable.implement.category.CategoryException.*;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoryAppender {

    private final CategoryRepository categoryRepository;

    public Category appendCategory(CreateCategory createCategory) {
        checkDuplicatedCategoryName(createCategory);

        Category category = Category.create(
                createCategory.name(),
                createCategory.point(),
                createCategory.house());

        return categoryRepository.save(category);
    }

    private void checkDuplicatedCategoryName(CreateCategory createCategory) {
        boolean isExistCategory = categoryRepository.existsByNameAndHouse(createCategory.name(), createCategory.house());
        if(isExistCategory) {
            throw new CategoryDuplicatedException(createCategory.name());
        }
    }
}
