//package study.moum.community.wishlist.controller;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import study.moum.auth.domain.CustomUserDetails;
//import study.moum.community.likes.dto.LikesDto;
//import study.moum.community.wishlist.dto.WishlistDto;
//import study.moum.community.wishlist.service.WishlistService;
//import study.moum.global.error.exception.NeedLoginException;
//import study.moum.global.response.ResponseCode;
//import study.moum.global.response.ResultResponse;
//
//@RestController
//@RequiredArgsConstructor
//public class WishlistController {
//
//    private final WishlistService wishlistService;
//
//    @GetMapping("/api/wishlists/{}")
//    public ResponseEntity<ResultResponse> getMyWishlist(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                                        @PathVariable int ){
//        if(customUserDetails == null){
//            throw new NeedLoginException();
//        }
//
//        List<Dto.Response> wishlistResponse = service.getMyWishlist();
//        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_LIST_GET_SUCCESS,wishlistResponse);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//
//    }
//}
