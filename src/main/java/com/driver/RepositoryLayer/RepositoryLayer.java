package com.driver.RepositoryLayer;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RepositoryLayer {

    HashMap<String, Airport> airportsDB = new HashMap<>();
    HashMap<Integer, Flight> flightsDB = new HashMap<>();
    HashMap<Integer, Passenger> passengersDB = new HashMap<>();

    HashMap<Integer, List<Integer>> flightPassengermap = new HashMap<>();
    HashMap<Integer, Integer> flightcancelmap = new HashMap<>();


    public String addAirport(Airport airport){
        String key = airport.getAirportName();

        airportsDB.put(key, airport);

        return "SUCCESS";
    }

    public String addflight(Flight flight){
        int key = flight.getFlightId();

        flightsDB.put(key, flight);
        return "SUCCESS";

    }

    public String addPassenger(Passenger passenger){
        int key = passenger.getPassengerId();
        passengersDB.put(key, passenger);
        return "SUCCESS";
    }


    public ArrayList<Airport> getallAirportslist(){
        ArrayList<Airport> airportslist = new ArrayList<>();
        for(Airport airport : airportsDB.values()){
            airportslist.add(airport);
        }
        return airportslist;
    }



    public Airport getAirportBYName(String airportName){
        for(Airport airport : airportsDB.values()){
            if(airport.getAirportName().equals(airportName)){
                return airport;
            }
        }
        return null;
    }



    public ArrayList<Flight> getallFlightslist(){
        ArrayList<Flight> flightslist = new ArrayList<>();

        for(Flight flight : flightsDB.values()){
            flightslist.add(flight);
        }
        return flightslist;
    }

    public Flight getFlightByFlightId(int flightId){
        for(Flight flight : flightsDB.values()){
            if(flight.getFlightId() == flightId){
                return flight;
            }
        }
        return null;
    }


    public ArrayList<Passenger> getallPassengers(){
        ArrayList<Passenger> passengerslist = new ArrayList<>();

        for(Passenger passenger : passengerslist){
            passengerslist.add(passenger);
        }
        return passengerslist;
    }

    public Passenger getPassengerByPassengerId(int passengerId){
        for(Passenger passenger : passengersDB.values()){
            if(passenger.getPassengerId() == passengerId){
                return passenger;
            }
        }
        return null;
    }


    public String bookTicket(int flightId, int passengerId){
        if(flightPassengermap.containsKey(flightId)){
            List<Integer> passengerList = flightPassengermap.get(flightId);
            if(passengerList.contains(passengerId)){
                return "FAILURE";
            }else{
                passengerList.add(passengerId);
                flightPassengermap.put(flightId, passengerList);
            }
        }else{
            List<Integer> newPassengerList = new ArrayList<>();
            newPassengerList.add(passengerId);
            flightPassengermap.put(flightId, newPassengerList);
        }
        return "SUCCESS";
    }

    public int noOfTicketsBookedForFlightId(int flightId){
        if(flightPassengermap.containsKey(flightId)){
            return flightPassengermap.get(flightId).size();
        }
        return 0;
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        int ct = 0;
        for(int flightId : flightPassengermap.keySet()){
            List<Integer> passengerList = flightPassengermap.get(flightId);
            for(int pId : passengerList){
                if(pId == passengerId){
                    ct++;
                }
            }
        }
        return ct;
    }

    public String cancelATicket(int flightId, int passengerId){

        List<Integer> pIds = flightPassengermap.get(flightId);
        if(!pIds.contains(passengerId)) return "FAILURE";
        List<Integer> newList = new ArrayList<>();
        int n = pIds.size();

        for(int i = 0; i < n; i++) {
            if( pIds.get(i) == passengerId) {
                newList.add(i);
            }
        }
        for(int i = 0; i < newList.size(); i++){
            int idx = newList.get(i);
            pIds.remove(idx);
        }
        flightcancelmap.put(flightId, flightcancelmap.getOrDefault(0, 1) + 1);

        return  "SUCCESS";
    }


    public int getCancelTickets(int flightId){
        if(flightcancelmap.containsKey(flightId)) {
            return flightcancelmap.get(flightId);
        }
        return 1;
    }
}
