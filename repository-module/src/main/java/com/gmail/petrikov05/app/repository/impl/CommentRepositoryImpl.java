package com.gmail.petrikov05.app.repository.impl;

import com.gmail.petrikov05.app.repository.CommentRepository;
import com.gmail.petrikov05.app.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {

}
