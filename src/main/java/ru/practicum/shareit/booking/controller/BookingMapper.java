package ru.practicum.shareit.booking.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public BookingDto convertToDto(Booking booking) {
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        return bookingDto;
    }

    public BookingDtoShort convertToShort(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDtoShort bookingDtoShort = new BookingDtoShort();
        bookingDtoShort.setId(booking.getId());
        bookingDtoShort.setBookerId(booking.getBooker().getId());
        return bookingDtoShort;
    }

    public List<BookingDto> convertToDtoListOfBooking(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDtos.add(convertToDto(booking));
        }
        return bookingDtos;
    }

    public Booking convertToEntity(BookingDtoInput bookingDtoInput) {
        validateDate(bookingDtoInput);
        Booking booking = modelMapper.map(bookingDtoInput, Booking.class);
        return booking;
    }

    private void validateDate(BookingDtoInput bookingDtoInput) {
        if (!bookingDtoInput.getStart().isBefore(bookingDtoInput.getEnd())) {
            throw new ValidationException("Время начала брони должно быть раньше времени конца!");
        }
    }
}
