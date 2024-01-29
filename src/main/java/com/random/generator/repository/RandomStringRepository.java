package com.random.generator.repository;


import com.random.generator.entity.RandomString;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomStringRepository extends JpaRepository<RandomString, Long> {
}
