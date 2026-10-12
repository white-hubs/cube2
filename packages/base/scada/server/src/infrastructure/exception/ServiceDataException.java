package com.guhecloud.kaihe.scada.server.infrastructure.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ServiceDataException extends ServiceException {

    private static final long serialVersionUID = 1L;

    Map<String,String> errors ;

   public ServiceDataException(Map<String,String> errors) {
       super();
       this.errors = errors;
   }

}

