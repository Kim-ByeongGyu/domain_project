package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.Velog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VelogRepository extends JpaRepository<Velog, Long> {
}
