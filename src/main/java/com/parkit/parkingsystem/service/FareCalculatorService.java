package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * This class calculate the ticket price according to the vehicle in entrance and the parking lot duration
 * @param ticket - parking slot ticket
 */
public class FareCalculatorService {
	
	/**
	 * 
	 * @param number - the number to round 
	 * @param decimal - the nearest one-hundredth, or thousandth, or.... 
	 * @return double - the rounded number
	 */
	private static double arroundNumber(double number, double decimal){
		return ((Math.round(number*decimal))/decimal);
		
	}	

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        /* the double format is necessary for calculating  with precision   */
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        double duration = arroundNumber((outHour - inHour)/(1000*60*60),1000.0);	

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(arroundNumber(duration * Fare.CAR_RATE_PER_HOUR,100.0));
                break;
            }
            case BIKE: {
                ticket.setPrice(arroundNumber(duration * Fare.BIKE_RATE_PER_HOUR,100.0));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}