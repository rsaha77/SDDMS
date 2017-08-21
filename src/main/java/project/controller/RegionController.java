package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import project.entity.RegionEntity;
import project.entity.RegionFetchEntity;
import project.service.spec.RegionService;

@Controller
public class RegionController {
	
	@Autowired
	RegionService regionService;
	
//	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/regions", method = RequestMethod.POST)
	@ResponseBody
	public RegionEntity getRegions (@RequestBody RegionFetchEntity regionFetchEntity) {
		return regionService.getBy (regionFetchEntity);
	}
}
















