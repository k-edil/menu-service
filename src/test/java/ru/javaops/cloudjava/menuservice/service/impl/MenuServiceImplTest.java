package ru.javaops.cloudjava.menuservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.javaops.cloudjava.menuservice.BaseTest;
import ru.javaops.cloudjava.menuservice.dto.MenuItemDto;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.service.MenuService;
import ru.javaops.cloudjava.menuservice.storage.model.Category;
import ru.javaops.cloudjava.menuservice.storage.repositories.MenuItemRepository;
import ru.javaops.cloudjava.menuservice.testutils.TestData;
import ru.javaops.cloudjava.menuservice.exception.MenuServiceException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;

@SpringBootTest
public class MenuServiceImplTest extends BaseTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemRepository repository;

    @Test
    void getMenusFor_DRINKS_returnsCorrectList() {
        List<MenuItemDto> drinks = menuService.getMenusFor(Category.DRINKS, SortBy.AZ);
        assertThat(drinks).hasSize(3);
        assertElementsInOrder(drinks, MenuItemDto::getName, List.of("Cappuccino", "Tea", "Wine"));
    }

    @Test
    void createMenuItem_createsMenuItem() {
        var dto = TestData.createMenuRequest();
        // Вычитаем некоторое количество наносекунд из-за возможных проблем со сравнением дат (проявляется на Windows,
        // при тестировании на Ubuntu и Mac такой проблемы не возникало)
        // так как Postgres не поддерживает точность дат до наносекунд из коробки
        var now = LocalDateTime.now().minusNanos(1000);
        MenuItemDto result = menuService.createMenuItem(dto);
        assertThat(result.getId()).isNotNull();
        assertFieldsEquality(result, dto, "name", "description", "price", "imageUrl", "timeToCook");
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }


    @Test
    void getMenu_returnsMenu_whenMenuFound() {
        Long id = getIdByName("Cappuccino");
        MenuItemDto dto = menuService.getMenu(id);
        assertThat(dto).isNotNull();
    }

    @Test
    void getMenu_returnsException_whenMenuNotFound() {
        Long id = 1000L;
        assertThrows(MenuServiceException.class,
                () -> menuService.getMenu(id));
    }

    @Test
    void deleteMenu_deletesMenu_whenMenuFound() {
        Long id = getIdByName("Cappuccino");
        assertThat(repository.findById(id)).isPresent();
        menuService.deleteMenuItem(id);
        assertThat(repository.findById(id)).isNotPresent();
    }

    @Test
    void createMenuItem_throws_whenRequestHasNotUniqueName() {
        var dto = TestData.createMenuRequest();
        menuService.createMenuItem(dto);
        assertThrows(MenuServiceException.class,
                () -> menuService.createMenuItem(dto));
    }

    @Test
    void updateMenuItem_updatesMenu_whenAllUpdateFieldsAreSet(){
        var dto = TestData.updateMenuFullRequest();
        Long id = getIdByName("Cappuccino");
        menuService.updateMenuItem(id, dto);
        MenuItemDto updated = menuService.getMenu(id);
        assertFieldsEquality(updated, dto, "name", "description", "price", "imageUrl", "timeToCook");
    }

    @Test
    void updateMenuItem_throws_whenMenuNotFound(){
        Long id = 1000L;
        assertThrows(MenuServiceException.class,
                () -> menuService.updateMenuItem(id, TestData.updateMenuFullRequest()));
    }

    @Test
    void updateMenuItem_throws_whenRequestHasNotUniqueName(){
        Long id = getIdByName("Cappuccino");
        var dto = TestData.updateMenuFullRequest();
        dto.setName("Wine");
        assertThrows(MenuServiceException.class,
                () -> menuService.updateMenuItem(id, dto));
    }
}
