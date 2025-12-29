package in.krish.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter@Getter
@AllArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	// ✅ identity only (from JWT / headers)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	// ✅ denormalized (optional but recommended)
	@Column(name = "user_email")
	private String userEmail;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "tenant_id", nullable = false)
	private Long tenantId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	public Comment() {

	}
}
