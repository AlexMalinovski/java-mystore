package ru.practicum.mystore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.exception.BadRequestException;
import ru.practicum.mystore.exception.NotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionAdviceController {
    @ExceptionHandler({NoResourceFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> handleNoSuchElementException(Exception exception, Model model) {
        model.addAttribute("title", "404: Ресурс не найден");
        model.addAttribute("message", exception.getMessage());
        return Mono.just("error-page.html");
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> handleIllegalArgumentException(Exception exception, Model model) {
        model.addAttribute("title", "400: Некорректный запрос");
        model.addAttribute("message", exception.getMessage());
        return Mono.just("error-page.html");
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<String> handleException(Exception exception, Model model) {
        model.addAttribute("title", "Упс. Что-то пошло не так...");
        model.addAttribute("message", "Работаем над устранением проблемы");
        log.error(exception.getMessage(), exception);
        return Mono.just("error-page.html"); // что-то пошло не так
    }
}
