package com.techacademy.springdemo.dao;

import com.techacademy.springdemo.entity.Customer;
import com.techacademy.springdemo.util.SortUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    // need to inject the session factory
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers(int theSortField) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // determine sort field
        String theFieldName = null;

        switch (theSortField) {
            case SortUtils.FIRST_NAME:
                theFieldName = "firstName";
                break;
            case SortUtils.LAST_NAME:
                theFieldName = "lastName";
                break;
            case SortUtils.EMAIL:
                theFieldName = "email";
                break;
            default:
                // if noting matches the default to sort by last name
                theFieldName = "lastName;";
        }


        // create a  query ... sort by the last name
        Query<Customer> theQuery =
                currentSession.createQuery("from Customer ORDER BY " + theFieldName,
                        Customer.class);

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the result
        return customers;
    }

    @Override
    public void saveCustomer(Customer theCustomer) {

        // get current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();


        // save the customer
        currentSession.saveOrUpdate(theCustomer);
    }

    @Override
    public Customer getCustomer(int theId) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // now retrieve / read from the database using the primary key
        Customer theCustomer = currentSession.get(Customer.class, theId);

        return theCustomer;
    }

    @Override
    public void deleteCustomer(int theId) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // delete the customer with primary key
        Query theQuery =
                currentSession.createQuery("delete from Customer  where id=:customerId");

        theQuery.setParameter("customerId", theId);

        theQuery.executeUpdate();

    }

    @Override
    public List<Customer> searchCustomers(String theSearchName) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        Query theQuery = null;

        //
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // search for first name or last name ... case-insensitive
            theQuery = currentSession.createQuery("from Customer where  " +
                    "lower(firstName) like :theName or lower(lastName) like  :theName", Customer.class);

            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        } else {
            // theSearchName is empty .... so just get a customers
            theQuery = currentSession.createQuery("from Customer", Customer.class);
        }

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the result
        return customers;
    }
}































