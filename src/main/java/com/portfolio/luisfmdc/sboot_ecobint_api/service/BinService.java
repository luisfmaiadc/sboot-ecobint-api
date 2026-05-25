package com.portfolio.luisfmdc.sboot_ecobint_api.service;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.NewBinRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;

import java.util.List;

public interface BinService {

    Bin postNewBin(NewBinRequest newBinRequest);
    void postBinStatus(String binId, BinStatusRequest binStatusRequest);
    Bin getBin(String binId);
    List<Bin> getAllBins();
}