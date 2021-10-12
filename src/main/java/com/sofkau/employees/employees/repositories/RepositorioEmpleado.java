package com.sofkau.employees.employees.repositories;


import com.sofkau.employees.employees.entities.Empleado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioEmpleado extends MongoRepository<Empleado, String> {
}

