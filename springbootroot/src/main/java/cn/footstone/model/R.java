package cn.footstone.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer NO_LOGIN = -1;

	public static final Integer SUCCESS = 1;

	public static final Integer FAIL = -2;

	public static final Integer NO_PERMISSION = -3;

	public static final Integer ERROR = -4;

	public static final String MESSAGE = "success";

	private String message = "success";

	private int status = SUCCESS;

	private T data;

	public R() {
	}

	public R(T data) {
		this.data = data;
	}

	public R(String message, T data) {
		super();
		this.data = data;
		this.message = message;
	}

	public R(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public R(int status, String message, T data) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

}
