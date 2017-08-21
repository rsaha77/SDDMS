package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import project.dto.ProductDetailsDto;
import project.dao.spec.ProductDao;
import project.entity.AddProductQuantityByManagerEntity;
import project.entity.EmergencyReplacementFetchEntity;
import project.entity.EmergencyReplacementProductListEntity;
import project.entity.ProductDetails;
import project.entity.ProductDetailsCreationEntity;
import project.entity.ProductDetailsEntity;
import project.entity.ProductDetailsFetchEntity;
import project.entity.ProductDetailsListEntity;
import project.entity.ProductShortageEntityForManager;
import project.entity.ReportProductShortageInfoEntity;
import project.exception.ServiceException;
import project.service.spec.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;

	
	@Override
	public ProductDetailsListEntity getBy (ProductDetailsFetchEntity entity) throws ServiceException {
		List <ProductDetailsDto> details = null;
		int productCount = 0;
		try {
			details = productDao.getBy (entity.getClientIdForRecommendation(), entity.getLength(), entity.getStart());
			productCount = productDao.getTotalCount ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
		
		List <ProductDetailsEntity> entities = Lists.newArrayList ();
		
		for (ProductDetailsDto detail : details) {
			ProductDetailsEntity productDetailsEntity = new ProductDetailsEntity (detail);
			entities.add (productDetailsEntity);
		}
		return new ProductDetailsListEntity (entity.getDraw (), productCount, productCount, entities);
	}
	
	
	
	@Override
	@Transactional
	public void insert (ProductDetailsCreationEntity productDetailsCreationEntity) throws ServiceException {
		ProductDetailsDto productDetails = new ProductDetailsDto (productDetailsCreationEntity);
		try {
			productDao.insert (productDetails);
		} catch (IOException e) {
			throw new ServiceException("Cannot add product details for productId: " + productDetailsCreationEntity.getProductId(), e);
		}
	}
	
	
	@Override
	public void update (ProductDetailsCreationEntity productDetailsCreationEntity) throws ServiceException {
		ProductDetailsDto productDetails = new ProductDetailsDto (productDetailsCreationEntity);
		try {
			productDao.update (productDetails);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
		} catch (IOException e) {
			throw new ServiceException("Cannot edit client detail for productId: " + productDetailsCreationEntity.getProductId (), e);
		}
	}
	
	
	@Override
	public void deleteBy (String id) throws ServiceException {
		try {
			productDao.deleteBy (id);
		} catch (IOException e) {
			throw new ServiceException("Cannot delete product detail for productId: " + id, e);
		}
	}
	
	@Override
	public List <EmergencyReplacementProductListEntity> emergencyReplacement (EmergencyReplacementFetchEntity emergencyReplacementFetchEntity) throws ServiceException {
		try {
			return productDao.getEmergencyReplacementProductListForProductId (emergencyReplacementFetchEntity);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
	
	@Override
	public List <ProductDetails> getProductDetailsList () throws ServiceException {
		try {
			return productDao.getProductIdAndProductName ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
	
	@Override
	public List <ProductShortageEntityForManager> getProductShortageEntityListForManager () throws ServiceException {
		try {
			return productDao.getProductShortageEntityListForManager ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
	
	@Override
	public void reportProductShortage (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws ServiceException {
		try {
			productDao.updateProductShortageNoteToTheManagerAfterTheSalesmanReportsItToTheManager (reportProductShortageInfoEntity);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
	
	@Override
	public void addProductQuantityByTheManager (AddProductQuantityByManagerEntity addProductQuantityByManagerEntity) throws ServiceException {
		try {
			productDao.actionsAfterProductQuantityUpdatedByManager (addProductQuantityByManagerEntity);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
}












