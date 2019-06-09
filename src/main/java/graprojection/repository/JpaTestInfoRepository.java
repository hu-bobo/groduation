package graprojection.repository;

import graprojection.jpa.model.JpaTestInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTestInfoRepository extends JpaRepository<JpaTestInfo, Long> {
    //@Override
    List<JpaTestInfo> findAll();
}
