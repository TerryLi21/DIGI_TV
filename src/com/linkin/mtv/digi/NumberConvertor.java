package com.linkin.mtv.digi;

/**
 * Created by sonnet on 3/26/15.
 */
public class NumberConvertor {

	private String banglaNumber = "";
	private String englishNumber;

	public NumberConvertor(String number) {
		englishNumber = number;
	}

	public String getBanglaNumber() {
		for (int i = 0; i < englishNumber.length(); i++) {
			convert(Integer.parseInt(englishNumber.charAt(i) + ""));
		}
		return new StringBuilder(banglaNumber).reverse().toString();
	}

	private void convert(int r) {
		switch (r) {
		case 1:
			banglaNumber = "১" + banglaNumber;
			break;

		case 2:
			banglaNumber = "২" + banglaNumber;
			break;

		case 3:
			banglaNumber = "৩" + banglaNumber;
			break;

		case 4:
			banglaNumber = "৪" + banglaNumber;
			break;

		case 5:
			banglaNumber = "৫" + banglaNumber;
			break;

		case 6:
			banglaNumber = "৬" + banglaNumber;
			break;

		case 7:
			banglaNumber = "৭" + banglaNumber;
			break;

		case 8:
			banglaNumber = "৮" + banglaNumber;
			break;

		case 9:
			banglaNumber = "৯" + banglaNumber;
			break;

		case 0:
			banglaNumber = "০" + banglaNumber;
			break;
		}
	}

}
