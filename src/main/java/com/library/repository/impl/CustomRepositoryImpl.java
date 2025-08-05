package com.library.repository.impl;

import com.library.dto.BookDto;
import com.library.entity.QBook;
import com.library.repository.CustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 도서명 / 저자 / 도서코드 로 책을 검색 -> 전체 도서 권 수 / 대여가능 권 수 까지 제공
     */
    @Override
    public List<BookDto> findBooks(String keyword) {
        // Book 엔티티를 Table 처럼 접근하기 위해 Q 클래스 사용 (QueryDsl)
        QBook book = QBook.book;
        return jpaQueryFactory
                .select(Projections.constructor(
                        BookDto.class,
                        book.title,
                        book.author,
                        book.code,
                        book.countDistinct(),
                        book.isAvailable.when(true).then(1).otherwise(0).sum())
                )
                .from(book)
                .where(keyword != null && !keyword.isBlank()
                        ? book.title.containsIgnoreCase(keyword)
                        .or(book.author.containsIgnoreCase(keyword))
                        .or(book.code.containsIgnoreCase(keyword))
                        : null)
                .groupBy(book.title, book.author, book.code)
                .fetch();
    }
}
