package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.InvalidPriceException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Product;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.payload.mapper.ProductVariantMapper;
import com.Suyash.StockFlow.payload.request.ProductVariantDto;
import com.Suyash.StockFlow.payload.response.ProductVariantResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductVariantPageResponse;
import com.Suyash.StockFlow.repository.ProductRepository;
import com.Suyash.StockFlow.repository.ProductVariantRepository;
import com.Suyash.StockFlow.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {
    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantMapper mapper;

    @Override
    public ProductVariantResponse createProductVariant(ProductVariantDto variantDto) {

        variantRepository.findBySkuIgnoreCase(variantDto.getSku())
                .ifPresent(existing -> {
                        throw new DuplicateResourceFoundException("Product Variant with SKU: " + variantDto.getSku() + " already exists");
                });



        Product product = productRepository.findById(variantDto.getProductId())
                .orElseThrow(()-> new ResourceNotFoundException("Product with productId: "+ variantDto.getProductId() +" not found"));

        if(!product.isActive()){
            throw new ResourceNotFoundException("Product with productId: " + variantDto.getProductId() + " is not active");
        }

        validatePricing(variantDto.getPrice(), variantDto.getDiscountPrice());

        ProductVariant variant = mapper.toEntity(variantDto);
        variant.setProduct(product);
        variant.setActive(true);

        ProductVariant savedVariant = variantRepository.save(variant);
        return mapper.toResponse(savedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getProductVariantById(Long variantId) {
        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with variantId: " + variantId + " not found."
                ));
        return mapper.toResponse(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantPageResponse getAllProductVariants(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<ProductVariant> variantPage = variantRepository.findByActiveTrue(pageable);

        List<ProductVariantResponse> responses = variantPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductVariantPageResponse.builder()
                .content(responses)
                .pageNumber(variantPage.getNumber())
                .pageSize(variantPage.getSize())
                .totalElements(variantPage.getTotalElements())
                .totalPages(variantPage.getTotalPages())
                .lastPage(variantPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantPageResponse getAllVariantsByProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with productId: " + productId + " not found."));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ProductVariant> pageDetails = variantRepository.findByProductAndActiveTrue(product, pageable);

        List<ProductVariantResponse> responses = pageDetails.getContent()
                .stream()
                .map(mapper :: toResponse)
                .toList();

        return ProductVariantPageResponse.builder()
                .content(responses)
                .pageNumber(pageDetails.getNumber())
                .pageSize(pageDetails.getSize())
                .totalElements(pageDetails.getTotalElements())
                .totalPages(pageDetails.getTotalPages())
                .lastPage(pageDetails.isLast())
                .build();
    }

    @Override
    public ProductVariantResponse updateProductVariant(ProductVariantDto dto, Long variantId) {
        ProductVariant existingVariant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product Variant with variantId: " + variantId + " not found."
                ));

        variantRepository.findBySkuIgnoreCase(dto.getSku())
                .ifPresent(matched ->{
                    if(!matched.getVariantId().equals(variantId)){
                        throw new DuplicateResourceFoundException(
                                "Product variant with SKU "+ dto.getSku() + " already exists."
                        );
                    }
                });

        validatePricing(dto.getPrice(),dto.getDiscountPrice());

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with productId: " + dto.getProductId() + " not found."
                ));

        if(!product.isActive()){
            throw new ResourceNotFoundException("Product with productId: "+dto.getProductId()+ " is not active");
        }

        existingVariant.setVariantName(dto.getVariantName());
        existingVariant.setSku(dto.getSku());
        existingVariant.setPrice(dto.getPrice());
        existingVariant.setDiscountPrice(dto.getDiscountPrice());
        existingVariant.setQuantityAvailable(dto.getQuantityAvailable());
        existingVariant.setProduct(product);

        ProductVariant updated = variantRepository.save(existingVariant);
        return mapper.toResponse(updated);
    }

    @Override
    public String deactivateProductVariant(Long variantId) {
        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with variantId: " + variantId + " not found."
                ));

        variant.setActive(false);
        variantRepository.save(variant);
        return "Product Variant with variantId: " + variantId + " deactivated successfully";
    }

    private void validatePricing(BigDecimal price, BigDecimal discountPrice){
        if (discountPrice != null && discountPrice.compareTo(price) >= 0){
            throw new InvalidPriceException(
                    "Discount price (" + discountPrice + ") must be less than the original price (" + price + ")."
            );
        }
    }
}
