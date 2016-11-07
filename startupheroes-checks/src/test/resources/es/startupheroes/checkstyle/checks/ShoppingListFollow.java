package es.startupheroes.checkstyle.checks;

import java.util.Date;
import javax.persistence.Entity;

/**
 * @author ozlem.ulag
 */
@Entity
public class ShoppingListFollow extends AbstractUserListItem {

   public static ShoppingListFollow newInstance(Integer userId, Integer shoppingListId) {
      ShoppingListFollow shoppingListFollow = new ShoppingListFollow();
      shoppingListFollow.setUserId(userId);
      shoppingListFollow.setObjectId(shoppingListId);
      shoppingListFollow.setCreatedAt(new Date());
      return shoppingListFollow;
   }

}
