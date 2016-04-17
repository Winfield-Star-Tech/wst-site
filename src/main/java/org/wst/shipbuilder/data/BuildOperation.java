package org.wst.shipbuilder.data;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BuildOperation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private int fundsRaised;
	
	private String name;
	
	private String currentStepName;
	
	private Date lastUpdateTime;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "operation")
	private Set<BuildItem> buildItems;
	
	public Set<BuildItem> getBuildItems() {
		return buildItems;
	}
	
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setBuildItems(Set<BuildItem> buildItems) {
		this.buildItems = buildItems;
	}

	public void addBuildItem(BuildItem i) {
		buildItems.add(i);
	}
	
	protected BuildOperation(){}
	
	public BuildOperation(String pName, String pStepName) {
		lastUpdateTime = new Date();
		fundsRaised = 0;
		name = pName;
		currentStepName = pStepName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFundsRaised() {
		return fundsRaised;
	}

	public void setFundsRaised(int fundsRaised) {
		this.fundsRaised = fundsRaised;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentStepName() {
		return currentStepName;
	}

	public void setCurrentStepName(String currentStepName) {
		this.currentStepName = currentStepName;
	}
	
}
