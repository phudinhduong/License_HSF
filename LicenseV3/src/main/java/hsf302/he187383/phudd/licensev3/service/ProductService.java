package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.model.Product;
import hsf302.he187383.phudd.licensev3.repository.PlanRepository;
import hsf302.he187383.phudd.licensev3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepo;
    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public Page<Product> findPage(String q, int page, int size) {
        var pageable = PageRequest.of(Math.max(0,page), Math.max(1, Math.min(size,50)),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        if (org.springframework.util.StringUtils.hasText(q)) {
            return productRepo.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable);
        }
        return productRepo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Product get(UUID id) {
        return productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }


    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {

        if (productRepo.findById(id).isEmpty()) return null;

        return productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional(readOnly = true)
    public Product findByCode(String code) {
        return productRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product create(Product p) {
        return productRepo.save(p);
    }

    public Product update(UUID id, Product p) {
        var existing = findById(id);
//        existing.setCode(p.getCode());
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        return productRepo.save(existing);
    }

    public void delete(UUID id) {
        System.out.println("aaaaaaa và");
        planRepository.deletePlanByProduct_Id(id);
        productRepo.deleteById(id);
    }
}

