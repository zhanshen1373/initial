package com.example.faceall.facealldetect;

import org.json.JSONArray;

public class WeatherObject {
	private String pm25;
	private String temperature;
	private String condition;
	private int is_xianxing;
	private JSONArray weihao;
	private String cityName;
	private String code;
	
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getIs_xianxing() {
		return is_xianxing;
	}
	public void setIs_xianxing(int is_xianxing) {
		this.is_xianxing = is_xianxing;
	}
	public JSONArray getWeihao() {
		return weihao;
	}
	public void setWeihao(JSONArray weihao) {
		this.weihao = weihao;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
