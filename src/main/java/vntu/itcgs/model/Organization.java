package vntu.itcgs.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Organization implements Serializable {

	private static final long serialVersionUID = 6825168779462431195L;

	private final String id;

	private final String name;

}
