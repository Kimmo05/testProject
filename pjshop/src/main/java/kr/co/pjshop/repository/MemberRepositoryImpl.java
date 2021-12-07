package kr.co.pjshop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pjshop.dto.MemberDto;
import kr.co.pjshop.dto.QMemberDto;
import kr.co.pjshop.entity.QMember;
import kr.co.pjshop.entity.SearchMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }
    //모두 검색
    @Override
    public Page<MemberDto> searchAll(Pageable pageable) {
        QueryResults<MemberDto> results = queryFactory
                .select(new QMemberDto(
                        QMember.member.id,
                        QMember.member.name,
                        QMember.member.loginId,
                        QMember.member.email,
                        QMember.member.role,
                        QMember.member.phoneNumber,
                        QMember.member.visitCount,
                        QMember.member.orderCount,
                        QMember.member.regTime
                ))
                .from(QMember.member)
                .orderBy(QMember.member.regTime.desc()) //생성날짜기준.내림차순 정렬
                .offset(pageable.getOffset()) //시작 위치
                .limit(pageable.getPageSize()) //제한 갯수
                .fetchResults();
//참고로 orderby가 있다면 카운트 쿼리에서는 자동으로 orderby가 지워지고 쿼리가 나간다.
        List<MemberDto> content = results.getResults(); //조회된 리스트로 결과를 반환
        long total = results.getTotal(); //전체 컨텐츠 갯수를 반환

        return new PageImpl<>(content, pageable, total); //파라미터로 content, pageable, total을 받는다
    }

    @Override
    public Page<MemberDto> searchByCondition(SearchMember search, Pageable pageable) {
        QueryResults<MemberDto> results = null;

        if (search.getSearchCondition().equals("userid")) {
            results = queryFactory
                    .select(new QMemberDto(
                            QMember.member.id,
                            QMember.member.name,
                            QMember.member.loginId,
                            QMember.member.email,
                            QMember.member.role,
                            QMember.member.phoneNumber,
                            QMember.member.visitCount,
                            QMember.member.orderCount,
                            QMember.member.regTime
                    ))
                    .from(QMember.member)
                    .where(loginIdEq(search.getSearchKeyword()))
                    .orderBy(QMember.member.regTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else if (search.getSearchCondition().equals("username")) {
            results = queryFactory
                    .select(new QMemberDto(
                            QMember.member.id,
                            QMember.member.name,
                            QMember.member.loginId,
                            QMember.member.email,
                            QMember.member.role,
                            QMember.member.phoneNumber,
                            QMember.member.visitCount,
                            QMember.member.orderCount,
                            QMember.member.regTime
                    ))
                    .from(QMember.member)
                    .where(nameEq(search.getSearchKeyword()))
                    .orderBy(QMember.member.regTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<MemberDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression loginIdEq(String loginIdCondition) {
        if (StringUtils.isEmpty(loginIdCondition)) {
            return null;
        }
        return QMember.member.loginId.likeIgnoreCase("%" + loginIdCondition + "%");
    }
    private BooleanExpression nameEq(String nameCondition) {
        if (StringUtils.isEmpty(nameCondition)) {
            return null;
        }
        return QMember.member.name.likeIgnoreCase("%" + nameCondition + "%");
    }


}