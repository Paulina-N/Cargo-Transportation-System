package hibernate;

import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hibernate {
    EntityManager entityManager = null;
    EntityManagerFactory entityManagerFactory = null;

    public Hibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void createVehicle(Vehicle vehicle) {
        createObject(vehicle);
    }
    public void createUser(User user) {
        createObject(user);
    }
    public void createTrip(Trip trip) {
        createObject(trip);
    }

    public void updateVehicle(Vehicle vehicle) {
        updateObject(vehicle);
    }
    public void updateUser(User user) {
        updateObject(user);
    }
    public void updateTrip(Trip trip) {
        updateObject(trip);
    }

    public void removeVehicle(Vehicle vehicle) {
        removeObject(vehicle, vehicle.getId());
    }
    public void removeUser(User user) {
        removeObject(user, user.getId());
    }
    public void removeTrip(Trip trip) {
        removeObject(trip, trip.getId());
    }

    public Object findById(Object object, int id) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            object = entityManager.find(object.getClass(), id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No object found by the given ID");
        }
        return object;
    }

    public <T> List<T> getAllObjects(Object object) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(object.getClass()));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return new ArrayList<>();
    }

    public void createObject(Object object) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateObject(Object object) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void removeObject(Object object, int id) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(object.getClass(), id));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public User getUserByLoginData(String fieldName1, String fieldName2, String data1, String data2, boolean isManager) {
        entityManager = entityManagerFactory.createEntityManager();
        Query q = null;
        CriteriaQuery<Driver> queryDriver = null;
        CriteriaQuery<Manager> queryManager = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        if (!isManager) {
            queryDriver = cb.createQuery(Driver.class);
            Root<Driver> root = queryDriver.from(Driver.class);
            if (Objects.equals(fieldName2, "password"))
                queryDriver.select(root).where(cb.and(cb.like(root.get(fieldName1), data1), cb.like(root.get(fieldName2), data2)));
            else
                queryDriver.select(root).where(cb.or(cb.like(root.get(fieldName1), data1), cb.like(root.get(fieldName2), data2)));
        } else {
            queryManager = cb.createQuery(Manager.class);
            Root<Manager> root = queryManager.from(Manager.class);
            if (Objects.equals(fieldName2, "password"))
                queryManager.select(root).where(cb.and(cb.like(root.get(fieldName1), data1), cb.like(root.get(fieldName2), data2)));
            else
                queryManager.select(root).where(cb.or(cb.like(root.get(fieldName1), data1), cb.like(root.get(fieldName2), data2)));
        }

        try {
            if (queryDriver != null) q = entityManager.createQuery(queryDriver);
            if (queryManager != null) q = entityManager.createQuery(queryManager);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        entityManager = entityManagerFactory.createEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery(
                            "SELECT u from Driver u WHERE u.username = :username", Driver.class).
                    setParameter("username", username).getSingleResult();
        } catch (NoResultException ignored) {}

        try {
            user = entityManager.createQuery(
                            "SELECT u from Manager u WHERE u.username = :username", Manager.class).
                    setParameter("username", username).getSingleResult();
        } catch (NoResultException ignored) {}

        return user;
    }

    public User getUserByEmail(String email) {
        entityManager = entityManagerFactory.createEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery(
                            "SELECT u from Driver u WHERE u.email = :email", Driver.class).
                    setParameter("email", email).getSingleResult();
        } catch (NoResultException ignored) {}

        try {
            user = entityManager.createQuery(
                            "SELECT u from Manager u WHERE u.email = :email", Manager.class).
                    setParameter("email", email).getSingleResult();
        } catch (NoResultException ignored) {}

        return user;
    }

//    public List<User> getAllUsers() {
//        return Stream.concat(getAllManagers().stream(), getAllDrivers().stream()).toList();
//    }
}
