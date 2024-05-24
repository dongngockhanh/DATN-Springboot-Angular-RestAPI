package com.project.shopapp.DTOs.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PaymentResponse implements Serializable {
    private String status;
    private String message;
    private String URL;
}
