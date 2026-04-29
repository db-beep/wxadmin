package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 反馈与建议实体类 */
@TableName("tb_fankui")
public class Fankui implements Serializable {

  /** 反馈与建议主键 */
  @TableId private String id;

  /** 反馈人 */
  @TableField("userid")
  private String userid;

  @TableField(exist = false)
  private User useridFrn;
  /** 机构 */
  @TableField("inid")
  private String inid;

  @TableField(exist = false)
  private Institution inidFrn;
  /** 反馈内容 */
  @TableField("title")
  private String title;

  /** 反馈时间 */
  @TableField("createtime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm",
      fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd"})
  private Date createtime;

  /** 回复状态 */
  @TableField("state")
  private String state;

  /** 回复内容 */
  @TableField("content")
  private String content;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public User getUseridFrn() {
    return useridFrn;
  }

  public void setUseridFrn(User v) {
    this.useridFrn = v;
  }

  public String getInid() {
    return inid;
  }

  public void setInid(String inid) {
    this.inid = inid;
  }

  public Institution getInidFrn() {
    return inidFrn;
  }

  public void setInidFrn(Institution v) {
    this.inidFrn = v;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

