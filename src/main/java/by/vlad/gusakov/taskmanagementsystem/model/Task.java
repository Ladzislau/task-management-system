package by.vlad.gusakov.taskmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "assigned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;

    @OneToMany(mappedBy = "relatedTask")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private User assignee;

    public enum Status {
        NEW, IN_PROGRESS, PENDING_REVIEW, POSTPONED, COMPLETED
    }


    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && priority == task.priority && Objects.equals(createdAt, task.createdAt) && Objects.equals(updatedAt, task.updatedAt) && Objects.equals(assignedAt, task.assignedAt) && Objects.equals(author, task.author) && Objects.equals(assignee, task.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }
}
