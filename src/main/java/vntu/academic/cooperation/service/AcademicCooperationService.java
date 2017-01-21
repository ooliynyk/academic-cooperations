package vntu.academic.cooperation.service;

import java.util.Date;

import vntu.academic.cooperation.dto.CooperationNetworkDTO;

public interface AcademicCooperationService {
	
	CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName);
	
	CooperationNetworkDTO fetchPublicationsCooperationNetworkInYears(String organizationName, Date fromYear, Date toYear);
	
}
