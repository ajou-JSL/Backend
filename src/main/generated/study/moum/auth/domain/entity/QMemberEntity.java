package study.moum.auth.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberEntity is a Querydsl query type for MemberEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberEntity extends EntityPathBase<MemberEntity> {

    private static final long serialVersionUID = -1778365931L;

    public static final QMemberEntity memberEntity = new QMemberEntity("memberEntity");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath password = createString("password");

    public final StringPath role = createString("role");

    public final StringPath username = createString("username");

    public final ListPath<study.moum.community.wishlist.domain.WishlistEntity, study.moum.community.wishlist.domain.QWishlistEntity> wishlists = this.<study.moum.community.wishlist.domain.WishlistEntity, study.moum.community.wishlist.domain.QWishlistEntity>createList("wishlists", study.moum.community.wishlist.domain.WishlistEntity.class, study.moum.community.wishlist.domain.QWishlistEntity.class, PathInits.DIRECT2);

    public QMemberEntity(String variable) {
        super(MemberEntity.class, forVariable(variable));
    }

    public QMemberEntity(Path<? extends MemberEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberEntity(PathMetadata metadata) {
        super(MemberEntity.class, metadata);
    }

}

