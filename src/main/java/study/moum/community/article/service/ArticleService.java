package study.moum.community.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleDetailsRepository;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.article.dto.ArticleDto;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;
import study.moum.global.error.exception.NeedLoginException;
import study.moum.global.error.exception.NoAuthorityException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleDetailsRepository articleDetailsRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 작성 메서드.
     *
     * @param articleRequestDto 게시글 작성 요청 DTO
     * @param memberName 작성자 사용자 이름
     * @return 작성된 게시글의 응답 DTO
     *
     * 해당 메서드는 사용자가 게시글을 작성할 때 호출되며, 작성자의 정보를 확인하고 게시글과
     * 게시글 상세 정보를 저장한 후 이를 반환
     *
     * - 작성자 확인 후 게시글 저장
     * - 게시글의 상세 내용(ArticleDetails)도 함께 저장
     */
    @Transactional
    public ArticleDto.Response postArticle(ArticleDto.Request articleRequestDto, String memberName){
        MemberEntity author = memberRepository.findByUsername(memberName);
        if(author == null){
            throw new NeedLoginException();
        }

        // article 테이블 -> title 작성
        ArticleDto.Request articleRequest = ArticleDto.Request.builder()
                .author(author)
                .title(articleRequestDto.getTitle())
                .build();

        // article 테이블 저장
        ArticleEntity newArticle = articleRequest.toEntity();
        articleRepository.save(newArticle);

        // article_details 테이블 -> content 작성
        ArticleDetailsDto.Request articleDetailsRequestDto = ArticleDetailsDto.Request.builder()
                .content(articleRequestDto.getContent())
                .build();

        // article_details 테이블 저장
        ArticleDetailsEntity newArticleDetails = articleDetailsRequestDto.toEntity();
        articleDetailsRepository.save(newArticleDetails);

        return new ArticleDto.Response(newArticle);

    }

    /**
     * 게시글 조회 메서드.
     *
     * @param articleDetailsId 게시글 상세 정보의 ID
     * @return 게시글의 상세 내용과 게시글 정보를 담은 응답 DTO
     *
     * 게시글과 게시글 상세 정보를 조회하고, 조회수가 증가한 후 반환
     */
    @Transactional
    public ArticleDetailsDto.Response getArticleById(int articleDetailsId){
        ArticleDetailsEntity articleDetails = getArticleDetails(articleDetailsId);
        ArticleEntity article = getArticle(articleDetailsId);

        article.viewCountUp(); // 조회수 증가

        return new ArticleDetailsDto.Response(articleDetails, article);
    }

    /**
     * 게시글 목록 조회 메서드.
     *
     * @return 게시글 목록의 응답 DTO 리스트
     *
     * 데이터베이스에서 모든 게시글을 조회한 후, 각 게시글을 응답 DTO로 변환하여 리스트로 반환
     */
    @Transactional(readOnly = true)
    public List<ArticleDto.Response> getArticleList() {
        // 데이터베이스에서 모든 게시글 조회
        List<ArticleEntity> articles = articleRepository.findAll();

        // 조회된 게시글들을 DTO로 변환
        List<ArticleDto.Response> articleResponseList = articles.stream()
                .map(ArticleDto.Response::new)
                .collect(Collectors.toList());

        return articleResponseList;
    }

    /**
     * 게시글 수정 메서드.
     *
     * @param articleDetailsId 게시글 상세 정보의 ID
     * @param articleDetailsRequestDto 수정할 게시글 상세 내용 DTO
     * @param memberName 수정 요청한 사용자 이름
     * @return 수정된 게시글과 게시글 상세 정보를 담은 응답 DTO
     *
     * 게시글 작성자만 수정할 수 있으며, 제목과 내용을 수정한 후 이를 저장
     */
    @Transactional
    public ArticleDetailsDto.Response updateArticleDetails(int articleDetailsId,
                                                           ArticleDetailsDto.Request articleDetailsRequestDto,
                                                           String memberName){

        ArticleDetailsEntity articleDetails = getArticleDetails(articleDetailsId);
        ArticleEntity article = getArticle(articleDetailsId);

        String articleAuthor = article.getAuthor().getUsername();
        checkAuthor(memberName, articleAuthor);

        String newTitle = articleDetailsRequestDto.getTitle();
        String newContent = articleDetailsRequestDto.getContent();

        // article_details, article 둘 다 update
        articleDetails.updateArticleDetails(newContent);
        article.updateArticle(newTitle);

        // article_details, article 둘 다 저장
        articleDetailsRepository.save(articleDetails);
        articleRepository.save(article);

        return new ArticleDetailsDto.Response(articleDetails, article);
    }

    /**
     * 게시글 삭제 메서드.
     *
     * @param articleDetailsId 게시글 상세 정보의 ID
     * @param memberName 삭제 요청한 사용자 이름
     * @return 삭제된 게시글의 응답 DTO
     *
     * 게시글 작성자만 삭제할 수 있으며, 게시글과 게시글 상세 정보를 삭제
     */
    @Transactional
    public ArticleDto.Response deleteArticleDetails(int articleDetailsId, String memberName){

        ArticleDetailsEntity articleDetails = getArticleDetails(articleDetailsId);
        ArticleEntity article = getArticle(articleDetailsId);

        // 로그인유저 == 작성자 여부 체크
        String articleAuthor = article.getAuthor().getUsername();
        checkAuthor(memberName, articleAuthor);

        // article_details, article 테이블 둘 다 삭제
        articleDetailsRepository.deleteById(articleDetailsId);
        articleRepository.deleteById(articleDetailsId);

        return new ArticleDto.Response(article);
    }

    private ArticleDetailsEntity getArticleDetails(int articleDetailsId) {
        return articleDetailsRepository.findById(articleDetailsId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private ArticleEntity getArticle(int articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private void checkAuthor(String memberName, String articleAuthor) {
        if (!memberName.equals(articleAuthor)) {
            throw new NoAuthorityException();
        }
    }


}
