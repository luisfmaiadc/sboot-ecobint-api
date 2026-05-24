package com.portfolio.luisfmdc.sboot_ecobint_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinResponse;
import com.portfolio.luisfmdc.ecobint.infrastructure.dto.BinStatusRequest;
import com.portfolio.luisfmdc.sboot_ecobint_api.config.exception.BinNotFoundException;
import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import com.portfolio.luisfmdc.sboot_ecobint_api.mapper.BinMapper;
import com.portfolio.luisfmdc.sboot_ecobint_api.service.BinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BinController.class)
class BinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BinService binService;

    @MockitoBean
    private BinMapper binMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Bin bin;
    private BinResponse binResponse;
    private BinStatusRequest binStatusRequest;

    @BeforeEach
    void setUp() {
        bin = new Bin("1", "Bin 1", "Location 1", 50.0, LocalDateTime.now());
        binResponse = new BinResponse();
        binResponse.setId("1");
        binResponse.setNome("Bin 1");
        binResponse.setLocalizacao("Location 1");
        binResponse.setNivelEnchimento(50.0);
        binResponse.setUltimaAtualizacao(bin.getUltimaAtualizacao());

        binStatusRequest = new BinStatusRequest();
        binStatusRequest.setPorcentagemEnchimento(0.75);
    }

    @Test
    void getBin_shouldReturnBinResponse_whenBinExists() throws Exception {
        when(binService.getBin(anyString())).thenReturn(bin);
        when(binMapper.toBinResponse(any(Bin.class))).thenReturn(binResponse);

        mockMvc.perform(get("/bin/{binId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Bin 1"));

        verify(binService, times(1)).getBin("1");
        verify(binMapper, times(1)).toBinResponse(bin);
    }

    @Test
    void getBin_shouldReturnNotFound_whenBinDoesNotExist() throws Exception {
        when(binService.getBin(anyString())).thenThrow(new BinNotFoundException("Lixeira não encontrada"));

        mockMvc.perform(get("/bin/{binId}", "1"))
                .andExpect(status().isNotFound());

        verify(binService, times(1)).getBin("1");
        verify(binMapper, never()).toBinResponse(any());
    }

    @Test
    void postBinStatus_shouldReturnNoContent_whenRequestIsValid() throws Exception {
        doNothing().when(binService).postBinStatus(anyString(), any(BinStatusRequest.class));

        mockMvc.perform(post("/bin/{binId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(binStatusRequest)))
                .andExpect(status().isNoContent());

        verify(binService, times(1)).postBinStatus(eq("1"), any(BinStatusRequest.class));
    }

    @Test
    void postBinStatus_shouldReturnBadRequest_whenRequestBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/bin/{binId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(binService, never()).postBinStatus(anyString(), any());
    }
}