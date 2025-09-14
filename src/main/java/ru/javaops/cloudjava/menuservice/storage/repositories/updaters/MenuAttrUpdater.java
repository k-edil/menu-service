package ru.javaops.cloudjava.menuservice.storage.repositories.updaters;

import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.AllArgsConstructor;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem;

import java.util.function.Function;

@AllArgsConstructor
public class MenuAttrUpdater<V> {
    // функция для получения значения поля из DTO
    private final Function<UpdateMenuRequest, V> valueExtractor;

    // имя атрибута в сущности MenuItem
    private final SingularAttribute<MenuItem, V> attr;

    public void updateAttr(CriteriaUpdate<MenuItem> criteria, UpdateMenuRequest dto) {
        //  TODO  criteria.set(attr, dtoValue);
        V value = valueExtractor.apply(dto);
        if (value != null) {
            criteria.set(attr, value);
        }
    }
}
