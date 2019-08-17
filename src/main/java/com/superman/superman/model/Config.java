package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class Config implements Serializable {
    private Integer id;
    private String ConfigNo;
    private String ConfigName;
    private String ConfigValue;
    private String Remark ;
    private String EditBy ;
    private String EditTime ;
}
