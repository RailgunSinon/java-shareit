package ru.practicum.shareit.booking.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(
        @Valid @RequestBody BookingRequestDto bookingRequestDto,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на добавление брони предмета со стороны клиента");
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestParam boolean approved,
        @PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на обновление брони предмета со стороны клиента");
        return bookingClient.patchBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на получение информации о брони предмета со стороны клиента");
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getCurrentBookingForUser(
        @RequestParam(required = false, defaultValue = "ALL") String state,
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Получен запрос на получение информации о брони предметов пользователя "
            + "со стороны клиента");
        return bookingClient.getCurrentBookingForUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getCurrentBookingForOwner(
        @RequestParam(required = false, defaultValue = "ALL") String state,
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Получен запрос на получение информации о брони предметов владельца "
            + "со стороны клиента");
        return bookingClient.getCurrentBookingForOwner(userId, state, from, size);
    }
}
