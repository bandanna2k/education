package education.openapi.specfirst.generated.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("TransactionRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public class TransactionRequest   {
  private Long accountId;
  private Double amount;

  /**
   **/
  public TransactionRequest accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("accountId")
  @NotNull public Long getAccountId() {
    return accountId;
  }

  @JsonProperty("accountId")
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  /**
   **/
  public TransactionRequest amount(Double amount) {
    this.amount = amount;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("amount")
  @NotNull public Double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Double amount) {
    this.amount = amount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionRequest transactionRequest = (TransactionRequest) o;
    return Objects.equals(this.accountId, transactionRequest.accountId) &&
        Objects.equals(this.amount, transactionRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionRequest {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

