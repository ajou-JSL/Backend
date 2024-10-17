package study.moum.community.wishlist.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishlistEntity is a Querydsl query type for WishlistEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlistEntity extends EntityPathBase<WishlistEntity> {

    private static final long serialVersionUID = -1713338339L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishlistEntity wishlistEntity = new QWishlistEntity("wishlistEntity");

    public final study.moum.community.article.domain.QArticleEntity article;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final study.moum.auth.domain.entity.QMemberEntity member;

    public QWishlistEntity(String variable) {
        this(WishlistEntity.class, forVariable(variable), INITS);
    }

    public QWishlistEntity(Path<? extends WishlistEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishlistEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishlistEntity(PathMetadata metadata, PathInits inits) {
        this(WishlistEntity.class, metadata, inits);
    }

    public QWishlistEntity(Class<? extends WishlistEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new study.moum.community.article.domain.QArticleEntity(forProperty("article"), inits.get("article")) : null;
        this.member = inits.isInitialized("member") ? new study.moum.auth.domain.entity.QMemberEntity(forProperty("member")) : null;
    }

}

