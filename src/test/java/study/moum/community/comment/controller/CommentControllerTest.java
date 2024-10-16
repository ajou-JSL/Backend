package study.moum.community.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.moum.community.article.controller.ArticleController;
import study.moum.community.article.service.ArticleService;
import study.moum.community.comment.dto.CommentDto;
import study.moum.community.comment.service.CommentService;

import static org.junit.jupiter.api.Assertions.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment_Success(){
        // given
        CommentDto.Request commentDto = CommentDto.Request.builder()
                .content("test content")
                .author()
                .
                .build();

        // when

        // then

    }

    @Test
    @DisplayName("댓글 생성 실패 - 없는 게시글에 생성 시도")
    void createComment_Faile_NoArticle(){

    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_Success(){

    }

    @Test
    @DisplayName("댓글 수정 실패 - 권한 없음")
    void updateComment_Success_NoAuthorization(){

    }

    @Test
    @DisplayName("댓글 수정 실패 - 없는 댓글에 수정 시도")
    void updateComment_Fail_NoComment(){

    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success(){

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 없음")
    void deleteComment_Fail_NoAuthorization(){

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 이미 삭제된 댓글")
    void deleteComment_Fail_AlreadyDeleted(){

    }
}