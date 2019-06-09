package graprojection.jpa.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name ="fpoing_bl")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Data
public class FpointInfo {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "gid",unique = true, nullable = false)
    private Long gid;
    private String name;
    private String optuser;
    private String wgs84_lng;
    private String wgs84_lat;
    //private long geom;
    private String address;
    private String remark;
    private String status;
    private String url;
    private String category;
    private Date time;


    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
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


    public String getWgs84Lng() {
        return wgs84_lng;
    }

    public void setWgs84Lng(String wgs84Lng) {
        this.wgs84_lng = wgs84_lng;
    }


    public String getWgs84Lat() {
        return wgs84_lat;
    }

    public void setWgs84Lat(String wgs84Lat) {
        this.wgs84_lat = wgs84_lat;
    }


//    public long getGeom() {
//        return geom;
//    }
//
//    public void setGeom(long geom) {
//        this.geom = geom;
//    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
