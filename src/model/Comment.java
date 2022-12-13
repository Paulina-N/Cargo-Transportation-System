package model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentText;

    @ManyToOne
    private Comment parentComment;
    @ManyToOne
    private Forum forum;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    public Comment(String commentText, Forum forum) {
        this.commentText = commentText;
        this.forum = forum;
    }

    public Comment(String commentText, Comment parentComment, Forum forum) {
        this.commentText = commentText;
        this.replies = new ArrayList<>();
        this.parentComment = parentComment;
        this.forum = forum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return commentText;
    }
}