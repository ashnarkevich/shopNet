package com.gmail.petrikov05.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.UserRepository;
import com.gmail.petrikov05.app.repository.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public User getUserByEmail(String email) {
        String queryString = "FROM " + entityClass.getSimpleName() + " u WHERE u.email = :email AND u.isDeleted = false";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        try {
            Object user = query.getSingleResult();
            return (User) user;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        String queryString = "FROM " + entityClass.getSimpleName() + " u WHERE u.id = :id";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("id", id);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("user with id = " + id + " not found");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getUsersByPage(int startPosition, int countByPage) {
        String queryString = "FROM " + entityClass.getSimpleName() + " e ORDER BY e.email ASC";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(countByPage);
        return (List<User>) query.getResultList();
    }

}
