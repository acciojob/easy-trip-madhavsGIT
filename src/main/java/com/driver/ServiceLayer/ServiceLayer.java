package com.driver.ServiceLayer;

import com.driver.RepositoryLayer.RepositoryLayer;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ServiceLayer {



    RepositoryLayer repoobject = new RepositoryLayer();

    public String addairport(Airport airport){
        String responce = repoobject.addAirport(airport);
        return responce;
    }

    public String addFlight(Flight flight){
        String res = repoobject.addflight(flight);
        return res;
    }

    public String addPassenger(Passenger passenger){
        String result = repoobject.addPassenger(passenger);
        return result;
    }

    public String getLargestAirport(){
        ArrayList<Airport> list = new ArrayList<>();

        list = repoobject.getallAirportslist();
        int mxterminals = 0;
        Airport ansairport = null;
        for(Airport airport : list){
            if(airport.getNoOfTerminals() > mxterminals) {
                mxterminals = airport.getNoOfTerminals();
                ansairport = airport;
            }
        }
        if(ansairport != null) return ansairport.getAirportName();
        return "No Airports Available";
    }

    public double getshortestdurationBtwcities(City city1, City city2){
        ArrayList<Flight> list = new ArrayList<>();
        list = repoobject.getallFlightslist();
        double shortestDuration = Integer.MAX_VALUE;
        for(Flight flight : list ){
            if(flight.getFromCity() == city1 && flight.getToCity() == city2){
                if ( flight.getDuration() < shortestDuration) {
                    shortestDuration = flight.getDuration();
                }
            }else{
                return -1;
            }
        }
        return shortestDuration;
    }



    public String getAirportNameByFlightId(int flightId){
        ArrayList<Flight> flightslist = new ArrayList<>();
        flightslist = repoobject.getallFlightslist();

        City anscity = null;
        for(Flight flight : flightslist){
            if(flight.getFlightId() == flightId){
                anscity = flight.getFromCity();
            }
        }
        ArrayList<Airport> airportslist = new ArrayList<>();
        airportslist = repoobject.getallAirportslist();

        for(Airport airport : airportslist){
            if(airport.getCity() == anscity){
                return airport.getAirportName();
            }
        }
        return "flightId is invalid";

    }


    public String bookTicket(int flightId, int passengerId){
        Flight flight = repoobject.getFlightByFlightId(flightId);
        Passenger passenger = repoobject.getPassengerByPassengerId(passengerId);

        if(flight == null || passenger == null) return "flight / passenger does not exist";

        int numberofticketsbookedforflightId = repoobject.noOfTicketsBookedForFlightId(flightId);
        if(numberofticketsbookedforflightId >= flight.getMaxCapacity()){
            return "FAILURE";
        }

        return repoobject.bookTicket(flightId, passengerId);

    }

    public int calculateFare(int flightId){
        int numberOfTicketsBookedForFlightId = repoobject.noOfTicketsBookedForFlightId(flightId);
        int price = 3000 + numberOfTicketsBookedForFlightId * 50;

        return price;
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        int count = repoobject.countOfBookingsDoneByPassengerAllCombined(passengerId);
        return count;
    }

    public String cancelATicket(int flightId, int passengerId){
        String result = repoobject.cancelATicket(flightId, passengerId);
        return result;
    }

    public int calculateRevenueCollectedByFlight(int flightId){
        int price = calculateFare(flightId);
        int canceledTickes = repoobject.getCancelTickets(flightId);
        int canceledPrice = canceledTickes * 50;
        return price - canceledPrice;
    }



    public int getNumberOfpeople(Date date, String airportname){
        ArrayList<Airport> airportslist = new ArrayList<>();
        airportslist = repoobject.getallAirportslist();

        City city = null;

        for(Airport airport : airportslist){
            if(airport.getAirportName() == airportname){
                city = airport.getCity();
            }
        }

        ArrayList<Flight> flightslist = new ArrayList<>();
        flightslist = repoobject.getallFlightslist();

        if(flightslist.size() == 0) return 0;

        ArrayList<Flight> flights = new ArrayList<>();
        for(Flight flight : flightslist){
            if(flight.getFlightDate() == date){
                flights.add(flight);
            }
        }
        int count = 0;
        for(Flight flight : flights){
            count += repoobject.noOfTicketsBookedForFlightId(flight.getFlightId());
        }

        return count;

    }
}
