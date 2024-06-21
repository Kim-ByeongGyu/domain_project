package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VelogRepository extends JpaRepository<Velog, Long> {
    public List<Velog> findByOwner(User owner);
    public Velog findByName(String name);
}
