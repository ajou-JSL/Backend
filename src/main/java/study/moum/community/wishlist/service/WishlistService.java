//package study.moum.community.wishlist.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import study.moum.auth.domain.entity.MemberEntity;
//import study.moum.auth.domain.repository.MemberRepository;
//import study.moum.community.article.domain.ArticleEntity;
//import study.moum.community.wishlist.domain.WishlistEntity;
//import study.moum.community.wishlist.domain.WishlistRepository;
//import study.moum.community.wishlist.dto.WishlistDto;
//import study.moum.global.error.ErrorCode;
//import study.moum.global.error.exception.CustomException;
//
//@Service
//@RequiredArgsConstructor
//public class WishlistService {
//
//    // 위시리스트 추가, 삭제는 좋아요 쪽에서 같이 진행됨
//
//    private final WishlistRepository wishlistRepository;
//    private final MemberRepository memberRepository;
//
//    // 위시리스트 목록 조회
//    // 멤버레포지토리에서 멤버를 찾음 -> 멤버아이디랑 위시리스트아이디랑 같은걸 찾음 ->
//    @Transactional(readOnly = true)
//    public WishlistEntity addToWishlist(MemberEntity member, ArticleEntity article) {
//        // 이미 위시리스트에 추가되어 있는지 확인
//        if (wishlistRepository.existsByMemberAndArticle(member, article)) {
//            throw new CustomException(ErrorCode.ALREADY_IN_WISHLIST);
//        }
//
//        WishlistDto.Request wishlistDto = WishlistEntity.builder()
//                .member(member)
//                .article(article)
//                .build();
//
//        WishlistEntity newWishlist = wishlistDto.toEntity();
//
//
//        // 위시리스트에 추가하고 저장
//        return wishlistRepository.save(wishlist);
//    }
//
//    @Transactional
//    public void removeFromWishlist(int memberId, int articleId) {
//        wishlistRepository.removeFromWishlist(memberId, articleId);
//    }
//
//}
