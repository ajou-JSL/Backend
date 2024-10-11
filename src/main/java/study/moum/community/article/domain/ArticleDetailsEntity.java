package study.moum.community.article.domain;

import jakarta.persistence.*;
import lombok.*;
import study.moum.community.comment.domain.CommentEntity;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "article_details")
public class ArticleDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ArticleEntity랑 one to one 매핑 안걸고 논리적 매핑
    @Column(name = "article_id")
    private int articleId;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "articleDetails", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments = new ArrayList<>();

    public void updateArticleDetails(String newContent){

        this.content = newContent;
    }

}
