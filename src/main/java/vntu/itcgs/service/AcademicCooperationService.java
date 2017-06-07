package vntu.itcgs.service;

import java.util.Date;

import vntu.itcgs.dto.CooperationNetwork;

public interface AcademicCooperationService {

	CooperationNetwork fetchCoAuthorsCooperationNetwork(String organizationName, boolean coAuthorsVerification);

	CooperationNetwork fetchPublicationsCooperationNetwork(String organizationName, Date fromYear, Date toYear, boolean coAuthorsVerification);

}
