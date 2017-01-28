package vntu.academcoop.service;

import java.util.Date;

import vntu.academcoop.dto.CooperationNetworkDTO;

public interface AcademicCooperationService {
	
	CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName);
	
	CooperationNetworkDTO fetchPublicationsCooperationNetwork(String organizationName, Date fromYear, Date toYear);
	
}
