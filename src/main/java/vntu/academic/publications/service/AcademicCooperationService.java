package vntu.academic.publications.service;

import java.util.Date;

import vntu.academic.publications.dto.CooperationNetworkDTO;

public interface AcademicCooperationService {
	
	CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName);
	
	CooperationNetworkDTO fetchPublicationsCooperationNetworkInYears(String organizationName, Date fromYear, Date toYear);
	
}
