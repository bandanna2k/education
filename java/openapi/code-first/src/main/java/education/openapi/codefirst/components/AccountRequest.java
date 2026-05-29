package education.openapi.codefirst.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotNull;
import java.util.Objects;


@JsonTypeName("AccountRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public class AccountRequest   {
  private Long accountId;

  /**
   **/
  public AccountRequest accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  
  @JsonProperty("accountId")
  @NotNull public Long getAccountId() {
    return accountId;
  }

  @JsonProperty("accountId")
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountRequest accountRequest = (AccountRequest) o;
    return Objects.equals(this.accountId, accountRequest.accountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountRequest {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
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

