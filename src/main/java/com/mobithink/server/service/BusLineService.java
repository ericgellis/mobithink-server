package com.mobithink.server.service;

import com.mobithink.server.entity.BusLine;

import java.util.List;

/**
 * Created by athiel on 02/02/2017.
 */

public interface BusLineService {

    BusLine createBusLine(BusLine busLine);

    List<BusLine> findOneByCityId(Long id);

    BusLine findOneByName(String name);
}
