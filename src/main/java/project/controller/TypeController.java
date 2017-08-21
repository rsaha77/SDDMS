package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import project.entity.TypeEntity;
import project.entity.TypeFetchEntity;
import project.service.spec.TypeService;

@Controller
public class TypeController {
	
	@Autowired
	TypeService typeService;
	
	@RequestMapping (value = "/newType")
	public String newType () {
		return "newType";
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/type", method = RequestMethod.POST)
	@ResponseBody
	public TypeEntity getTypes (@RequestBody TypeFetchEntity typeFetchEntity) {
		return typeService.getBy (typeFetchEntity);
	}
}
















