package education.openapi.codefirst.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Objects;


public class Balance
{
    public String balance;

  public Balance()
  {
  }

  public Balance(String balance)
  {
    this.balance = balance;
  }
}

