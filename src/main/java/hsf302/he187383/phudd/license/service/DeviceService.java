package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.model.Device;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

//@Service
//public class DeviceService {
//    private final DeviceRepository repo;
//    private final OrganizationRepository orgRepo;
//
//    public DeviceService(DeviceRepository repo, OrganizationRepository orgRepo){
//        this.repo = repo; this.orgRepo = orgRepo;
//    }
//
//    @Transactional
//    public Device upsertByFingerprint(UUID orgId, String fp, String name, String os){
//        orgRepo.findById(orgId).orElseThrow(() -> new ExceptionInInitializerError("Org not found"));
//        return repo.findByOrganizationIdAndFingerprint(orgId, fp)
//                .map(d -> { d.setName(name); d.setOs(os); d.setLastSeen(Instant.now()); return d; })
//                .orElseGet(() -> {
//                    Device d = Device.builder()
//                            .organization(OrganizationRef(orgId))
//                            .fingerprint(fp).name(name).os(os)
//                            .firstSeen(Instant.now())
//                            .build();
//                    return repo.save(d);
//                });
//    }
//
//    @Transactional
//    public Device setBlocked(UUID deviceId, boolean blocked){
//        var d = repo.findById(deviceId).orElseThrow(() -> new ExceptionInInitializerError("Device not found"));
//        d.setBlocked(blocked);
//        return d;
//    }
//
//}
