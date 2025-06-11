package io.github.daninishikino.venda_carros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.daninishikino.venda_carros.controller.DTO.request.VeiculoDTO;
import io.github.daninishikino.venda_carros.controller.DTO.request.VendaRequestDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.UsuarioResponseDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.VendaResponseDTO;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoDisponivelException;
import io.github.daninishikino.venda_carros.model.enums.Papel;
import io.github.daninishikino.venda_carros.service.VendaService;
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
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VendaService vendaService;

    @Test
    @WithMockUser(roles = "VENDEDOR")
    @DisplayName("Deve realizar uma venda com sucesso")
    public void deveRealizarVendaComSucesso() throws Exception {

        VendaRequestDTO vendaRequestDTO = new VendaRequestDTO("ABC1234");

        UUID vendaId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();

        VeiculoDTO veiculoDTO = new VeiculoDTO(
                veiculoId,
                "Fiat",
                "Uno",
                2020,
                2021,
                "Branco",
                "ABC1234",
                new BigDecimal("35000.00"),
                "Descrição do veículo",
                false, // não disponível após venda
                OffsetDateTime.now()
        );

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
                usuarioId,
                "Usuário Teste",
                "usuario",
                Papel.VENDEDOR
        );

        VendaResponseDTO vendaResponseDTO = new VendaResponseDTO(
                vendaId,
                veiculoDTO,
                usuarioResponseDTO,
                OffsetDateTime.now(),
                new BigDecimal("35000.00")
        );


        when(vendaService.realizarVenda(any(VendaRequestDTO.class))).thenReturn(vendaResponseDTO);


        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vendaId.toString()))
                .andExpect(jsonPath("$.veiculoDTO.marca").value("Fiat"))
                .andExpect(jsonPath("$.valorVenda").value(35000.00));
    }

    @Test
    @WithMockUser(roles = "VENDEDOR")
    @DisplayName("Deve retornar erro ao tentar vender veículo indisponível")
    public void deveRetornarErroAoTentarVenderVeiculoIndisponivel() throws Exception {

        VendaRequestDTO vendaRequestDTO = new VendaRequestDTO("XYZ9876");

        when(vendaService.realizarVenda(any(VendaRequestDTO.class)))
                .thenThrow(new VeiculoNaoDisponivelException("O Veiculo não está disponivel."));


        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve negar acesso a usuário não autenticado")
    public void deveNegarAcessoAUsuarioNaoAutenticado() throws Exception {

        VendaRequestDTO vendaRequestDTO = new VendaRequestDTO("ABC1234");


        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaRequestDTO)))
                .andExpect(status().isUnauthorized());
    }
}
