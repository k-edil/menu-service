package ru.javaops.cloudjava.menuservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.javaops.cloudjava.menuservice.dto.CreateMenuRequest;
import ru.javaops.cloudjava.menuservice.dto.MenuItemDto;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.exception.MenuServiceException;
import ru.javaops.cloudjava.menuservice.mapper.MenuItemMapper;
import ru.javaops.cloudjava.menuservice.service.MenuService;
import ru.javaops.cloudjava.menuservice.storage.model.Category;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem;
import ru.javaops.cloudjava.menuservice.storage.repositories.MenuItemRepository;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private MenuItemMapper mapper;

    @Override
    public MenuItemDto createMenuItem(CreateMenuRequest dto) {
        if (repository.findByName(dto.getName()) != null) {
            throw new MenuServiceException("Menu with name " + dto.getName() + " already exists", HttpStatus.CONFLICT);
        }

        try {
            MenuItem entity = mapper.toDomain(dto);
            MenuItem saved = repository.save(entity);
            return mapper.toDto(saved);
        }
        catch (DataIntegrityViolationException e) {
            throw new MenuServiceException("Menu with name " + dto.getName() + " already exists", HttpStatus.CONFLICT);
        }
    }

    @Override
    public void deleteMenuItem(Long id) {
        repository.deleteById(id);
    }

    @Override
    public MenuItemDto updateMenuItem(Long id, UpdateMenuRequest update) {
        MenuItem item = repository.findByName(update.getName());
        if (item != null && !item.getId().equals(id)) {
            throw new MenuServiceException("Menu with name " + update.getName() + " already exists", HttpStatus.CONFLICT);
        }

        try {
            int resultId = repository.updateMenu(id, update);
            if (resultId == 0) {
                throw new MenuServiceException("Menu with id " + id + " not found", HttpStatus.NOT_FOUND);
            }
            return mapper.toDto(repository.findById(id).get());
        }
        catch (DataIntegrityViolationException e) {
            throw new MenuServiceException("Menu with name " + update.getName() + " already exists", HttpStatus.CONFLICT);
        }
    }

    @Override
    public MenuItemDto getMenu(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new MenuServiceException("Menu not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<MenuItemDto> getMenusFor(Category category, SortBy sortBy) {
        return mapper.toDtoList(repository.getMenusFor(category, sortBy));
    }
}
