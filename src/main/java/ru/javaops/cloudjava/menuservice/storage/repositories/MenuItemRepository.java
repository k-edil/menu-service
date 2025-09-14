package ru.javaops.cloudjava.menuservice.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem;

import java.util.List;


public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, CustomMenuItemRepository {

    List<MenuItem> findByName(String name);
}
