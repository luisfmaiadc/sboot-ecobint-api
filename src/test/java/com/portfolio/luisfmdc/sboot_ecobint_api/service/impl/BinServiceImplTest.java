package com.portfolio.luisfmdc.sboot_ecobint_api.service.impl;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.NewBinRequest;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.UpdateBinRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.config.exception.BinNotFoundException;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import com.portfolio.luisfmdc.sboot_ecobint_api.mapper.BinMapper;
import com.portfolio.luisfmdc.sboot_ecobint_api.repository.BinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinServiceImplTest {

    @Mock
    private BinRepository binRepository;

    @Mock
    private BinMapper binMapper;

    @InjectMocks
    private BinServiceImpl binService;

    private Bin bin;
    private BinStatusRequest binStatusRequest;
    private NewBinRequest newBinRequest;
    private UpdateBinRequest updateBinRequest;

    @BeforeEach
    void setUp() {
        bin = new Bin("1", "Bin 1", "Location 1", 50.0, LocalDateTime.now());
        
        binStatusRequest = new BinStatusRequest();
        binStatusRequest.setPorcentagemEnchimento(75.0);

        newBinRequest = new NewBinRequest();
        newBinRequest.setNome("New Bin");
        newBinRequest.setLocalizacao("New Location");

        updateBinRequest = new UpdateBinRequest();
        updateBinRequest.setNome("Bin 1 Updated");
        updateBinRequest.setLocalizacao("Location 1 Updated");
    }

    @Test
    void postNewBin_shouldReturnSavedBin() {
        Bin unpersistedBin = new Bin(null, "New Bin", "New Location", null, null);
        when(binMapper.toBin(any(NewBinRequest.class))).thenReturn(unpersistedBin);
        when(binRepository.save(any(Bin.class))).thenReturn(new Bin("2", "New Bin", "New Location", 0.0, LocalDateTime.now()));

        Bin savedBin = binService.postNewBin(newBinRequest);

        assertNotNull(savedBin);
        assertEquals("2", savedBin.getId());

        verify(binMapper, times(1)).toBin(newBinRequest);
        verify(binRepository, times(1)).save(unpersistedBin);
    }

    @Test
    void updateBin_shouldUpdateAndReturnBin_whenBinExists() {
        when(binRepository.findById(anyString())).thenReturn(Optional.of(bin));
        when(binRepository.save(any(Bin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bin updatedBin = binService.updateBin("1", updateBinRequest);

        assertNotNull(updatedBin);
        assertEquals("Bin 1 Updated", updatedBin.getNome());
        assertEquals("Location 1 Updated", updatedBin.getLocalizacao());

        verify(binRepository, times(1)).findById("1");
        verify(binRepository, times(1)).save(any(Bin.class));
    }

    @Test
    void updateBin_shouldThrowBinNotFoundException_whenBinDoesNotExist() {
        when(binRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BinNotFoundException.class, () -> binService.updateBin("1", updateBinRequest));

        verify(binRepository, times(1)).findById("1");
        verify(binRepository, never()).save(any(Bin.class));
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

    @Test
    void getAllBins_shouldReturnListOfBins() {
        when(binRepository.findAll()).thenReturn(List.of(bin));

        List<Bin> bins = binService.getAllBins();

        assertNotNull(bins);
        assertFalse(bins.isEmpty());
        assertEquals(1, bins.size());
        assertEquals("1", bins.getFirst().getId());
        verify(binRepository, times(1)).findAll();
    }
}