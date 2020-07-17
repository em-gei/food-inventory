package com.project.foodinventory.dtos;

public class FoodDTO {
	
	private int id;
	private String name;
	
	public FoodDTO(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "FoodDTO [id=" + id + ", name=" + name + "]";
	}
	
	
}
