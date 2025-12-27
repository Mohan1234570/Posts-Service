package in.krish;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String content,
        String imageUrl,
        LocalDateTime createdAt
) {}

