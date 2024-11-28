package model;
/**
 * optionDto
 */
public class OptionDto {
	private String option;
	private String value;
	public String getOption() {
		return option;
	}
	public void setIOption(String option) {
		this.option = option;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void printTag() {
		System.out.println("option:" + option);
		System.out.println("value:" + value);
	}
}