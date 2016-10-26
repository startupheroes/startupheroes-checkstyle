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
        name = "test_correct_entity_pmi_av1_av2_av3_uk",
        columnNames = {"productModelId", "attributeValue_1", "attributeValue_2", "attributeValue_3"}),
    @UniqueConstraint(name = "test_entity_relative_url_uk", columnNames = {"relativeUrl"})
},
    indexes = {
        @Index(
            name = "test_entity_sku_index",
            columnList = "userId"
        )})
class TestCorrectEntity {

   @Id
   @GeneratedValue
   private Integer id;

   /**
    * can be the same for all variants, depends on shop owner
    */
   @Column
   private String sku;

   @Column(nullable = false)
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
   private String name;

   /**
    * can be the same for all variants, depends on shop owner
    */
   @Column(nullable = false)
   private String relativeUrl;

   @Column(nullable = false)
   private Integer searchBoost;

   @Column(length = 2048)
   private String searchKeywords;

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

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

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

   public Date getLastUpdatedAt() {
      return lastUpdatedAt;
   }

   public void setLastUpdatedAt(Date lastUpdatedAt) {
      this.lastUpdatedAt = lastUpdatedAt;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TestCorrectEntity that = (TestCorrectEntity) o;

      return id != null ? id.equals(that.id) : that.id == null;
   }

   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }

   @Override
   public String toString() {
      return "TestCorrectEntity{" +
             "id=" + id +
             ", sku='" + sku + '\'' +
             ", productModelId=" + productModelId +
             ", attributeValue_1='" + attributeValue_1 + '\'' +
             ", attributeValue_2='" + attributeValue_2 + '\'' +
             ", attributeValue_3='" + attributeValue_3 + '\'' +
             ", name='" + name + '\'' +
             ", relativeUrl='" + relativeUrl + '\'' +
             ", searchBoost=" + searchBoost +
             ", searchKeywords='" + searchKeywords + '\'' +
             ", createdAt=" + createdAt +
             ", lastUpdatedAt=" + lastUpdatedAt +
             '}';
   }

}