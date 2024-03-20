package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseResponse {
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
