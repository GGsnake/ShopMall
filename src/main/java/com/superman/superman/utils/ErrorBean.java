package com.superman.superman.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liujupeng on 2018/11/19.
 */
@Getter
@Setter
@ToString
public class ErrorBean<T>  {

        public static final Integer OK = 0;
        public static final Integer ERROR = 100;

        private Integer code;
        private String message;
        private String url;
        private T data;


}
