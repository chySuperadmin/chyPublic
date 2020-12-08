package test.chy.enums;

public enum TestConstans {
	PARAMTERCODE(10000,"³É¹¦");

	private int code;
	private String value;
	
	TestConstans(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
