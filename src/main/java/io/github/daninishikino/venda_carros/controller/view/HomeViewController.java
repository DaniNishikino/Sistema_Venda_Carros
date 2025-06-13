package io.github.daninishikino.venda_carros.controller.view;

import io.github.daninishikino.venda_carros.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeViewController {
    private final VeiculoService veiculoService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("veiculos", veiculoService.buscarTodos());
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/acesso-negado")
    public String acessoNegado(){
        return "error/acesso-negado";
    }
}
