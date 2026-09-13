package com.epbank.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/* Annotation Definitions
@Entity 
- Tells JPA that this class represent an entity that can be stored in the database.

@Table(name = "customers") 
- Maps this Customer class to the "customers" table in PostgreSQL.

@Id 
- Marks this field as the primary key of the database table. 

@GeneratedValue(strategy = GenerationType.IDENTITY) 
- Tells JPA that the database is responsible for automatically generating the primary key value 
when a new customer is inserted. 

@Column 
- Maps a Java Field to a column in the database table. 

@Column(name = "first_name", nullable = false)
- Maps firstName to the "first_name" database column.
- nullable = false means the database does not allow this value to be NULL.

@Column(nullable = false, unique = true)
- nullable = false means the value cannot be NULL.
- unique = true means no two customers can have the same value.
  In this case, every customer must have a unique email. 

@PrePersist
- Tells JPA to run this method automatically before a new Customer 
is inserted into the database.  
*/

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
