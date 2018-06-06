package com.github.brosgarden.java8;

import java.util.Objects;

public class Bean {

  private long id;
  private long otherId;
  private String value;

  public Bean(long id, long otherId, String value) {
    this.id = id;
    this.otherId = otherId;
    this.value = value;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getOtherId() {
    return otherId;
  }

  public void setOtherId(long otherId) {
    this.otherId = otherId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Bean)) {
      return false;
    }
    Bean bean = (Bean) o;
    return id == bean.id && otherId == bean.otherId && Objects.equals(value, bean.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, otherId, value);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Bean{");
    sb.append("id=").append(id);
    sb.append(", otherId=").append(otherId);
    sb.append(", value='").append(value).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
