package com.portfolio.luisfmdc.sboot_ecobint_api.service.impl;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.NewBinRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import com.portfolio.luisfmdc.sboot_ecobint_api.config.exception.BinNotFoundException;
import com.portfolio.luisfmdc.sboot_ecobint_api.mapper.BinMapper;
import com.portfolio.luisfmdc.sboot_ecobint_api.repository.BinRepository;
import com.portfolio.luisfmdc.sboot_ecobint_api.service.BinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BinServiceImpl implements BinService {

    private final BinRepository binRepository;
    private final BinMapper binMapper;

    @Override
    public Bin postNewBin(NewBinRequest newBinRequest) {
        Bin bin = binMapper.toBin(newBinRequest);
        return binRepository.save(bin);
    }

    @Override
    public void postBinStatus(String binId, BinStatusRequest binStatusRequest) {
        binRepository.findById(binId).orElseThrow(() -> new BinNotFoundException("Lixeira não encontrada com id: " + binId));
        long updated = binRepository.updateStatus(binId, binStatusRequest.getPorcentagemEnchimento(), LocalDateTime.now());
        if (updated == 0) throw new RuntimeException("Houve um erro ao atualizar o status da lixeira com id: " + binId);
    }

    @Override
    public Bin getBin(String binId) {
        return binRepository.findById(binId)
                .orElseThrow(() -> new BinNotFoundException("Lixeira não encontrada com id: " + binId));
    }

    @Override
    public List<Bin> getAllBins() {
        return binRepository.findAll();
    }
}