package graprojection.repository;

import graprojection.jpa.model.TableUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableUserInfoRepository extends JpaRepository<TableUserInfo, Long> {
    TableUserInfo findByUsername(String userName);

    Page<TableUserInfo> findAll(Specification querySpecifi, Pageable pageable);

    TableUserInfo findByUserid(String userid);
}
