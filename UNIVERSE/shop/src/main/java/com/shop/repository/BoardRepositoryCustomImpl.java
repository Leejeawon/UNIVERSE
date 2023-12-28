package com.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.*;
import com.shop.entity.*;
import org.springframework.data.domain.Page;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    // EntityManager를 기반으로 생성되고
    // QueryDsl을 사용하고 JPA쿼리를 실행할 수 있도록 함
    private JPAQueryFactory queryFactory;

    // 생성자에서 EntityManager를 받아서 JPAQueryFactory를 초기화
    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 현재 날짜와 시간을 설정하여 해당 시간 이후로 등록된 상품만 조회하도록
    // 조건을 생성
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if (StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QBoard.board.regTime.after(dateTime);
    }

    // 검색어가 포함되어 있는 상품 또는 아이디를 조회
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if (StringUtils.equals("itemName", searchBy)){
            return QBoard.board.boardName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QBoard.board.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        // 쿼리를 실행해서 조회된 항목을 가져옴
        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                // 등록날짜를 기준, 판매 상태 기준, 검색어를 기준으로 하여 조회
                // where절에 , 로 표시하면 and로 실행됨
                // or 조건을 이용하고자 한다면 BooleanBuilder를 사용
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery())
                )
                .orderBy(QBoard.board.id.desc()) // 정렬
                .offset(pageable.getOffset()) // 데이터를 가져오도록 시작 인덱스를 설정
                .limit(pageable.getPageSize()) // 한번에 가져올 페이지의 개수
                .fetch(); // 리스트를 반환
        // 전체 항목수
        long total = queryFactory.select(Wildcard.count)
                .from(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery())
                )
                .fetchOne();
        // 조회된 항목의 리스트, 페이지정보, 전체항목수를 page객체로 반환
        return  new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<BoardContentDto> getBoardContentPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        // Querydsl을 사용하기 위해서 q클래스 선언
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        // QMainItemDto의 생성자에 반환할 값을 입력
        List<BoardContentDto> content = queryFactory.select(
                        new QBoardContentDto(
                                board.id,
                                board.boardName,
                                board.boardDetail,
                                boardImg.imgUrl)
                )
                .from(boardImg)
                .join(boardImg.board, board) // itemImg와 item을 조인
                .where(boardImg.repImgYn.eq("Y")) // 대표이미지만 불러옴
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 아이템의 개수를 조회
        long total = queryFactory.select(Wildcard.count)
                .from(boardImg)
                .join(boardImg.board, board)
                .where(boardImg.repImgYn.eq("Y"))
                .where(boardNameLike(boardSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    // 검색어가 공백이면 null, 아니면 검색어가 포함되는 상품을 조회
    private BooleanExpression boardNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.boardName.like("%" + searchQuery + "%");
    }
}
