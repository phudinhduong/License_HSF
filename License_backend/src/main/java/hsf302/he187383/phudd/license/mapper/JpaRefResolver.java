package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.model.*;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

public interface JpaRefResolver {
    <T> T getRef(Class<T> type, UUID id);

    default Product toProduct(UUID id) { return id == null ? null : getRef(Product.class, id); }
    default Plan toPlan(UUID id) { return id == null ? null : getRef(Plan.class, id); }
    default User toUser(UUID id) { return id == null ? null : getRef(User.class, id); }
    default Order toOrder(UUID id) { return id == null ? null : getRef(Order.class, id); }
    default License toLicense(UUID id) { return id == null ? null : getRef(License.class, id); }
    default Account toAccount(UUID id) { return id == null ? null : getRef(Account.class, id); }
    default Wallet toWallet(UUID id) { return id == null ? null : getRef(Wallet.class, id); }
    default WalletTxn toWalletTxn(UUID id) { return id == null ? null : getRef(WalletTxn.class, id); }
    default Topup toTopup(UUID id) { return id == null ? null : getRef(Topup.class, id); }
}
