package project.controller;
//import static java.lang.System.out;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.dao.spec.ProductDao;
import project.entity.ChartEntity;
import project.entity.EmergencyReplacementFetchEntity;
import project.entity.EmergencyReplacementProductListEntity;
import project.entity.ProductDetails;
import project.entity.ProductDetailsCreationEntity;
import project.entity.ProductDetailsEntity;
import project.entity.ProductDetailsFetchEntity;
import project.entity.ProductDetailsListEntity;
import project.entity.ReportProductShortageInfoEntity;
import project.entity.RequestForProductEntity;
import project.service.spec.ClientService;
import project.service.spec.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	ProductDao productDao;
	
//	private String leadIdReceived;
	
	@RequestMapping(value = "/productmanagement")
	public String productmanagement() {
		ModelMap model = new ModelMap();
	    model.put ("productName", "myProductName");
		return "productmanagement";
	}
	
	
	
//	@RequestMapping (value = "/productmanagement/openPageWithLeadsRecommendation", method = RequestMethod.POST)
//	@ResponseBody
//	public void findProductByProductId (@RequestParam String leadId) {
//		System.out.println ("Lead Id = " + leadId);
//		leadIdReceived = leadId;
//		System.out.println ("Lead Id Received = " + leadIdReceived);
//	}
	
	
	
	@RequestMapping (value = "/productmanagement/fetchProductDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductDetailsListEntity findProductByProductId (@RequestBody ProductDetailsFetchEntity entity) {
		ProductDetailsListEntity ob =  productService.getBy (entity);
		return ob;
	}
	
	@RequestMapping (value = "/productmanagement/addProductDetails", method = RequestMethod.POST)
	@ResponseBody
	public void addProductDetails (@RequestBody ProductDetailsCreationEntity productDetailsCreationEntity) {
		productService.insert (productDetailsCreationEntity);
	}
	
	
	@RequestMapping (value = "/productmanagement/modifyProductDetails", method = RequestMethod.POST)
	@ResponseBody
	public void updateProductDetails (@RequestBody ProductDetailsCreationEntity productDetailsCreationEntity) {
		productService.update (productDetailsCreationEntity);
	}
	
	
	@RequestMapping (value = "/productmanagement/deleteProductDetails", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteProductDetails (@RequestParam String id) {
		productService.deleteBy (id);
	}
	
	@RequestMapping (value = "/productmanagement/emergencyReplacement", method = RequestMethod.POST)
	@ResponseBody
	public List <EmergencyReplacementProductListEntity> emergencyReplacement (@RequestBody EmergencyReplacementFetchEntity emergencyReplacementFetchEntity) {
		return productService.emergencyReplacement (emergencyReplacementFetchEntity);
	}
	
	
	@RequestMapping (value = "/productmanagement/getProductNameAndId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <ProductDetails> getProductDetailsList () {
		List <ProductDetails> productDetailsList = productService.getProductDetailsList ();
		return productDetailsList;
	}
	
	@RequestMapping (value = "/productmanagement/reportProductShortage", method = RequestMethod.POST)
	@ResponseBody
	public void reportProductShortage (@RequestBody ReportProductShortageInfoEntity reportProductShortageInfoEntity) {
		productService.reportProductShortage (reportProductShortageInfoEntity);
	}
	
	
	@RequestMapping (value = "/productmanagement/getProductForOrderId", method = RequestMethod.POST)
	@ResponseBody
	public ProductDetailsEntity getProductDetailsForOrderId (@RequestParam int orderId) {
		return productDao.getProductDetailsForOrderId (orderId);
	}
	
	
	@RequestMapping (value = "/viewProductWithLeadId", method = RequestMethod.GET)
	@ResponseBody
	public String viewProductWithLeadId (@RequestParam String leadId) {
		return "productmanagement";
	}
	
	
	@RequestMapping (value = "/productmanagement/requestForProduct", method = RequestMethod.POST)
	@ResponseBody
	public boolean requestForProduct (@RequestBody RequestForProductEntity requestForProductEntity) {
		return productDao.requestForProduct (requestForProductEntity);
	}
	
	@RequestMapping (value = "/productmanagement/getQuantitySoldForTypes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ChartEntity getQuantitySoldForTypes () {
		return productDao.getQuantitySoldForTypes ();
	}
}

















