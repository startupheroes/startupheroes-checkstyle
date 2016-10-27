package startupheroes.checkstyle.checks.custom;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_pmi_av1_av2_av3", // not correct naming for uk!
        columnNames = {"productModelId", "attributeValue_1", "attributeValue_2", "attributeValue_3"})
},
    indexes = {
        @Index(
            name = "sku_index", // not correct naming for index!
            columnList = "sku"
        )})
class TestWrongEntity {

   @Id
   @Column // redundant with Id annotation!
   @GeneratedValue
   private Integer productId; // name as direct 'id' the generated column.

   /**
    * can be the same for all variants, depends on shop owner
    */
   @Column // redundant with Id annotation!
   @javax.persistence.Id
   private String sku;

   @javax.persistence.Column(nullable = false)
   private Integer productModelId;

   @Column
   private String attributeValue_1;

   @Column
   private String attributeValue_2;

   @Column
   private String attributeValue_3;

   /**
    * can be the same for all variants, depends on shop owner
    */
   @Column(nullable = false)
   private String productName; // wrong naming, name as "name" without context!

   /**
    * can be the same for all variants, depends on shop owner
    */
   @Column(nullable = false, unique = true) // unique should not be here, define inside @Table annotation.
   private String relativeUrl;

   @Column(nullable = false)
   private Integer searchBoost;

   @Column(length = 2048, nullable = true) // redundant default assign!
   private String searchKeywords;

   @Column // created at and last updated at columns must be nullable = false!
   private Date createdAt;

   // no lastUpdatedAt column!

   @Column(insertable = true, updatable = true, length = 255)
   private Date defaultValue;

   public Integer getProductId() {
      return productId;
   }

   public void setProductId(Integer productId) {
      this.productId = productId;
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public Integer getProductModelId() {
      return productModelId;
   }

   public void setProductModelId(Integer productModelId) {
      this.productModelId = productModelId;
   }

   public String getAttributeValue_1() {
      return attributeValue_1;
   }

   public void setAttributeValue_1(String attributeValue_1) {
      this.attributeValue_1 = attributeValue_1;
   }

   public String getAttributeValue_2() {
      return attributeValue_2;
   }

   public void setAttributeValue_2(String attributeValue_2) {
      this.attributeValue_2 = attributeValue_2;
   }

   public String getAttributeValue_3() {
      return attributeValue_3;
   }

   public void setAttributeValue_3(String attributeValue_3) {
      this.attributeValue_3 = attributeValue_3;
   }

   // no getters setters for product name column!

   public String getRelativeUrl() {
      return relativeUrl;
   }

   public void setRelativeUrl(String relativeUrl) {
      this.relativeUrl = relativeUrl;
   }

   public Integer getSearchBoost() {
      return searchBoost;
   }

   public void setSearchBoost(Integer searchBoost) {
      this.searchBoost = searchBoost;
   }

   public String getSearchKeywords() {
      return searchKeywords;
   }

   public void setSearchKeywords(String searchKeywords) {
      this.searchKeywords = searchKeywords;
   }

   public Date getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
   }

   public Date getDefaultValue() {
      return defaultValue;
   }

   public void setDefaultValue(Date defaultValue) {
      this.defaultValue = defaultValue;
   }

   // no toString() method!

}
