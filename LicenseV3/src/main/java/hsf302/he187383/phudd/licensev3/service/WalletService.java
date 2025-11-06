package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;
    private final WalletTxnRepository txnRepo;
    private final TopupRepository topupRepo;
    private final UserRepository userRepository;

    @Transactional
    public void addCredits(UUID userId, long credits) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
                    newWallet.setBalance(0L); // mặc định 0
                    newWallet.setStatus(WalletStatus.ACTIVE);
                    return newWallet;
                });

        wallet.setBalance(wallet.getBalance() + credits);
        walletRepo.save(wallet);
    }

    public Wallet getWalletByUserId(UUID userId) {
        return walletRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Page<Wallet> searchWallets(UUID userId, WalletStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return walletRepo.searchWallets(userId, status, pageable);
    }
}


