package org.example.DAOs;

import org.example.entities.Category;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.List;


public class CategoryDao implements ICategoryDao {

    private final SessionFactory sessionFactory;


    public CategoryDao(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Category getCategoryById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Category.class, id);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Category", Category.class).list();
        }
    }

    @Override
    public void createCategory(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(category);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateCategory(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(category);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteCategory(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(category);
            session.getTransaction().commit();
        }
    }

}
