package com.ps;

import org.hamcrest.Matchers;
import org.javalite.activejdbc.Base;
import org.junit.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;


public class ActiveJDBCTest {

    @Before
    public void setUp() {
        Base.open("org.h2.Driver",
                "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:create-db.sql'",
                null, null);
    }

    @After
    public void tearDown() {
        Base.close();
    }

    @Test
    public void simpleCRUD() {
        // create
        Employee employee = new Employee()
                .set("first_name", "John")
                .set("last_name", "Doe");
        employee.saveIt();
        // get one
        Employee saved = Employee.findFirst("first_name = ?", "John");
        assertThat(saved, notNullValue());
        // update
        saved.set("last_name", "Steinbeck").saveIt();
        Employee updated = Employee.findFirst("first_name = ?", "John");
        assertThat(updated.get("last_name"), is("Steinbeck"));
        // select
        List<Employee> employees = Employee.where("first_name like ?", "J%");
        assertThat(employees.size(), Matchers.greaterThanOrEqualTo(1));
        // delete
        Employee deleted = Employee.findFirst("first_name = ?", "John");
        deleted.delete();
        // getting all
        assertThat(Employee.findAll(), empty());
    }

}