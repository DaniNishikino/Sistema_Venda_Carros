package io.github.daninishikino.venda_carros.controller.handler;

import io.github.daninishikino.venda_carros.controller.DTO.response.ErrorResponse;
import io.github.daninishikino.venda_carros.exceptions.usuario.AcessoNegadoException;
import io.github.daninishikino.venda_carros.exceptions.usuario.UsuarioNaoEncontradoException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoDisponivelException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoEncontradoException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculosDadosSensiveisException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErrorResponse> handleAcessoNegadoException(AcessoNegadoException e,
                                                              HttpServletRequest request){
        String erro = "Acesso negado, não possui a permissão para isso.";
       return buildErrorResponse(e, HttpStatus.FORBIDDEN,List.of(erro), request);
    }
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e,
                                                                      HttpServletRequest request){
        String erro = "Não foi posssivel encontrar o usuario";
       return buildErrorResponse(e, HttpStatus.NOT_FOUND, List.of(erro), request);
    }
    @ExceptionHandler(VeiculosDadosSensiveisException.class)
    public ResponseEntity<ErrorResponse> handleVeiculosDadosSensiveisException(VeiculosDadosSensiveisException e,
                                                                               HttpServletRequest request){
        String erro = "Dados sensiveis de veiculo, não podem ser alterados";
        return buildErrorResponse(e, HttpStatus.NOT_ACCEPTABLE, List.of(erro), request);
    }
    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleVeiculoNaoEncontradoException(VeiculoNaoEncontradoException e,
                                                                             HttpServletRequest request){
        String erro = "Veiculo não foi encontrado";
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, List.of(erro), request);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                    HttpServletRequest request){
        List<String> erros  = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, erros, request);

    }
    @ExceptionHandler(VeiculoNaoDisponivelException.class)
    public ResponseEntity<ErrorResponse> handleVeiculoNaoDisponivelException(VeiculoNaoDisponivelException e,
                                                                             HttpServletRequest request){
        String erro = "O Veiculo não está disponivel.";
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, List.of(erro), request);
    }
    private  ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception e, HttpStatus status, List<String > error, HttpServletRequest request){
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
