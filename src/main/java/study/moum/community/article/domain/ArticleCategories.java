package study.moum.community.article.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleCategories {
    FREE_TALKING_BOARD("자유게시판"),
    RECRUIT_BOARD("모집게시판");

    private final String category;
}
