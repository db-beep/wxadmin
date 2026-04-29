package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 非遗活动报名实体类 */
@TableName("tb_baom")
public class Baom implements Serializable {

  /** 非遗活动报名主键 */
  @TableId private String id;

  /** 报名人 */
  @TableField("userid")
  private String userid;

  @TableField(exist = false)
  private User useridFrn;
  /** 报名活动 */
  @TableField("acid")
  private String acid;

  @TableField(exist = false)
  private CulActivi acidFrn;
  /** 非遗机构 */
  @TableField("jig")
  private String jig;

  @TableField(exist = false)
  private Institution jigFrn;
  /** 报名备注 */
  @TableField("remark")
  private String remark;

  /** 报名时间 */
  @TableField("createtime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm",
      fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd"})
  private Date createtime;

  /** 审核 */
  @TableField("state")
  private String state;

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

  public String getAcid() {
    return acid;
  }

  public void setAcid(String acid) {
    this.acid = acid;
  }

  public CulActivi getAcidFrn() {
    return acidFrn;
  }

  public void setAcidFrn(CulActivi v) {
    this.acidFrn = v;
  }

  public String getJig() {
    return jig;
  }

  public void setJig(String jig) {
    this.jig = jig;
  }

  public Institution getJigFrn() {
    return jigFrn;
  }

  public void setJigFrn(Institution v) {
    this.jigFrn = v;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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
}

