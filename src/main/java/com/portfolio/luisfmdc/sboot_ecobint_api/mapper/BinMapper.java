package com.portfolio.luisfmdc.sboot_ecobint_api.mapper;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinResponse;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BinMapper {

    BinResponse toBinResponse(Bin bin);
    List<BinResponse> toBinResponseList(List<Bin> binList);
}