package rest.model.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

@Getter
@Setter
@PackagePrivate
public class UserRequest {

	String user;
	String senha;
}
