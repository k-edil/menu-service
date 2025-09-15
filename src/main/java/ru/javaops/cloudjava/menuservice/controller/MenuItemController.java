package ru.javaops.cloudjava.menuservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.javaops.cloudjava.menuservice.dto.CreateMenuRequest;
import ru.javaops.cloudjava.menuservice.dto.MenuItemDto;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.service.MenuService;
import ru.javaops.cloudjava.menuservice.storage.model.Category;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuService menuService;

    @PostMapping
    public MenuItemDto createMenuItem(CreateMenuRequest dto) {
        log.info("createMenuItem");
        return menuService.createMenuItem(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteMenuItem(Long id) {
        log.info("deleteMenuItem");
        menuService.deleteMenuItem(id);
    }

    @PatchMapping("/{id}")
    public MenuItemDto updateMenu(@PathVariable Long id, @RequestBody UpdateMenuRequest update) {
        log.info("updateMenu");
        return menuService.updateMenuItem(id, update);
    }

    @GetMapping("/{id}")
    public MenuItemDto getMenu(@PathVariable Long id) {
        log.info("getMenu");
        return menuService.getMenu(id);
    }

    @GetMapping
    public List<MenuItemDto> getMenusFor(@RequestParam Category category, @RequestParam SortBy sortBy) {
        log.info("getMenusFor");
        return menuService.getMenusFor(category, sortBy);
    }
}
