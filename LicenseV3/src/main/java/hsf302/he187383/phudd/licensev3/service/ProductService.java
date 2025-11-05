package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.model.Product;
import hsf302.he187383.phudd.licensev3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepo;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
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
        existing.setCode(p.getCode());
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        return productRepo.save(existing);
    }

    public void delete(UUID id) {
        productRepo.deleteById(id);
    }
}

