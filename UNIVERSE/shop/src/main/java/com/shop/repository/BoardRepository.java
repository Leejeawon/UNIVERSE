package com.shop.repository;

import com.shop.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

    //JpaRepository<엔티티 타입, 기본키 타입>
    public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board>, BoardRepositoryCustom{
        List<Board> findByBoardNameOrBoardDetail(String boardName, String boardDetail);

        @Query("select i from Board i where i.boardDetail like %:boardDetail%")
        List<Board> findByBoardDetail(@Param("boardDetail") String boardDetail);

        @Query(value="select * from Board i where i.board_Detail like %:boardDetail%",nativeQuery = true)
        List<Board> findByBoardDetailByNative(@Param("boardDetail") String boardDetail);

}
