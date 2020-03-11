package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	private static double arroundNumber(double number, double decimal) {
		return ((Math.round(number * decimal)) / decimal);
	}

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCar() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(ticket.getPrice(), arroundNumber((1.0 - Fare.FREE_DURATION) * Fare.CAR_RATE_PER_HOUR, 100.0));
	}

	@Test
	public void calculateFareBike() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(ticket.getPrice(), arroundNumber((1.0 - Fare.FREE_DURATION) * Fare.BIKE_RATE_PER_HOUR, 100.0));
	}

	@Test
	public void calculateFareUnkownType() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket,fidelity));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,fidelity));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
															// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(arroundNumber((0.75 - Fare.FREE_DURATION) * Fare.BIKE_RATE_PER_HOUR, 100.0), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithMoreThanADayParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(arroundNumber((24 - Fare.FREE_DURATION) * Fare.BIKE_RATE_PER_HOUR, 100.0), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanThirtyMinutesParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));// 45 minutes parking time should give 3/4th
															// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(0.0, ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
															// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(arroundNumber((0.75 - Fare.FREE_DURATION) * Fare.CAR_RATE_PER_HOUR, 100.0), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(arroundNumber((24 - Fare.FREE_DURATION) * Fare.CAR_RATE_PER_HOUR, 100.0), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
		boolean fidelity = false;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));// 45 minutes parking time should give 3/4th
															// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket,fidelity);
		assertEquals(0.0, ticket.getPrice());
	}
	
	 @Test
	    public void calculateFareCarWithFidelity(){
		   boolean fidelity = true; 
	        Date inTime = new Date();
	        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
	        Date outTime = new Date();
	        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
	        ticket.setInTime(inTime);
	        ticket.setOutTime(outTime);
	        ticket.setParkingSpot(parkingSpot);
	        fareCalculatorService.calculateFare(ticket,fidelity);
	        assertEquals(ticket.getPrice(), arroundNumber((1 - Fare.FREE_DURATION) * Fare.CAR_RATE_PER_HOUR_WITH_FIDELITY,100.0));
	    }   
	    
	    @Test
	    public void calculateFareBikeWithFidelity(){
		   boolean fidelity = true; 
	        Date inTime = new Date();
	        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
	        Date outTime = new Date();
	        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
	        ticket.setInTime(inTime);
	        ticket.setOutTime(outTime);
	        ticket.setParkingSpot(parkingSpot);
	        fareCalculatorService.calculateFare(ticket,fidelity);
	        assertEquals(ticket.getPrice(), arroundNumber((1 - Fare.FREE_DURATION) * Fare.BIKE_RATE_PER_HOUR_WITH_FIDELITY,100.0));
	    }   
}
