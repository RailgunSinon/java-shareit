package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    @PositiveOrZero
    private long id;
    @NotNull
    @NotBlank
    private String description;
    @PositiveOrZero
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemDto> items;
}
