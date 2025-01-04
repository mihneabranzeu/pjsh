package edu.jpa.TABLE_PER_SUBCLASS;

import edu.jpa.TABLE_PER_SUBCLASS.entity.Customer;
import edu.jpa.TABLE_PER_SUBCLASS.entity.Employee;
import edu.jpa.TABLE_PER_SUBCLASS.entity.Executive;
import org.apache.log4j.PropertyConfigurator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Launcher {
    static {
        PropertyConfigurator.configure(Launcher.class.getResource("/log4j.properties"));
    }


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnits.lab04.TABLE_PER_SUBCLASS");
        init(emf.createEntityManager());
        sample(emf.createEntityManager());
		
		emf.close();
    }


    private static void init(EntityManager em) {
        em.getTransaction().begin();

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Customer #1 (10% discount).");
        customer.setDiscount(10.0);
        em.persist(customer);

        Employee employee = new Employee();
        employee.setId(2);
        employee.setName("Employee #1.");
        employee.setSalary(1000.0);
        em.persist(employee);

        Executive executive = new Executive();
        executive.setId(3);
        executive.setName("Executive #1.");
        executive.setSalary(2000.0);
        executive.setBonus(30.0);
        em.persist(executive);

        em.getTransaction().commit();
    }


    private static void sample(EntityManager em) {
        em.getTransaction().begin();

        Executive executive = em.find(Executive.class, 3);
        System.out.println(executive.getName());

        em.getTransaction().rollback();
    }
}
