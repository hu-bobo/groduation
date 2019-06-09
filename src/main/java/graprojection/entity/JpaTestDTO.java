package graprojection.entity;

import lombok.Data;

import java.util.Date;
@Data
public class JpaTestDTO {

  private String id;
  private Date age;
  private String name;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public Date getAge() {
    return age;
  }

  public void setAge(Date age) {
    this.age = age;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
