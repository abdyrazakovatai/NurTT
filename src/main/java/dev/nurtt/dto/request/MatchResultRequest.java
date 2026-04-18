package dev.nurtt.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchResultRequest {
    private Long winnerId;           // id слота победителя (slot1 или slot2)
    private List<SetRequest> sets;   // партии
}
