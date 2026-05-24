package com.portfolio.luisfmdc.sboot_ecobint_api.mapper;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinResponse;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinMapper {

    BinResponse toBinResponse(Bin bin);
}