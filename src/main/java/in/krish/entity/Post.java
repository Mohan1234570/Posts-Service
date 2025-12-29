package in.krish.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "image_url", length = 500)
	private String imageUrl;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	// 🔐 External reference (Auth Service)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	// 🧱 Multi-tenant support
	@Column(name = "tenant_id", nullable = false)
	private Long tenantId;

	// ================= RELATIONSHIPS =================
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Like> likes = new ArrayList<>();

	// ================= TRANSIENT =================
	@Transient
	public int getLikesCount() {
		return likes.size();
	}

	@Transient
	public List<String> getLikedBy() {
		return likes.stream()
				.map(l -> String.valueOf(l.getUserId()))
				.toList();
	}
}
