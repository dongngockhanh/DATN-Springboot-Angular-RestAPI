package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Component
@Getter
@Setter
public class MessageResponse {
    @JsonIgnore
    private final MessageSource messageSource;
    @JsonIgnore
    private final LocaleResolver localeResolver;

    @JsonProperty("message")
    private String message;

    public String getMessageResponse(String messageKey) {
        return this.message = messageSource.getMessage(messageKey,null,getLocale());
    }

    //lấy locale từ client
    @JsonIgnore
    private Locale getLocale(){
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return localeResolver.resolveLocale(request);
    }
}
