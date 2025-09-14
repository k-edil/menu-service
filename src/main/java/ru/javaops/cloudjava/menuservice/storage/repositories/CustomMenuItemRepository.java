package ru.javaops.cloudjava.menuservice.storage.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.storage.model.Category;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem;

import java.util.List;

public interface CustomMenuItemRepository {

//    int updateMenu(Long id, UpdateMenuRequest dto);
    @Modifying
    @Query("""
       UPDATE MenuItem m
       SET m.name = COALESCE(:#{#dto.name}, m.name),
           m.description = COALESCE(:#{#dto.description}, m.description),
           m.price = COALESCE(:#{#dto.price}, m.price),
           m.timeToCook = COALESCE(:#{#dto.timeToCook}, m.timeToCook),
           m.imageUrl = COALESCE(:#{#dto.imageUrl}, m.imageUrl)
       WHERE m.id = :id
       """)
    int updateMenu(@Param("id") Long id, @Param("dto") UpdateMenuRequest dto);

    List<MenuItem> getMenusFor(Category category, SortBy sortBy);
}
