package ua.stepess.microservices.pcdemo.service;

public interface BookingService<T> {

    T book(T booking);

    T find(Long bookingId);

}
