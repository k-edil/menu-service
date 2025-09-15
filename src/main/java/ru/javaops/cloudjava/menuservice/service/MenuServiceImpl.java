package ru.javaops.cloudjava.menuservice.service;

import org.springframework.stereotype.Service;
import ru.javaops.cloudjava.menuservice.dto.CreateMenuRequest;
import ru.javaops.cloudjava.menuservice.dto.MenuItemDto;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.storage.model.Category;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Override
    public MenuItemDto createMenuItem(CreateMenuRequest dto) {
        return null;
    }

    @Override
    public void deleteMenuItem(Long id) {

    }

    @Override
    public MenuItemDto updateMenuItem(Long id, UpdateMenuRequest update) {
        return null;
    }

    @Override
    public MenuItemDto getMenu(Long id) {
        return null;
    }

    @Override
    public List<MenuItemDto> getMenusFor(Category category, SortBy sortBy) {
        return List.of();
    }
}
