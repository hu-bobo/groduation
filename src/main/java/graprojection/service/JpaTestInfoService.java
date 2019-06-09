package graprojection.service;

import graprojection.jpa.model.JpaTestInfo;

import java.util.List;

public interface JpaTestInfoService {
    List<JpaTestInfo> getAll();
}
