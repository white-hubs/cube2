package com.guhecloud.cube.deliver.project.merchant.commons.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

}
