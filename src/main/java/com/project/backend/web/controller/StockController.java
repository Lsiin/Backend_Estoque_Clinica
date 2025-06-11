package com.project.backend.web.controller;

import com.project.backend.dto.StockDTO;
import com.project.backend.entities.Stock;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.StockRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

@Operation(summary = "Mostra o estoque",
    responses = {
        @ApiResponse(responseCode = "200", description = "Estoque mostrado",
                content = @Content(mediaType = "application/Json", schema = @Schema(implementation = StockDTO.class))),
        @ApiResponse(responseCode = "401", description = "Estoque vazio",
                content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
        @ApiResponse(responseCode = "500", description = "Recurso não processado por dados de entrada inválido",
                content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),

    })

    @GetMapping("/all")
    public List<StockDTO> findAll() {
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            throw new GlobalExceptionHandler.StockNotFoundException("Estoque vazio");
        }
        return stocks.stream().map(stock -> new StockDTO(stock)).collect(Collectors.toList());
    }


}
