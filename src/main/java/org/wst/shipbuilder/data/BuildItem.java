package org.wst.shipbuilder.data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class BuildItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;

	@ManyToOne
	private BuildOperation operation;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "buildItem")
	private Set<BuildComponent> buildComponents;
	
	public BuildOperation getOperation() {
		return operation;
	}

	public void setOperation(BuildOperation operation) {
		this.operation = operation;
	}

	public Set<BuildComponent> getBuildComponents() {
		return buildComponents;
	}
	
	public void addBuildComponent(BuildComponent c) {
		buildComponents.add(c);
	}
	
	public void setBuildComponents(Set<BuildComponent> buildComponents) {
		this.buildComponents = buildComponents;
	}

	protected BuildItem() {}
	
	public BuildItem(String name) {
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
}
