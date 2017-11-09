package com.openkx.kxexam.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.openkx.kxexam.util.ID;
import com.openkx.kxexam.util.Time;

@Entity
public class Exam {

	@Id
	private String id;
	
	private String papername;
	
	private String paper_json;
	
	@Transient
	private List<String> papers;
	
	@ManyToOne
	private Classify classify;
	
	private Timestamp ctime;
	
	public Exam(){
		this.id = ID.uuid();
		this.ctime = Time.timestamp();
	}
	
	public Exam(String papername){
		this.id = ID.uuid();
		this.papername = papername;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPapername() {
		return papername;
	}

	public void setPapername(String papername) {
		this.papername = papername;
	}

	public String getPaper_json() {
		return paper_json;
	}

	public void setPaper_json(String paper_json) {
		this.paper_json = paper_json;
	}

	public List<String> getPapers() {
		papers = JSON.parseArray(this.getPaper_json(), String.class);
		return papers;
	}

	public void setPapers(List<String> papers) {
		this.papers = papers;
		this.paper_json = JSON.toJSONString(papers);
	}

	public Timestamp getCtime() {
		return ctime;
	}

	public void setCtime(Timestamp ctime) {
		this.ctime = ctime;
	}

	public Classify getClassify() {
		return classify;
	}

	public void setClassify(Classify classify) {
		this.classify = classify;
	}
	
	
}