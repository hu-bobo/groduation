package graprojection.repository;

import graprojection.entity.FpointDTO;
import graprojection.jpa.model.FpointInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FpointInfoRepository extends JpaRepository<FpointInfo, Long> {
    Page<FpointInfo> findAll(Specification querySpecifi, Pageable pageable);

    FpointInfo findByGid(Long gid);

    //jpa
    @Query(value = "SELECT f.category, COUNT(f.category)  FROM fpoing_bl f where (f.time between ?1 and ?2) GROUP BY f.category",nativeQuery = true)
    List<Object[]> findData(java.util.Date startTime, java.util.Date endTime);

    @Query(value = "SELECT f.optuser, COUNT(f.optuser)  FROM fpoing_bl f where (f.time between ?1 and ?2) GROUP BY f.optuser",nativeQuery = true)
    List<Object[]> finduserData(java.util.Date startTime, java.util.Date endTime);
}
