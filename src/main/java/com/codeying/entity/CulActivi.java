package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 非遗传承活动实体类 */
@TableName("tb_culActivi")
public class CulActivi implements Serializable {

  /** 非遗传承活动主键 */
  @TableId private String id;

  /** 活动名称 */
  @TableField("name")
  private String name;

  /** 主办机构 */
  @TableField("zhubf")
  private String zhubf;

  @TableField(exist = false)
  private Institution zhubfFrn;
  /** 活动内容 */
  @TableField("content")
  private String content;

  /** 报名说明 */
  @TableField("baomsm")
  private String baomsm;

  /** 活动开始时间 */
  @TableField("starttime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm",
      fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd"})
  private Date starttime;

  /** 结束时间 */
  @TableField("endtime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm",
      fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd"})
  private Date endtime;

  /** 限制报名人数 */
  @TableField("count0")
  private Integer count0;

  /** 已报名人数 */
  @TableField("count")
  private Integer count;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getZhubf() {
    return zhubf;
  }

  public void setZhubf(String zhubf) {
    this.zhubf = zhubf;
  }

  public Institution getZhubfFrn() {
    return zhubfFrn;
  }

  public void setZhubfFrn(Institution v) {
    this.zhubfFrn = v;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getBaomsm() {
    return baomsm;
  }

  public void setBaomsm(String baomsm) {
    this.baomsm = baomsm;
  }

  public Date getStarttime() {
    return starttime;
  }

  public void setStarttime(Date starttime) {
    this.starttime = starttime;
  }

  public Date getEndtime() {
    return endtime;
  }

  public void setEndtime(Date endtime) {
    this.endtime = endtime;
  }

  public Integer getCount0() {
    return count0;
  }

  public void setCount0(Integer count0) {
    this.count0 = count0;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}

