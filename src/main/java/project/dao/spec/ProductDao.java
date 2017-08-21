package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.ProductDetailsDto;
import project.entity.AddProductQuantityByManagerEntity;
import project.entity.ChartEntity;
import project.entity.EmergencyReplacementFetchEntity;
import project.entity.EmergencyReplacementProductListEntity;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.OrderDetailsCreationEntity;
import project.entity.OrderProcessingCreationEntity;
import project.entity.ProductDetails;
import project.entity.ProductDetailsEntity;
import project.entity.ProductRequestEntity;
import project.entity.ProductShortageEntityForManager;
import project.entity.ReportProductShortageInfoEntity;
import project.entity.RequestForProductEntity;

public interface ProductDao {
	List <ProductDetailsDto> getBy (String clientIdForReco, int start, int size) throws IOException;
	
	int getTotalCount () throws IOException;
	
	void insert (ProductDetailsDto productDetails) throws IOException;
	
	void update (ProductDetailsDto ob) throws IOException;
	
	void deleteBy (String id) throws IOException;
	
	// Update Product Count functions Starts
	boolean updateProductQuantityAfterPlacingOrder (String productId, String orderQuantity) throws IOException;
	
	void updateProductQuantityAfterProcessingOrder (String productId, String orderQuantity) throws IOException;
	
	void updateProductCountAfterDeletingOrder (OrderProcessingCreationEntity orderProcessingCreationEntity) throws IOException;
	
	void updateProductCountAfterReceivingRequestFromSalesperson (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws IOException;
	// Update Product Count functions Ends
	
	List <EmergencyReplacementProductListEntity> getEmergencyReplacementProductListForProductId (EmergencyReplacementFetchEntity emergencyReplacementFetchEntity) throws IOException;
	
	List <ProductDetails> getProductIdAndProductName () throws IOException;
	
	void updateProductShortageNoteToTheManagerAfterTheSalesmanReportsItToTheManager (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws IOException;
	
	void reportProductShortageAfterPlacingOrder (OrderDetailsCreationEntity orderDetailsCreationEntity) throws IOException;
	
	List <ProductShortageEntityForManager> getProductShortageEntityListForManager () throws IOException;
	
	void actionsAfterProductQuantityUpdatedByManager (AddProductQuantityByManagerEntity addProductQuantityByManagerEntity) throws IOException;
	
	ProductDetailsEntity getProductDetailsForOrderId (int orderId);
	
	boolean requestForProduct (RequestForProductEntity requestForProductEntity);
	
	List <ProductRequestEntity> fetchProductRequestTable ();
	
	ChartEntity getQuantitySoldForTypes ();
}
























