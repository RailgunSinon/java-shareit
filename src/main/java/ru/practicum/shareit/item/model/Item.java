package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
// Я немного подкорректировал структуру, чтобы было одинаково везде и более "красиво".
// Также заложил фундамент на следующее ТЗ, ибо я из-за майских и работы на них опаздываю)
// Так что летим вперёд

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    int id;
    int userId;
    String name;
    String description;
    boolean available;
}
