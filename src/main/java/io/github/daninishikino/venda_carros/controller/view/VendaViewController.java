package io.github.daninishikino.venda_carros.controller.view;

import io.github.daninishikino.venda_carros.controller.DTO.request.VendaRequestDTO;
import io.github.daninishikino.venda_carros.service.VeiculoService;
import io.github.daninishikino.venda_carros.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaViewController {
    private final VendaService vendaService;
    private final VeiculoService veiculoService;

    @GetMapping("/realizar/{placa}")
    public String formVenda(@PathVariable String placa, Model model){
        model.addAttribute("veiculo", veiculoService.buscarPelaPlaca(placa));
        model.addAttribute("vendaRequest", new VendaRequestDTO(placa));
        return "venda/confirmar";
    }
    @PostMapping("/confirmar")
    public String confirmarVenda(@ModelAttribute VendaRequestDTO vendaRequestDTO, Model model){
        var vendaResponse = vendaService.realizarVenda(vendaRequestDTO);
        model.addAttribute("venda", vendaResponse);
        return "venda/sucesso";
    }
    @GetMapping("/historico")
    public String historicoVendas(Model model){
        model.addAttribute("vendas", vendaService.buscarTodasVendas());
        return "venda/historico";
    }
}
