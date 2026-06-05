package education.openapi.model;

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

/**
 * Request object for deposit and withdrawal transactions
 **/
@ApiModel(description = "Request object for deposit and withdrawal transactions")
@JsonTypeName("TransactionRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public class TransactionRequest   {
  private Integer accountId;
  private String amount;

  /**
   * The unique account identifier
   **/
  public TransactionRequest accountId(Integer accountId) {
    this.accountId = accountId;
    return this;
  }

  
  @ApiModelProperty(example = "123", value = "The unique account identifier")
  @JsonProperty("accountId")
  public Integer getAccountId() {
    return accountId;
  }

  @JsonProperty("accountId")
  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  /**
   * The transaction amount as a string
   **/
  public TransactionRequest amount(String amount) {
    this.amount = amount;
    return this;
  }

  
  @ApiModelProperty(example = "50.0", value = "The transaction amount as a string")
  @JsonProperty("amount")
  public String getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(String amount) {
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

