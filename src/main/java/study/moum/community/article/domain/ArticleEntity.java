package study.moum.community.article.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import study.moum.auth.domain.entity.MemberEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity author;

    @NotNull @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "comments_count")
    private int commentCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void createDate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void viewCountUp(){
        this.viewCount += 1;
    }
    public void likesCountUp() { this.likesCount += 1; }
    public void updateCommentCount(int num){
        this.commentCount += num;
    }
}
