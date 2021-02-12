package com.gmail.petrikov05.app.service;

import com.gmail.petrikov05.app.repository.CommentRepository;
import com.gmail.petrikov05.app.repository.model.Article;
import com.gmail.petrikov05.app.repository.model.Comment;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
class CommentServiceTest {

    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {this.commentService = new CommentServiceImpl(commentRepository);}

    @Test
    public void deleteValidCommentById_returnTrue() {
        Comment returnedComment = getValidComment();
        when(commentRepository.getObjectByID(VALID_COMMENT_ID)).thenReturn(returnedComment);
        when(commentRepository.delete(returnedComment)).thenReturn(true);
        boolean actualResult = commentService.deleteById(VALID_COMMENT_ID);
        verify(commentRepository, times(1)).getObjectByID(anyLong());
        verify(commentRepository, times(1)).delete(any());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isTrue();
    }

    @Test
    public void deleteInvalidCommentById_returnfalse() {
        when(commentRepository.getObjectByID(VALID_COMMENT_ID)).thenReturn(null);
        boolean actualResult = commentService.deleteById(VALID_COMMENT_ID);
        verify(commentRepository, times(1)).getObjectByID(VALID_COMMENT_ID);
        assertThat(actualResult).isFalse();
    }

    private Comment getValidComment() {
        Comment comment = new Comment();
        comment.setId(VALID_COMMENT_ID);
        comment.setDate(VALID_COMMENT_DATE);
        comment.setText(VALID_COMMENT_TEXT);
        comment.setAuthor(getValidUser());
        comment.setArticle(getValidArticle());
        return comment;
    }

    private Article getValidArticle() {
        Article article = new Article();
        return article;
    }

    private User getValidUser() {
        User user = new User();
        user.setUserDetails(getValidUserInformation());
        return user;
    }

    private UserDetails getValidUserInformation() {
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(VALID_COMMENT_LAST_NAME);
        userDetails.setFirstName(VALID_COMMENT_FIRST_NAME);
        return userDetails;
    }

}