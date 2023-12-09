package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentPageResponse {

    private Integer currentPage;

    private Integer totalPage;

    private Long totalElements;

    private List<Comment> comments;
}
