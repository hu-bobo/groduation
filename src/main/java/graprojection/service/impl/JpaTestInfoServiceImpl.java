package graprojection.service.impl;

import graprojection.jpa.model.JpaTestInfo;
import graprojection.repository.JpaTestInfoRepository;
import graprojection.service.JpaTestInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JpaTestInfoServiceImpl implements JpaTestInfoService {
    @Autowired
    private JpaTestInfoRepository jpaTestInfoRepository;

    @Override
    public List<JpaTestInfo> getAll(){
        return jpaTestInfoRepository.findAll();
    }

}
