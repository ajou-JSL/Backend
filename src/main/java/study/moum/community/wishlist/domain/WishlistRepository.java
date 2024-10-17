package study.moum.community.wishlist.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleEntity;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {

    /*
        INSERT INTO wishlist (user_id, article_id)
        VALUES (?, ?);
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wishlist(member_id, article_id) VALUES(:member_id, :article_id)",nativeQuery = true)
    void addToWishlist(int member_id, int article_id);

    /*
        DELETE FROM wishlist
        WHERE member_id = ? AND article_id = ?;
    */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wishlist WHERE member_id = :member_id AND article_id = :article_id", nativeQuery = true)
    void removeFromWishlist(int member_id, int article_id);


    boolean existsByMemberAndArticle(MemberEntity member, ArticleEntity article);

}
