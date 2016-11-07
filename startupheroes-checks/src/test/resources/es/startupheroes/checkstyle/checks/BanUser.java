package es.startupheroes.checkstyle.checks;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author obasar
 * @author onurozcan
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "ban_user_user_id_object_id_uk", columnNames = {"userId", "objectId"})})
public class BanUser extends AbstractUserListItem {

   private String reason;

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }

}
