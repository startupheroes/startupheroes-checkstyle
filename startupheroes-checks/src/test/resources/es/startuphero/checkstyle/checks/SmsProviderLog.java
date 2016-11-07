package es.startuphero.checkstyle.checks;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "sms_provider_log_sms_id_type_index", columnList = "smsId, type"))
public class SmsProviderLog {

   @Id
   @GeneratedValue
   private Integer id;

   @Column(nullable = false)
   private Integer userId;

   @Column
   private Integer smsId;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private SmsProviderRequestType type;

   @Column(length = 1024, nullable = false)
   private String request;

   @Column(length = 1024)
   private String response;

   @Column(nullable = false)
   private Date requestedAt;

   @Column
   private Date respondedAt;

   @Column(length = 1024, nullable = false)
   private String uri;

   @Column(nullable = false)
   private String method;

   public Integer getSmsId() {
      return smsId;
   }

   public void setSmsId(Integer smsId) {
      this.smsId = smsId;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public SmsProviderRequestType getType() {
      return type;
   }

   public void setType(SmsProviderRequestType type) {
      this.type = type;
   }

   public String getRequest() {
      return request;
   }

   public void setRequest(String request) {
      this.request = request;
   }

   public String getResponse() {
      return response;
   }

   public void setResponse(String response) {
      this.response = response;
   }

   public Date getRequestedAt() {
      return requestedAt;
   }

   public void setRequestedAt(Date requestedAt) {
      this.requestedAt = requestedAt;
   }

   public Date getRespondedAt() {
      return respondedAt;
   }

   public void setRespondedAt(Date respondedAt) {
      this.respondedAt = respondedAt;
   }

   public String getUri() {
      return uri;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public String getMethod() {
      return method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   @Override
   public String toString() {
      return "SmsProviderLog{" +
             "id=" + id +
             ", smsId=" + smsId +
             ", userId=" + userId +
             ", type=" + type +
             ", request='" + request + '\'' +
             ", response='" + response + '\'' +
             ", requestedAt=" + requestedAt +
             ", respondedAt=" + respondedAt +
             ", uri='" + uri + '\'' +
             ", method='" + method + '\'' +
             '}';
   }

   private enum SmsProviderRequestType {

      SEND_SMS,
      QUERY_SMS,
      SUBSCRIBE,

   }

}
