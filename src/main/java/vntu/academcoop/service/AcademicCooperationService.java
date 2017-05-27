package vntu.academcoop.service;

import java.util.Date;

import vntu.academcoop.dto.CooperationNetwork;

public interface AcademicCooperationService {
	
	CooperationNetwork fetchCoAuthorsCooperationNetwork(String organizationName);
	
	CooperationNetwork fetchPublicationsCooperationNetwork(String organizationName, Date fromYear, Date toYear);
	
}
