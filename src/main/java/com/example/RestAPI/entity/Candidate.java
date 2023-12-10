package com.example.RestAPI.entity;

public class Candidate {
	
	private String name;
    private int voteCount;
    
    public Candidate(String name, int i) {
		this.name = name;
		this.voteCount= i;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
    
    
}
