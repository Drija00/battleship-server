package main.java.domain;

import java.io.Serializable;
import java.sql.ResultSet;

public interface GenericEntity extends Serializable {
    String getClassName();
    String getAtrValues();
    String getAtrNames();
    String setAtrValues();
    String getWhereCondition();
    GenericEntity getNewRecord(ResultSet rs);
    Long getId();

}
