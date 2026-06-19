package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .leftJoin(todo.user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(
            String keyword,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String managerNickname,
            Pageable pageable
    ) {
        List<TodoSearchResponse> content = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(
                        titleContains(keyword),
                        createdAtAfterOrEqualStartDate(startDate),
                        createdAtBeforeOrEqualEndDate(endDate),
                        managerNicknameContains(managerNickname)
                )
                .groupBy(todo.id, todo.title, todo.createdAt)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(
                        titleContains(keyword),
                        createdAtAfterOrEqualStartDate(startDate),
                        createdAtBeforeOrEqualEndDate(endDate),
                        managerNicknameContains(managerNickname)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount == null ? 0L : totalCount);
    }

    private BooleanExpression titleContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return todo.title.like("%" + keyword + "%");
    }

    private BooleanExpression createdAtAfterOrEqualStartDate(LocalDateTime startDate) {
        return startDate == null ? null : todo.createdAt.goe(startDate);
    }

    private BooleanExpression createdAtBeforeOrEqualEndDate(LocalDateTime endDate) {
        return endDate == null ? null : todo.createdAt.loe(endDate);
    }

    private BooleanExpression managerNicknameContains(String managerNickname) {
        if (managerNickname == null || managerNickname.isBlank()) {
            return null;
        }

        return manager.user.nickname.like("%" + managerNickname + "%");
    }
}
