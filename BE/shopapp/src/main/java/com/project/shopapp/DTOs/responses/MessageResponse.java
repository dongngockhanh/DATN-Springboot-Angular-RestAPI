package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.untils.MessageKeys;
import lombok.*;
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

    // trả về object
    @JsonIgnore
    public MessageResponse getMessageResponse(String messageKey,Object... params){
        getMessageString(messageKey,params);
        return this;
    }

    // trả về String
    public String getMessageString(String messageKey,Object... params) {
        return this.message = messageSource.getMessage(messageKey,params,getLocale());
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
