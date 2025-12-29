package in.krish.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class JwtUser {
    private Long userId;
    private String email;
    private Long tenantId;
    private Set<String> roles;
}
