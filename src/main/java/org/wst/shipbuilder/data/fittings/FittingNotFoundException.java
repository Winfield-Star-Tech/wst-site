package org.wst.shipbuilder.data.fittings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Fitting")  // 404
public class FittingNotFoundException extends RuntimeException{

}
