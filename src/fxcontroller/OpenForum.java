package fxcontroller;

import hibernate.Hibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Comment;
import model.Driver;
import model.Forum;
import model.User;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class OpenForum {
    public TextField titleField;
    public TextArea descriptionField;
    public Text descriptionFlow;
    public Button saveBtn;
    public Button deleteBtn;
    public Button createBtn;
    public Text titleHeading;
    public Button editForumBtn;
    public ListView<Comment> commentList;
    public TextArea addCommentField;
    public Button okBtn;
    public Button closeBtn;
    public Text commentsLabel;
    public Button addCommentBtn;
    public Button modifyCommentBtn;
    public Button deleteCommentBtn;
    public Button saveCommentBtn;
    public Button cancelCommentBtn;

    private EntityManagerFactory entityManagerFactory;
    private Forum selectedForum;
    private Comment selectedComment;
    private Hibernate hibernate;
    private Main main;
    private User currentUser;

    public void createEntity(EntityManagerFactory entityManagerFactory, boolean create) {
        this.entityManagerFactory = entityManagerFactory;
        if (create) {
            createBtn.setVisible(true);
            descriptionField.setVisible(true);
            saveBtn.setVisible(false);
            deleteBtn.setVisible(false);
            editForumBtn.setVisible(false);
            titleHeading.setVisible(false);
            titleField.setVisible(true);
            commentsLabel.setVisible(false);
            commentList.setVisible(false);
            addCommentBtn.setVisible(false);
            modifyCommentBtn.setVisible(false);
            deleteCommentBtn.setVisible(false);
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, Forum selectedForum) {
        this.entityManagerFactory = entityManagerFactory;
        this.selectedForum = selectedForum;
        this.hibernate = new Hibernate(entityManagerFactory);
        this.currentUser = currentUser;

        if (currentUser.getClass() == Driver.class) {
            editForumBtn.setVisible(false);
        }

        fillFields();
    }

    private void fillFields() {
        titleHeading.setText(selectedForum.getTitle());
        titleField.setText(selectedForum.getTitle());
        descriptionField.setText(selectedForum.getDescription());
        descriptionFlow.setText(selectedForum.getDescription());

        List<Comment> allComments = hibernate.getForumComments(selectedForum);
        commentList.getItems().clear();
        allComments.forEach(c-> commentList.getItems().add(c));
    }

    public void updateParentLists(Main main) {
        this.main = main;
    }

    public void saveForum() {
        selectedForum.setTitle(titleField.getText());
        selectedForum.setDescription(descriptionField.getText());
        titleHeading.setText(titleField.getText());
        descriptionFlow.setText(descriptionField.getText());
        hibernate.updateForum(selectedForum);
        main.setData(entityManagerFactory);

        saveBtn.setVisible(false);
        deleteBtn.setVisible(false);
        editForumBtn.setVisible(true);
        titleHeading.setVisible(true);
        titleField.setVisible(false);
        descriptionField.setVisible(false);
        descriptionFlow.setVisible(true);
    }

    public void deleteForum() {
        hibernate.removeForum(selectedForum);
        main.setData(entityManagerFactory);
        Stage stage = (Stage) deleteBtn.getScene().getWindow();
        stage.close();
    }

    public void createForum() {
        this.hibernate = new Hibernate(entityManagerFactory);
        Forum forum = new Forum(titleField.getText(), descriptionField.getText());
        hibernate.createForum(forum);
        main.setData(entityManagerFactory);
        Stage stage = (Stage) createBtn.getScene().getWindow();
        stage.close();
    }

    public void editForum() {
        saveBtn.setVisible(true);
        deleteBtn.setVisible(true);
        editForumBtn.setVisible(false);
        titleHeading.setVisible(false);
        titleField.setVisible(true);
        descriptionField.setVisible(true);
        descriptionFlow.setVisible(false);
    }

    public void addComment() {
        addCommentBtn.setVisible(false);
        okBtn.setVisible(true);
        addCommentField.clear();
        addCommentField.setVisible(true);
        modifyCommentBtn.setVisible(false);
        deleteCommentBtn.setVisible(false);
        cancelCommentBtn.setVisible(true);
    }

    public void ok() {
        addCommentBtn.setVisible(true);
        modifyCommentBtn.setVisible(true);
        deleteCommentBtn.setVisible(true);
        okBtn.setVisible(false);
        addCommentField.setVisible(false);
        cancelCommentBtn.setVisible(false);

        this.hibernate = new Hibernate(entityManagerFactory);
        Comment comment = new Comment(addCommentField.getText(), selectedForum);
        hibernate.createComment(comment);
        fillFields();
    }

    public void closeForum() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    public void modifyComment() {
        selectedComment = commentList.getSelectionModel().getSelectedItem();
        if (selectedComment == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select a comment to modify it!");
        }
        else {
            addCommentField.setText(String.valueOf(selectedComment));
            addCommentBtn.setVisible(false);
            saveCommentBtn.setVisible(true);
            addCommentField.setVisible(true);
            modifyCommentBtn.setVisible(false);
            deleteCommentBtn.setVisible(false);
            cancelCommentBtn.setVisible(true);
        }
    }

    public void deleteComment() {
        selectedComment = commentList.getSelectionModel().getSelectedItem();
        if (selectedComment == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select a comment before deleting it!");
        }
        else {
            Comment comment = (Comment) hibernate.findById(selectedComment, selectedComment.getId());
            hibernate.removeComment(comment);
            fillFields();
        }
    }

    public void saveComment() {
        addCommentBtn.setVisible(true);
        saveCommentBtn.setVisible(false);
        addCommentField.setVisible(false);
        modifyCommentBtn.setVisible(true);
        deleteCommentBtn.setVisible(true);
        cancelCommentBtn.setVisible(false);

        Comment comment = (Comment) hibernate.findById(selectedComment, selectedComment.getId());
        comment.setCommentText(addCommentField.getText());
        hibernate.updateComment(comment);
        fillFields();
    }

    public void cancelComment() {
        addCommentBtn.setVisible(true);
        modifyCommentBtn.setVisible(true);
        deleteCommentBtn.setVisible(true);
        saveCommentBtn.setVisible(false);
        okBtn.setVisible(false);
        addCommentField.setVisible(false);
        cancelCommentBtn.setVisible(false);
    }
}
