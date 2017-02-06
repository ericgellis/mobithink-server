package com.mobithink.server.facade;

import com.mobithink.server.DTO.*;
import com.mobithink.server.entity.*;
import com.mobithink.server.exeption.MobithinkBusinessException;
import com.mobithink.server.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by athiel on 03/02/2017.
 */

@RestController
@RequestMapping(value = "/mobithink/trip" )
public class TripFacade {

    @Resource
    TripService tripService;

    @Resource
    StationDataService stationDataService;

    @Resource
    EventService eventService;

    @Resource
    RollingPointService rollingPointService;


    /**
     *
     * POST. Register a new TripDTO.
     *
     * @param tripDTO object
     *
     * @return text : "success" if save tripDTO
     *
     */
    @PostMapping(path = "/create", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> create(@Valid @RequestBody TripDTO tripDTO) throws MobithinkBusinessException {

        Trip savedTrip = saveNewTrip(tripDTO);
        List<StationData> stationDataList = saveStationDataList(savedTrip, tripDTO.getStationDataDTOList());
        saveEventList(savedTrip, tripDTO.getEventDTOList(), stationDataList);
        saveRollingPointList(savedTrip, tripDTO.getRollingPointDTOList());

        return ResponseEntity.ok("success");
    }

    /**
     *
     * GET. find TripDTO associeted at city and line.
     *
     * @param cityName and lineName
     *
     * @return List<TripDTO> or null if not exist Trip saved for this busLine
     *
     */
    @GetMapping(path = "/find/{cityName}/{lineName}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<List<TripDTO>> create(@Valid @PathVariable String cityName , @PathVariable String lineName) throws MobithinkBusinessException {

        String busLine = lineName;
        String city = cityName;
        List<Trip> tripList = tripService.findTripListByBusLineNameAndcityName(lineName, cityName);
        List<TripDTO> tripDTOList = new ArrayList<>();

        if (tripList != null){
            for (Trip trip : tripList){
                TripDTO tripDTO = createTripDTO(trip);
                tripDTOList.add(tripDTO);
            }
            return ResponseEntity.ok(tripDTOList);
        } else return null;
    }


    private TripDTO createTripDTO(Trip trip) {

        List<StationDataDTO> stationDataDTOList = stationDataService.findAllStationDataDtoByTripId(trip.getId());
        List<EventDTO> eventDTOList = eventService.findAllEventDTObyTripId(trip.getId());
        List<RollingPointDTO> rollingPointDTOList = rollingPointService.findRollingPOintDtoListByTripId(trip.getId());

        TripDTO tripDTO = ConverterOfDTO.convertTripToTripDTO(trip, stationDataDTOList, eventDTOList, rollingPointDTOList);

        return tripDTO;
    }


    private void saveRollingPointList(Trip savedTrip, List<RollingPointDTO> rollingPointDTOList) {

        for (RollingPointDTO rollingPointDto : rollingPointDTOList){
            RollingPoint rollingPoint = new RollingPoint();
            rollingPoint.setTrip(savedTrip);
            rollingPoint.setGpsLat(rollingPointDto.getGpsLat());
            rollingPoint.setGpsLong(rollingPointDto.getGpsLong());
            rollingPoint.setTimeOfRollingPoint(rollingPointDto.getPointTime());
            rollingPoint.setTraffic(rollingPointDto.getTrafficIndex());

            rollingPointService.createRollingPoint(rollingPoint);
        }
    }

    private void saveEventList(Trip savedTrip, List<EventDTO> eventDTOList, List<StationData> stationDataList) {
        Event event = new Event();
        event.setTrip(savedTrip);
        for (EventDTO eventDto : eventDTOList) {
            event.setEndTime(eventDto.getEndTime());
            event.setStartTime(eventDto.getStartTime());
            event.setEventName(eventDto.getEventName());
            if (eventDto.getStationName() != null) {
                for (StationData stationData : stationDataList){
                    if(stationData.getStationName().equals(eventDto.getStationName())){
                        event.setStationData(stationData);
                    }
                }
            }
            event.setGpsLat(eventDto.getGpsLat());
            event.setGpsLong(eventDto.getGpsLong());

            eventService.createEvent(event);
        }
    }

    private List<StationData> saveStationDataList(Trip savedTrip, List<StationDataDTO> stationDataDTOList) {
        int counter = 0;

        List<StationData> stationDataList = new ArrayList<>();

        for (StationDataDTO stationDataDTO : stationDataDTOList ){

            StationData stationData = new StationData();
            stationData.setStationName(stationDataDTO.getStationName());
            stationData.setEndTime(stationDataDTO.getEndTime());
            stationData.setStartTime(stationDataDTO.getStartTime());
            stationData.setNumberOfComeIn(stationDataDTO.getNumberOfComeIn());
            stationData.setNumberOfGoOut(stationDataDTO.getNumberOfGoOut());
            stationData.setGpsLat(stationDataDTO.getGpsLat());
            stationData.setGpsLong(stationDataDTO.getGpsLong());
            stationData.setStationStep(counter);
            stationData.setTrip(savedTrip);

            stationDataList.add(stationDataService.createStationData(stationData));
            counter++;
        }
        return stationDataList;
    }

    private Trip saveNewTrip(TripDTO tripDTO) {

        Trip savedTrip = new Trip();
        savedTrip.setWeather(tripDTO.getWeather());
        savedTrip.setAtmo(tripDTO.getAtmo());
        savedTrip.setBusLineName(tripDTO.getBusLineDtoName());
        savedTrip.setCityName(tripDTO.getCityDtoName());
        savedTrip.setStartGpsLat(tripDTO.getStartGpsLat());
        savedTrip.setStartGpsLong(tripDTO.getStartGpsLong());
        savedTrip.setEndGpsLat(tripDTO.getEndGpsLat());
        savedTrip.setEndGpsLong(tripDTO.getEndGpsLong());
        savedTrip.setStartTime(tripDTO.getStartTime());
        savedTrip.setEndTime(tripDTO.getEndTime());
        savedTrip.setTemperature(tripDTO.getTemperature());
        savedTrip.setTripName(tripDTO.getTripName());
        savedTrip.setVehicleCapacity(tripDTO.getVehicleCapacity());

        return tripService.createTrip(savedTrip);
    }
}
