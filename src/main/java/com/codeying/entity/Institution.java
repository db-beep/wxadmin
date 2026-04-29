package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 非遗机构实体类 */
@TableName("tb_institution")
public class Institution extends LoginUser implements Serializable {

  public Institution() {
    role = "institution";
    rolech = "非遗机构";
    isWuser = false;
  }

  /** 非遗机构主键 */
  @TableId private String id;

  /** 用户名 */
  @TableField("username")
  private String username;

  /** 密码 */
  @TableField("password")
  private String password;

  /** 非遗机构名称 */
  @TableField("name")
  private String name;

  /** 机构头像 */
  @TableField("avatar")
  private String avatar;

  /** 电话 */
  @TableField("tele")
  private String tele;

  /** 地址 */
  @TableField("place")
  private String place;

  /** 机构简介 */
  @TableField("description")
  private String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getTele() {
    return tele;
  }

  public void setTele(String tele) {
    this.tele = tele;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

