package es.startupheroes.checkstyle.checks;

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
public abstract class AbstractUserListItem {

   @Id
   @GeneratedValue
   private Integer userListItemId;

   @Column(nullable = false)
   private Integer userId;

   @Column(nullable = false)
   private Integer objectId;

   @Column(nullable = false)
   private Date createdAt;

   @Column
   private Date viewedAt;

   public Integer getUserListItemId() {
      return userListItemId;
   }

   public void setUserListItemId(Integer userListItemId) {
      this.userListItemId = userListItemId;
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

   public Date getViewedAt() {
      return viewedAt;
   }

   public void setViewedAt(Date viewedAt) {
      this.viewedAt = viewedAt;
   }

   @Override
   public String toString() {
      return "AbstractUserListItem{" +
             "userListItemId=" + userListItemId +
             ", userId=" + userId +
             ", objectId=" + objectId +
             ", createdAt=" + createdAt +
             ", viewedAt=" + viewedAt +
             '}';
   }
}
