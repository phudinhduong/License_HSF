package hsf302.he187383.phudd.license.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JpaRefResolverImpl implements JpaRefResolver {

    @PersistenceContext
    private EntityManager em;

    @Override
    public <T> T getRef(Class<T> type, UUID id) {
        return em.getReference(type, id); // proxy, không hit DB ngay
    }
}
