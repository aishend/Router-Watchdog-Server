package com.routerwatchdog.commands;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommandRepository extends JpaRepository<CommandEntity, String> {

    List<CommandEntity> findAllByOrderByCreatedAtDesc();
}