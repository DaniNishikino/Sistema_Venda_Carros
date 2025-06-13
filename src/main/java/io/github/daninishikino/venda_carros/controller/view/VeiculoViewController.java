package io.github.daninishikino.venda_carros.controller.view;

import io.github.daninishikino.venda_carros.controller.DTO.request.VeiculoDTO;
import io.github.daninishikino.venda_carros.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/veiculo")
@RequiredArgsConstructor
public class VeiculoViewController {

    private final VeiculoService veiculoService;

    @GetMapping
    public String listarVeiculos(Model model){
        model.addAttribute("veiculos", veiculoService.buscarTodos());
        return "veiculo/detalhes";
    }
    @GetMapping("/{placa}")
    public String detalharVeiculo(@PathVariable String placa, Model model){
        model.addAttribute("veiculo", veiculoService.buscarPelaPlaca(placa));
        return "veiculo/detalhes";
    }
    @GetMapping("/novo")
    public String novoVeiculoForm(Model model){
        model.addAttribute("veiculo", new VeiculoDTO(
                null, "", "", 2023, 2023, "", "",
                BigDecimal.ZERO, "", true, null
        ));
        return "veiculo/form";
    }
    @PostMapping
    public String salvarVeiculo(@Valid @ModelAttribute("veiculo") VeiculoDTO veiculoDTO,
                                BindingResult result){
        if (result.hasErrors()){
            return "veiculo/form";
        }
        veiculoService.salvar(veiculoDTO);
        return "redirect:/home";
    }
    @GetMapping("/editar/{placa}")
    public String editarVeiculoForm(@PathVariable String placa, Model model){
        model.addAttribute("veiculo", veiculoService.buscarPelaPlaca(placa));
        return "veiculo/editar";
    }
    @PostMapping("/atualizar/{placa}")
    public String atualizarVeiculo(@PathVariable String placa,
                                   @Valid @ModelAttribute("veiculo") VeiculoDTO veiculoDTO,
                                   BindingResult result){
        if (result.hasErrors()){
            return "veiculo/editar";
        }
        veiculoService.atualizar(placa, veiculoDTO);
        return "redirect:/home";
    }
    @GetMapping("/remover/{placa}")
    public String removerVeiculo(@PathVariable String placa){
        veiculoService.deletar(placa);
        return "redirect:/home";
    }
}
