package com.library.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.library.dto.BookDto;
import com.library.entity.QBook;
import com.library.repository.CustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<BookDto> findBooks(String keyword) {
		
		// 와 진짜 하나도 모르겠다 ㅋㅋㅋ
		
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
						? book.title.containsIgnoreCase(keyword).or(book.author.containsIgnoreCase(keyword))
						: null)
				.groupBy(book.title, book.author, book.code)
				.fetch();
	}
}
