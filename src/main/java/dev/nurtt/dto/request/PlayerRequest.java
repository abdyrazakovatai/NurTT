package dev.nurtt.dto.request;

import lombok.*;

@Getter @Setter
@Builder
public class PlayerRequest {
    private String fullName;
    private String email;
}
