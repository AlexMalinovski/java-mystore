package ru.practicum.mystore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.practicum.mystore.exception.BadRequestException;
import ru.practicum.mystore.exception.NotFoundException;

@ControllerAdvice
public class ExceptionAdviceController {
    // Обработка 404
    @ExceptionHandler({NoResourceFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(Exception exception, Model model) {
        model.addAttribute("title", "404: Ресурс не найден");
        model.addAttribute("message", exception.getMessage());
        return "error-page.html";
    }

    // Обработка 400
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(Exception exception, Model model) {
        model.addAttribute("title", "400: Некорректный запрос");
        model.addAttribute("message", exception.getMessage());
        return "error-page.html";
    }

    // Обработка 500
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception, Model model) {
        model.addAttribute("title", "Упс. Что-то пошло не так...");
        model.addAttribute("message", "Работаем над устранением проблемы");
        return "error-page.html"; // что-то пошло не так
    }
}
