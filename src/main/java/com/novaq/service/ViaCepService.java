package com.novaq.service;

import com.novaq.dtos.response.ViaCepResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ViaCepService {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://viacep.com.br/ws")
            .build();

    public ViaCepResponseDTO findAddressByPostalCode(String postalCode) {

        if (postalCode == null || postalCode.isBlank()){
            throw new IllegalArgumentException("Postal code cannot be empty");
        }

        String cleanPostalCode = postalCode.replaceAll("\\D", "");

        try{

            return restClient.get()
                    .uri("/{postalCode}/json", cleanPostalCode)
                    .retrieve()
                    .body(ViaCepResponseDTO.class);

        }
        catch (Exception e) {
            throw new RuntimeException("Failed to integrate with ViaCEP API for postal code: " + postalCode, e);
        }
    }

}
