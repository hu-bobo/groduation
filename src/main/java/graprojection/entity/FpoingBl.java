package graprojection.entity;


import java.util.Date;

public class FpoingBl {

  private long gid;
  private String name;
  private String optuser;
  private String address;
  private String wgs84Lng;
  private String wgs84Lat;
  private long geom;
  private String remark;
  private Date time;
  private String status;
  private String category;
  private String url;


  public long getGid() {
    return gid;
  }

  public void setGid(long gid) {
    this.gid = gid;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getOptuser() {
    return optuser;
  }

  public void setOptuser(String optuser) {
    this.optuser = optuser;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getWgs84Lng() {
    return wgs84Lng;
  }

  public void setWgs84Lng(String wgs84Lng) {
    this.wgs84Lng = wgs84Lng;
  }


  public String getWgs84Lat() {
    return wgs84Lat;
  }

  public void setWgs84Lat(String wgs84Lat) {
    this.wgs84Lat = wgs84Lat;
  }


  public long getGeom() {
    return geom;
  }

  public void setGeom(long geom) {
    this.geom = geom;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
