package study.moum.community.comment.domain;

import jakarta.persistence.*;
import lombok.*;
import study.moum.auth.domain.entity.MemberEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity author;

    @Column(name = "content")
    private String content;
}
