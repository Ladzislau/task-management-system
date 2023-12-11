package by.vlad.gusakov.taskmanagementsystem.repository;

import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndCommentsContaining(Long id, Comment comment);

    Page<Task> findByAssigneeOrderByCreatedAtDesc(User assignee, Pageable pageable);

    Page<Task> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);

    Page<Task> findByAuthorOrAssigneeOrderByCreatedAtDesc(User author, User assignee, Pageable pageable);
}
