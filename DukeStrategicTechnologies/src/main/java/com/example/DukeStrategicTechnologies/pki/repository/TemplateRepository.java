package com.example.DukeStrategicTechnologies.pki.repository;

import com.example.DukeStrategicTechnologies.pki.model.Template;
import com.example.DukeStrategicTechnologies.pki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

}
