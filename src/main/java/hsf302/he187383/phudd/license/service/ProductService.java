package hsf302.he187383.phudd.license.service;


import hsf302.he187383.phudd.license.model.Product;
import hsf302.he187383.phudd.license.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo){ this.repo = repo; }

    public List<Product> list(){ return repo.findAll(); }

    @Transactional
    public Product create(Product p){ return repo.save(p); }

    @Transactional
    public Product update(UUID id, Product patch){
        var p = repo.findById(id).orElseThrow(() -> new ExceptionInInitializerError("Product not found"));
        if (patch.getName()!=null) p.setName(patch.getName());
        if (patch.getCurrentVersion()!=null) p.setCurrentVersion(patch.getCurrentVersion());
        return p;
    }
}
