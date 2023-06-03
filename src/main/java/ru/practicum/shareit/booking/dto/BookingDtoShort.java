package ru.practicum.shareit.booking.dto;

import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoShort {
    @PositiveOrZero
    private long id;
    @PositiveOrZero
    private long bookerId;
}
