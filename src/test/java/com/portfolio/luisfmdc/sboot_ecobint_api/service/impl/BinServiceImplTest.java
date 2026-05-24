package com.portfolio.luisfmdc.sboot_ecobint_api.service.impl;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.config.exception.BinNotFoundException;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import com.portfolio.luisfmdc.sboot_ecobint_api.repository.BinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinServiceImplTest {

    @Mock
    private BinRepository binRepository;

    @InjectMocks
    private BinServiceImpl binService;

    private Bin bin;
    private BinStatusRequest binStatusRequest;

    @BeforeEach
    void setUp() {
        bin = new Bin("1", "Bin 1", "Location 1", 50.0, LocalDateTime.now());
        binStatusRequest = new BinStatusRequest();
        binStatusRequest.setPorcentagemEnchimento(75.0);
    }

    @Test
    void postBinStatus_shouldUpdateBinStatus_whenBinExists() {
        when(binRepository.findById(anyString())).thenReturn(Optional.of(bin));
        when(binRepository.updateStatus(anyString(), any(Double.class), any(LocalDateTime.class))).thenReturn(1L);

        assertDoesNotThrow(() -> binService.postBinStatus("1", binStatusRequest));

        verify(binRepository, times(1)).findById("1");
        verify(binRepository, times(1)).updateStatus(eq("1"), eq(75.0), any(LocalDateTime.class));
    }

    @Test
    void postBinStatus_shouldThrowBinNotFoundException_whenBinDoesNotExist() {
        when(binRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BinNotFoundException.class, () -> binService.postBinStatus("1", binStatusRequest));

        verify(binRepository, times(1)).findById("1");
        verify(binRepository, never()).updateStatus(anyString(), any(Double.class), any(LocalDateTime.class));
    }

    @Test
    void postBinStatus_shouldThrowRuntimeException_whenUpdateFails() {
        when(binRepository.findById(anyString())).thenReturn(Optional.of(bin));
        when(binRepository.updateStatus(anyString(), any(Double.class), any(LocalDateTime.class))).thenReturn(0L);

        assertThrows(RuntimeException.class, () -> binService.postBinStatus("1", binStatusRequest));

        verify(binRepository, times(1)).findById("1");
        verify(binRepository, times(1)).updateStatus(eq("1"), eq(75.0), any(LocalDateTime.class));
    }

    @Test
    void getBin_shouldReturnBin_whenBinExists() {
        when(binRepository.findById(anyString())).thenReturn(Optional.of(bin));

        Bin foundBin = binService.getBin("1");

        assertNotNull(foundBin);
        assertEquals("1", foundBin.getId());
        verify(binRepository, times(1)).findById("1");
    }

    @Test
    void getBin_shouldThrowBinNotFoundException_whenBinDoesNotExist() {
        when(binRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(BinNotFoundException.class, () -> binService.getBin("1"));
        verify(binRepository, times(1)).findById("1");
    }
}