package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.dto.product.*;
import hsf302.he187383.phudd.license.mapper.ProductMapper;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public ProductResp create(ProductCreateReq req) {
        // unique code
        if (repo.existsByCodeIgnoreCase(req.getCode())) {
            throw new ResponseStatusException(CONFLICT, "Product code already exists");
        }
        Product entity = mapper.toEntity(req);
        entity = repo.save(entity);
        return mapper.toResp(entity);
    }

    public ProductResp update(UUID id, ProductUpdateReq req) {
        Product entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));

        // unique code (excluding current)
        if (repo.existsByCodeIgnoreCaseAndIdNot(req.getCode(), id)) {
            throw new ResponseStatusException(CONFLICT, "Product code already exists");
        }

        mapper.update(entity, req); // MapStruct updates code/name/description
        entity = repo.save(entity);
        return mapper.toResp(entity);
    }

    public void delete(UUID id) {
        Product entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
        try {
            repo.delete(entity);
        } catch (DataIntegrityViolationException e) {
            // FK đang tham chiếu (Plan…)
            throw new ResponseStatusException(CONFLICT, "Product is referenced by other records");
        }
    }

    public ProductResp get(UUID id) {
        return repo.findById(id)
                .map(mapper::toResp)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }

    public Page<ProductResp> list(String q, Pageable pageable) {
        Page<Product> page;
        if (q == null || q.isBlank()) {
            page = repo.findAll(pageable);
        } else {
            page = repo.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable);
        }
        return page.map(mapper::toResp);
    }
}
