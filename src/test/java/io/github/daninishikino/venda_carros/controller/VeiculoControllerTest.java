package io.github.daninishikino.venda_carros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.daninishikino.venda_carros.controller.DTO.request.VeiculoDTO;
import io.github.daninishikino.venda_carros.service.VeiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VeiculoService veiculoService;

    @Test
    @DisplayName("Deve listar todos os veículos")
    @WithMockUser(roles = "GERENTE")
    public void deveListarTodosVeiculos() throws Exception {
        VeiculoDTO veiculo1 = new VeiculoDTO(
                UUID.randomUUID(),
                "Fiat",
                "Uno",
                2020,
                2021,
                "Branco",
                "ABC1234",
                new BigDecimal("35000.00"),
                "Descrição do veículo 1",
                true,
                null
        );

        VeiculoDTO veiculo2 = new VeiculoDTO(
                UUID.randomUUID(),
                "Volkswagen",
                "Gol",
                2019,
                2020,
                "Preto",
                "DEF5678",
                new BigDecimal("32000.00"),
                "Descrição do veículo 2",
                true,
                null
        );

        List<VeiculoDTO> veiculos = Arrays.asList(veiculo1, veiculo2);
        when(veiculoService.buscarTodos()).thenReturn(veiculos);

        mockMvc.perform(get("/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Fiat"))
                .andExpect(jsonPath("$[0].modelo").value("Uno"))
                .andExpect(jsonPath("$[1].marca").value("Volkswagen"))
                .andExpect(jsonPath("$[1].modelo").value("Gol"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve buscar veículo por placa")
    public void deveBuscarVeiculoPorPlaca() throws Exception {

        String placa = "ABC1234";
        VeiculoDTO veiculoDTO = new VeiculoDTO(
                UUID.randomUUID(),
                "Fiat",
                "Uno",
                2020,
                2021,
                "Branco",
                placa,
                new BigDecimal("35000.00"),
                "Descrição do veículo",
                true,
                null
        );

        when(veiculoService.buscarPelaPlaca(placa)).thenReturn(veiculoDTO);

        mockMvc.perform(get("/veiculos/{placa}", placa))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Fiat"))
                .andExpect(jsonPath("$.modelo").value("Uno"))
                .andExpect(jsonPath("$.placa").value(placa));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Deve negar acesso ao buscar por placa para cliente")
    public void deveNegarAcessoAoBuscarPorPlacaParaCliente() throws Exception {
        mockMvc.perform(get("/veiculos/ABC1234"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve criar um veículo com sucesso")
    public void deveCriarVeiculoComSucesso() throws Exception {

        VeiculoDTO veiculoDTO = new VeiculoDTO(
                null,
                "Fiat",
                "Uno",
                2020,
                2021,
                "Branco",
                "ABC1234",
                new BigDecimal("35000.00"),
                "Descrição do veículo",
                true,
                null
        );

        doNothing().when(veiculoService).salvar(any(VeiculoDTO.class));

        // Act & Assert
        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve atualizar um veículo com sucesso")
    public void deveAtualizarVeiculoComSucesso() throws Exception {
        String placa = "ABC1234";
        VeiculoDTO veiculoDTO = new VeiculoDTO(
                UUID.randomUUID(),
                "Fiat",
                "Uno",
                2020,
                2021,
                "Vermelho", // Cor alterada
                placa,
                new BigDecimal("36000.00"), // Preço alterado
                "Descrição atualizada",
                true,
                null
        );

        when(veiculoService.atualizar(eq(placa), any(VeiculoDTO.class))).thenReturn(veiculoDTO);

        mockMvc.perform(put("/veiculos/{placa}", placa)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cor").value("Vermelho"))
                .andExpect(jsonPath("$.preco").value(36000.00));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve deletar um veículo com sucesso")
    public void deveDeletarVeiculoComSucesso() throws Exception {
        String placa = "ABC1234";
        doNothing().when(veiculoService).deletar(placa);

        mockMvc.perform(delete("/veiculos/{placa}", placa))
                .andExpect(status().isNoContent());
    }
}
