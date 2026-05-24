package com.portfolio.luisfmdc.sboot_ecobint_api.controller;

import com.portfolio.luisfmdc.ecobint.infrastructure.api.BinApi;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinResponse;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import com.portfolio.luisfmdc.sboot_ecobint_api.mapper.BinMapper;
import com.portfolio.luisfmdc.sboot_ecobint_api.service.BinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BinController implements BinApi {

    private final BinService binService;
    private final BinMapper binMapper;

    @Override
    public ResponseEntity<BinResponse> getBin(String binId) {
        log.info("[BinController] Iniciando busca de dados da lixeira id={}.", binId);
        Bin bin = binService.getBin(binId);
        log.info("[BinController] Dados da lixeira id={} obtidos com sucesso.", binId);
        return ResponseEntity.ok(binMapper.toBinResponse(bin));
    }

    @Override
    public ResponseEntity<Void> postBinStatus(String binId, BinStatusRequest binStatusRequest) {
        log.info("[BinController] Iniciando atualização do status da lixeira id={}.", binId);
        binService.postBinStatus(binId, binStatusRequest);
        log.info("[BinController] Status da lixeira id={} atualizado com sucesso.", binId);
        return ResponseEntity.noContent().build();
    }
}