package com.gmail.petrikov05.app.service.impl;

import java.lang.invoke.MethodHandles;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.CommentRepository;
import com.gmail.petrikov05.app.repository.model.Comment;
import com.gmail.petrikov05.app.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final static Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {this.commentRepository = commentRepository;}

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Comment comment = commentRepository.getObjectByID(id);
        if (comment != null) {
            return commentRepository.delete(comment);
        } else {
            logger.info("comment with id(" + id + ") not found");
            return false;
        }
    }

}
