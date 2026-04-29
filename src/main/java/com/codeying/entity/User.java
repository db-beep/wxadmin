package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 用户实体类 */
@TableName("tb_user")
public class User extends LoginUser implements Serializable {

  public User() {
    role = "user";
    rolech = "用户";
    isWuser = true;
  }

  /** 用户主键 */
  @TableId private String id;

  /** 用户名 */
  @TableField("username")
  private String username;

  /** 密码 */
  @TableField("password")
  private String password;

  /** 姓名 */
  @TableField("name")
  private String name;

  /** 头像 */
  @TableField("avatar")
  private String avatar;

  /** 性别 */
  @TableField("gender")
  private String gender;

  /** 兴趣标签 */
  @TableField("labels")
  private String labels;

  @TableField(exist = false)
  private List<CultureType> labelsLabels;

  @TableField(exist = false)
  private String labelsLabelStr;
  /** 年龄 */
  @TableField("age")
  private Integer age;

  /** 电话 */
  @TableField("tele")
  private String tele;

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

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getLabels() {
    return labels;
  }

  public void setLabels(String labels) {
    this.labels = labels;
  }

  public List<CultureType> getLabelsLabels() {
    return labelsLabels;
  }

  public String getLabelsLabelStr() {
    return labelsLabelStr;
  }

  public void setLabelsLabels(List<CultureType> v) {
    this.labelsLabels = v;
    if (v == null) return;
    String s = "";
    for (CultureType label : this.labelsLabels) {
      if (s.length() > 0) {
        s += ",";
      }
      s += label.getName();
    }
    labelsLabelStr = s;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getTele() {
    return tele;
  }

  public void setTele(String tele) {
    this.tele = tele;
  }
}

