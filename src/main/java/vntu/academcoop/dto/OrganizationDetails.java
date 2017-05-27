package vntu.academcoop.dto;

import vntu.academcoop.model.Organization;

public class OrganizationDetails {

	private String id;
	private String name;

	private Integer cooperationValue = 0;

	public OrganizationDetails(Organization organization) {
		this.id = organization.getId();
		this.name = organization.getName();
	}

	public OrganizationDetails() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCooperationValue() {
		return cooperationValue;
	}

	public void setCooperationValue(Integer cooperationValue) {
		this.cooperationValue = cooperationValue;
	}

}
