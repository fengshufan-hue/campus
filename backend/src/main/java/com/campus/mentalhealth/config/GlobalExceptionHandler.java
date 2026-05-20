package com.campus.mentalhealth.config;

import com.campus.mentalhealth.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return Result.error(400, message);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleDateTimeParseException(DateTimeParseException e) {
        return Result.error(400, "日期格式不正确，请使用yyyy-MM-dd格式");
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNumberFormatException(NumberFormatException e) {
        return Result.error(400, "数字格式不正确");
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("数据库访问异常", e);
        return Result.error("服务器内部错误，请稍后重试");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("服务器内部错误，请稍后重试");
    }
}
