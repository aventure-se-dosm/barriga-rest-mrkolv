package rest.model.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

@Getter
@Setter
@PackagePrivate
public class UserResponse{

	String user;
	String senha;
}
