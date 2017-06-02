package vntu.itcgs.dto;

import lombok.Data;
import vntu.itcgs.model.Organization;

@Data
public class OrganizationDetails {

	private String id;
	private String name;

	private Integer cooperationValue = 0;

	public OrganizationDetails(Organization organization) {
		this.id = organization.getId();
		this.name = organization.getName();
	}

}
