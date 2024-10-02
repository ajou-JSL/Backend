package study.moum.community.article.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Article_Details")
public class ArticleDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ArticleEntity랑 one to one 매핑 안걸고 사용
    @Column(name = "article_id")
    private int articleId;

    @Column(name = "content")
    private String content;

    public void updateContent(String newContent){
        this.content = newContent;
    }

}
