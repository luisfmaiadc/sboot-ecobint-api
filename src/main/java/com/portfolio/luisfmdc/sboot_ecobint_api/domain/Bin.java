package com.portfolio.luisfmdc.sboot_ecobint_api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Bin")
public class Bin {

    @Id
    private String id;
    private String nome;
    private String localizacao;
    private Double nivelEnchimento;
    private LocalDateTime ultimaAtualizacao;
}