package project.service.spec;

import java.util.List;

import project.entity.AddProductQuantityByManagerEntity;
import project.entity.EmergencyReplacementFetchEntity;
import project.entity.EmergencyReplacementProductListEntity;
import project.entity.ProductDetails;
import project.entity.ProductDetailsCreationEntity;
import project.entity.ProductDetailsFetchEntity;
import project.entity.ProductDetailsListEntity;
import project.entity.ProductShortageEntityForManager;
import project.entity.ReportProductShortageInfoEntity;
import project.exception.ServiceException;

public interface ProductService {
	
	ProductDetailsListEntity getBy (ProductDetailsFetchEntity productDetailsFetchEntity) throws ServiceException;
	
	void insert (ProductDetailsCreationEntity productDetailsCreationEntity) throws ServiceException;
	
	void deleteBy (String id) throws ServiceException;
	
	void update (ProductDetailsCreationEntity ob) throws ServiceException;
	
	List <EmergencyReplacementProductListEntity> emergencyReplacement (EmergencyReplacementFetchEntity emergencyReplacementFetchEntity) throws ServiceException;
	
	List <ProductDetails> getProductDetailsList () throws ServiceException;
	
	void reportProductShortage (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws ServiceException;
	
	List <ProductShortageEntityForManager> getProductShortageEntityListForManager () throws ServiceException;
	
	void addProductQuantityByTheManager (AddProductQuantityByManagerEntity addProductQuantityByManagerEntity) throws ServiceException;
}
