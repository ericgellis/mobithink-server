package com.mobithink.server.entity;

import javax.persistence.*;

/**
 * Created by athiel on 01/02/2017.
 *
 */

@Entity
public class StationData {

    @Id
    @SequenceGenerator(name = "mobithink_uid", sequenceName = "mobithink_uid", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mobithink_uid")
    @Column(name="id")
    private Long id;

    @Column(name = "come_in")
    private int numberOfComeIn;

    @Column(name = "go_out")
    private int numberOfGoOut;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "step")
    private int stationStep;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "gps_lat")
    private Double gpsLat;

    @Column(name = "gps_long")
    private Double gpsLong;

    @Column(name = "speed")
    private Double speed;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Double getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(Double gpsLong) {
        this.gpsLong = gpsLong;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfComeIn() {
        return numberOfComeIn;
    }

    public void setNumberOfComeIn(int numberOfComeIn) {
        this.numberOfComeIn = numberOfComeIn;
    }

    public int getNumberOfGoOut() {
        return numberOfGoOut;
    }

    public void setNumberOfGoOut(int numberOfGoOut) {
        this.numberOfGoOut = numberOfGoOut;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getStationStep() {
        return stationStep;
    }

    public void setStationStep(int stationStep) {
        this.stationStep = stationStep;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "StationData{" +
                "id=" + id +
                ", numberOfComeIn=" + numberOfComeIn +
                ", numberOfGoOut=" + numberOfGoOut +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", stationStep=" + stationStep +
                ", stationName='" + stationName + '\'' +
                ", gpsLat=" + gpsLat +
                ", gpsLong=" + gpsLong +
                ", trip=" + trip +
                '}';
    }
}
