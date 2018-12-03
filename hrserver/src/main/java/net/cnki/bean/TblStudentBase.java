package net.cnki.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TblStudentBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer sid;

    private String username;

    private String password;

    private Integer schoolNum;

    private Integer studentNum;

    private String studentName;

    private String mobile;

    private String email;

    private String status;

    private String loginToken;

    private Date createTime;

    private String cid;

    private String appKey;

    private Date updatetime;

    private List<Role> roles;



}