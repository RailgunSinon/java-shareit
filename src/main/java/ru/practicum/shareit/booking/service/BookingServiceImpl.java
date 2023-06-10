package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.State;
import ru.practicum.shareit.booking.model.enums.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public void addBooking(Booking booking, long userId, long itemId) {
        log.info("Создание нового бронирования");
        itemService.isItemExists(itemId);
        userService.isUserExists(userId);
        if (!itemService.isItemAvailable(itemId)) {
            throw new ValidationException("Вещь недоступна к бронированию");
        }
        booking.setBooker(userService.getUserById(userId));
        booking.setItem(itemService.getItem(itemId));
        if (isUserAnItemOwner(userId, booking)) {
            throw new NotFoundException("Владелец не может бронировать собственную вещь");
        }
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void updateBooking(long bookingId, long userId, boolean approval) {
        log.info("Обновление разрешения бронирования");
        Booking booking = getBookingById(bookingId);
        if (!isUserAnItemOwner(userId, booking)) {
            throw new NotFoundException("Пользователь не является владельцем!");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Нельзя сменить статус из другого статуса");
        }
        if (approval == true) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
    }


    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(long bookingId) {
        log.info("Получение объекта бронирования с id " + bookingId);
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NotFoundException("Бронь c Id " + bookingId + " не найдена!");
        }
        return bookingOptional.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(long bookingId, long userId) {
        log.info("Получение объекта бронирования с id " + bookingId);
        Booking booking = getBookingById(bookingId);
        userService.isUserExists(userId);
        if (!(isUserAnItemOwner(userId, booking) || isUserAnItemBooker(userId, booking))) {
            throw new NotFoundException("Доступ к предмету ограничен хозяином или резерватором");
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookingOfUserWithState(long userId, String state, int from,
        int size) {
        log.info("Получить все данные о бронированиях текущего пользователя");
        if (!userService.isUserExists(userId)) {
            throw new NotFoundException("Пользователь не был найден");
        }
        State curState = State.convert(state);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        switch (curState) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByBookingStartDesc(userId, page);
            case PAST:
                return bookingRepository
                    .findAllByBookerIdAndBookingEndIsBeforeOrderByBookingStartDesc(userId,
                        LocalDateTime.now(), page);
            case FUTURE:
                return bookingRepository
                    .findAllByBookerIdAndBookingStartIsAfterOrderByBookingStartDesc(userId,
                        LocalDateTime.now(), page);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                    LocalDateTime.now(), page);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByBookingStartDesc(userId,
                    Status.WAITING, page);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByBookingStartDesc(userId,
                    Status.REJECTED, page);
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookingForItemsOfOwnerWithState(long userId, String state, int from,
        int size) {
        log.info("Получить все данные о бронированиях владельца");
        if (!userService.isUserExists(userId)) {
            throw new NotFoundException("Пользователь не был найден");
        }
        if (!isUserAnOwner(userId)) {
            throw new NotFoundException("Не найдено предметов у пользователя");
        }
        State curState = State.convert(state);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        switch (curState) {
            case ALL:
                return bookingRepository.findAllByOwnerId(userId, page);
            case PAST:
                return bookingRepository
                    .findAllByOwnerIdAndEndBefore(userId, LocalDateTime.now(), page);
            case FUTURE:
                return bookingRepository
                    .findAllByOwnerIdAndStartAfter(userId, LocalDateTime.now(), page);
            case CURRENT:
                return bookingRepository
                    .findAllByOwnerIdAndStartAfterAndEndBefore(userId, LocalDateTime.now(), page);
            case WAITING:
                return bookingRepository.findAllByOwnerIdAndState(userId, Status.WAITING, page);
            case REJECTED:
                return bookingRepository.findAllByOwnerIdAndState(userId, Status.REJECTED, page);
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    @Override
    public Booking getItemLastBooking(long itemId) {
        List<Booking> bookings = bookingRepository
            .findAllByItemAndStatePast(itemId, LocalDateTime.now(), Status.REJECTED);
        if (bookings.size() == 0 || bookings.isEmpty()) {
            return null;
        }
        return bookings.get(0);
    }

    @Override
    public Booking getItemNextBooking(long itemId) {
        List<Booking> bookings = bookingRepository
            .findAllByItemAndStateFuture(itemId, LocalDateTime.now(), Status.REJECTED);
        if (bookings.size() == 0 || bookings.isEmpty()) {
            return null;
        }
        return bookings.get(0);
    }

    private boolean isUserAnOwner(long userId) {
        List<Booking> bookings = bookingRepository.findAll();
        for (Booking booking : bookings) {
            if (isUserAnItemOwner(userId, booking)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUserAnItemOwner(long userId, Booking booking) {
        return booking.getItem().getUserId() == userId ? true : false;
    }

    private boolean isUserAnItemBooker(long userId, Booking booking) {
        return booking.getBooker().getId() == userId ? true : false;
    }

}
