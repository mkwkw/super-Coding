package com.github.supercoding.web.controller;

import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exception.InvalidValueException;
import com.github.supercoding.service.exception.NotAcceptException;
import com.github.supercoding.service.exception.NotFoundException;
import com.github.supercoding.web.dto.airline.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/air-reservation")
@RequiredArgsConstructor
@Slf4j
public class AirReservationController {

    private final AirReservationService airReservationService;

    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets(@RequestParam("user-Id") Integer userId,

                                             @RequestParam("airline-ticket-type") String ticketType ){

            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
            return new TicketResponse(tickets);

        //NotAcceptException, NotFoundException 발생하면 ExceptionControllerAdvice가 처리할 것이다!
    }

    @ApiOperation("User와 Ticket ID로 예약 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest) {

        return airReservationService.makeReservation(reservationRequest);

        //NotAcceptException, NotFoundException 발생하면 ExceptionControllerAdvice가 처리할 것이다!
    }

    @ApiOperation("userId의 예약한 항공편과 수수료 총합")
    @GetMapping("/users-sum-price")
    public Double findUserFlightPrice(
            @ApiParam(name = "user-Id", value = "유저 ID", example = "1") @RequestParam("user-id") Integer userId
    ){
        Double sum = airReservationService.findUserFlightSumPrice(userId);
        return sum;
    }

    @ApiOperation("user id, ticket id에 해당하는 항공권 결제")
    @PostMapping("/payments")
    public String payTickets(@RequestBody PaymentRequest paymentRequest){
        return "요청하신 결제 중 "+airReservationService.payTickets(paymentRequest)+"건 진행완료 되었습니다.";
    }
}
