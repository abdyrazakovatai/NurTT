package dev.nurtt.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class PlayerResponse {
    private Long id;
    private String fullName;
    private String email;
}
