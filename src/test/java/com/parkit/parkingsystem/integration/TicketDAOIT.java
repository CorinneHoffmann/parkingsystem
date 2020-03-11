package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.sql.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class TicketDAOIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {

		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	}

	@Test
	public void verifyHistory() throws Exception {
		Integer count = 0;

		String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (26 * 60 * 60 * 1000)));
		ticket.setOutTime(new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setPrice(1.5);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);
		
		ticket.setInTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
		ticket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setPrice(1.5);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);

		count = ticketDAO.getHistory(vehicleRegNumber);
		assertTrue(count > 1);
	}
	
	@Test
	public void verifyGetTicketReturnTheMoreRecentLine() throws Exception {

		String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (3 * 60 * 60 * 1000)));
		ticket.setOutTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setPrice(1.5);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);

		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setOutTime(null);
		ticket.setParkingSpot(parkingSpot);
		ticket.setPrice(0.0);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);

		Ticket ticketRetour = ticketDAO.getTicket(vehicleRegNumber);
		//assertEquals(ticketRetour.getOutTime(), null);
		assertNull(ticketRetour.getOutTime());
	}
}