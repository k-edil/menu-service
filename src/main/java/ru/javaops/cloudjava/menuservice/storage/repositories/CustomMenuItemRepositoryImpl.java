package ru.javaops.cloudjava.menuservice.storage.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.cloudjava.menuservice.dto.SortBy;
import ru.javaops.cloudjava.menuservice.dto.UpdateMenuRequest;
import ru.javaops.cloudjava.menuservice.storage.model.Category;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem;
import ru.javaops.cloudjava.menuservice.storage.model.MenuItem_;
import ru.javaops.cloudjava.menuservice.storage.repositories.updaters.MenuAttrUpdater;

import java.util.List;

import static ru.javaops.cloudjava.menuservice.storage.model.MenuItem_.NAME;
import static ru.javaops.cloudjava.menuservice.storage.model.MenuItem_.PRICE;

@Repository
public class CustomMenuItemRepositoryImpl implements CustomMenuItemRepository {

    private final EntityManager em;
    private final List<MenuAttrUpdater<?>> updaters;

    public CustomMenuItemRepositoryImpl(EntityManager em, List<MenuAttrUpdater<?>> updaters) {
        this.em = em;
        this.updaters = updaters;
    }

    @Transactional
    @Override
    public int updateMenu(Long id, UpdateMenuRequest dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<MenuItem> criteriaUpdate = cb.createCriteriaUpdate(MenuItem.class);
        Root<MenuItem> root = criteriaUpdate.from(MenuItem.class);
        updaters.forEach(updater -> updater.updateAttr(criteriaUpdate, dto));
        criteriaUpdate.where(cb.equal(root.get(MenuItem_.id), id));
        return em.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public List<MenuItem> getMenusFor(Category category, SortBy sortBy) {
        // TODO
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MenuItem> cq = cb.createQuery(MenuItem.class);
        Root<MenuItem> root = cq.from(MenuItem.class);

        // Фильтр по категории
        cq.where(cb.equal(root.get(MenuItem_.category), category));

        // Сортировка через SortBy
        if (sortBy != null) {
            cq.orderBy(sortBy.getOrder(cb, root));
        }

        return em.createQuery(cq).getResultList();
    }
}
