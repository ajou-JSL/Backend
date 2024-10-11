package study.moum.community.comment.domain;

import jakarta.persistence.*;
import lombok.*;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity author;

    @JoinColumn(name = "article_details_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private ArticleDetailsEntity articleDetails;

    @Column(name = "content")
    private String content;
}
