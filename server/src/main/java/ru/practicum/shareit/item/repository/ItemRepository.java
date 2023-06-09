package ru.practicum.shareit.item.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByUserIdOrderById(long userId);

    @Query(value = "select i from Item i where lower(i.name) like %?1% "
        + "or lower(i.description) like %?1% and i.available=true")
    List<Item> findByNameOrDescriptionLike(String text, Pageable pageable);

    List<Item> findAllByRequestIdOrderById(long requestId, Pageable pageable);
}
