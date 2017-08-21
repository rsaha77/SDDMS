package project.controller;


//import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import project.service.spec.RecentActivityService;


@Controller
public class DashboardController {
//	private static final String APP_NAME = "SDDMS";
	
//	@RequestMapping(value={"/", "/indexS"})
//	public String indexSalesperson (/*Model model, HttpSession session*/) {
////		model.addAttribute("appName", APP_NAME);
//		return "dashboardSalesman";
//	}

//	@RequestMapping(value = {"salesM","/indexM"})
//	public String indexManager (/*Model model, HttpSession session*/) {
////		model.addAttribute("appName", APP_NAME);
//		return "dashboardManager";
//	}

	@RequestMapping(value = {"salesD", "/indexD"})
	public String indexDirector (/*Model model, HttpSession session*/) {
//		model.addAttribute("appName", APP_NAME);
		return "dashboardDirector";
	}
}
