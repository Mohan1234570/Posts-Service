package in.krish.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreatedEvent {
    private Long postId;
    private Long userId;
    private Long tenantId;
    private String title;
}

