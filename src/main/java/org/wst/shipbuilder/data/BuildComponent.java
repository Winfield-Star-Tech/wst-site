package org.wst.shipbuilder.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BuildComponent {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	
	private int requiredNum;
	
	private int currentNum;
	
	@ManyToOne
	private BuildItem buildItem;

	public BuildItem getBuildItem() {
		return buildItem;
	}

	public void setBuildItem(BuildItem buildItem) {
		this.buildItem = buildItem;
	}

	protected BuildComponent() {}
	
	public BuildComponent(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRequiredNum() {
		return requiredNum;
	}

	public void setRequiredNum(int requiredNum) {
		this.requiredNum = requiredNum;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}
}
