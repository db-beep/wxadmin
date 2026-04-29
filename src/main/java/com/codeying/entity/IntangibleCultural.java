package com.codeying.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import java.io.Serializable;
/** 非遗文化科普实体类 */
@TableName("tb_intangibleCultural")
public class IntangibleCultural implements Serializable {

  // ======================
  @TableField(exist = false)
  private int starCount;

  @TableField(exist = false)
  private int praiseCount;

  @TableField(exist = false)
  private String smallTip; // 提示

  @TableField(exist = false)
  private double recommendScore; // 推荐系数
  // ======================

  /** 非遗文化科普主键 */
  @TableId private String id;

  /** 科普首图 */
  @TableField("showpic")
  private String showpic;

  /** 非遗文化标题 */
  @TableField("showtitle")
  private String showtitle;

  /** 发布机构 */
  @TableField("fabjg")
  private String fabjg;

  @TableField(exist = false)
  private Institution fabjgFrn;
  /** 非遗文化标签 */
  @TableField("labels")
  private String labels;

  @TableField(exist = false)
  private List<CultureType> labelsLabels;

  @TableField(exist = false)
  private String labelsLabelStr;
  /** 非遗文化简述 */
  @TableField("showdesc")
  private String showdesc;

  /** 科普内容详情 */
  @TableField("showdetail")
  private String showdetail;

  /** 发布时间 */
  @TableField("publishtime")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm",
      fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd"})
  private Date publishtime;

  /** 非遗文化展示 */
  @TableField("vv")
  private String vv;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getShowpic() {
    return showpic;
  }

  public void setShowpic(String showpic) {
    this.showpic = showpic;
  }

  public String getShowtitle() {
    return showtitle;
  }

  public void setShowtitle(String showtitle) {
    this.showtitle = showtitle;
  }

  public String getFabjg() {
    return fabjg;
  }

  public void setFabjg(String fabjg) {
    this.fabjg = fabjg;
  }

  public Institution getFabjgFrn() {
    return fabjgFrn;
  }

  public void setFabjgFrn(Institution v) {
    this.fabjgFrn = v;
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

  public String getShowdesc() {
    return showdesc;
  }

  public void setShowdesc(String showdesc) {
    this.showdesc = showdesc;
  }

  public String getShowdetail() {
    return showdetail;
  }

  public void setShowdetail(String showdetail) {
    this.showdetail = showdetail;
  }

  public Date getPublishtime() {
    return publishtime;
  }

  public void setPublishtime(Date publishtime) {
    this.publishtime = publishtime;
  }

  public String getVv() {
    return vv;
  }

  public void setVv(String vv) {
    this.vv = vv;
  }

  public int getStarCount() {
    return starCount;
  }

  public void setStarCount(int starCount) {
    this.starCount = starCount;
  }

  public int getPraiseCount() {
    return praiseCount;
  }

  public void setPraiseCount(int praiseCount) {
    this.praiseCount = praiseCount;
  }

  public String getSmallTip() {
    return smallTip;
  }

  public void setSmallTip(String smallTip) {
    this.smallTip = smallTip;
  }

  public double getRecommendScore() {
    return recommendScore;
  }

  public void setRecommendScore(double recommendScore) {
    this.recommendScore = recommendScore;
  }
}

