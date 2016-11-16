package es.startuphero.checkstyle.checks;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author ozlem.ulag
 */
@MappedSuperclass
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "objectId"})})
public class NotHaveAbstractName {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false)
  private Integer userId;

  @Column(nullable = false)
  private Integer objectId;

  @Column(nullable = false)
  private Date createdAt;

  @Column(nullable = false)
  private Date lastUpdatedAt;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getObjectId() {
    return objectId;
  }

  public void setObjectId(Integer objectId) {
    this.objectId = objectId;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public void setLastUpdatedAt(Date lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @Override
  public String toString() {
    return "NotHaveAbstractName{" +
        "id=" + id +
        ", userId=" + userId +
        ", objectId=" + objectId +
        ", createdAt=" + createdAt +
        ", lastUpdatedAt=" + lastUpdatedAt +
        '}';
  }
}
