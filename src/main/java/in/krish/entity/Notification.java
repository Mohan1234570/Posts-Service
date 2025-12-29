//package in.krish.entity;
//
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "notification")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Notification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // 🔐 RECEIVER USER ID (JWT)
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//
//    // 🔐 SENDER USER ID (JWT)
//    @Column(name = "sender_id")
//    private Long senderId;
//
//    // 🔗 POST IS LOCAL ENTITY → RELATION OK
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @Column(nullable = false, length = 500)
//    private String message;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(nullable = false)
//    private Boolean read = false;
//}
