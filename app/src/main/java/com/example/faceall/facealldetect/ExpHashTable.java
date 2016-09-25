package com.example.faceall.facealldetect;

import java.util.Hashtable;

public class ExpHashTable extends Hashtable<String,String> {
	
	public ExpHashTable() {
		this.put("Angry", "生气");
		this.put("Disgust","反感");
		this.put("Fear","害怕");
		this.put("Happy", "高兴");
		this.put("Sad", "悲伤");
		this.put("Surprise", "吃惊");
		this.put("Neutral", "中性");
		this.put("male", "爷们儿");
		this.put("female", "姑娘");
		this.put("White","白种人");
		this.put("Yellow", "黄种人");
		this.put("Black/Brown", "黑／棕种人");
	}

}
