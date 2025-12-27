package in.krish.kafkaconsumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.krish.entity.UserProfile;
import in.krish.repo.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final UserProfileRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(
            topics = "auth.user.created",
            groupId = "chatbook-user-sync"
    )
    public void consume(String message) {
        try {
            JsonNode root = mapper.readTree(message);
            JsonNode user = root.get("user");

            UserProfile profile = new UserProfile();
            profile.setUserId(user.get("id").asLong());
            profile.setEmail(user.get("email").asText());
            profile.setFullName(user.get("fullName").asText());
            profile.setTenantId(user.get("tenantId").asLong());
            profile.setPlan(user.get("plan").asText());
            profile.setStatus(user.get("status").asText());

            repo.save(profile);

            log.info(" User synced into Chatbook: {}", profile.getUserId());

        } catch (Exception e) {
            log.error("Failed to process USER_CREATED", e);
        }
    }
}

