package com.imcode.imcms.components.datainitializer;

import com.imcode.imcms.model.Loop;
import com.imcode.imcms.persistence.entity.LoopJPA;
import com.imcode.imcms.persistence.entity.Version;
import com.imcode.imcms.persistence.repository.LoopRepository;
import org.springframework.stereotype.Component;

@Component
public class LoopDataInitializer extends TestDataCleaner {
    public static final int TEST_VERSION_NO = 0;

    private final LoopRepository loopRepository;
    private final VersionDataInitializer versionDataInitializer;

    public LoopDataInitializer(LoopRepository loopRepository, VersionDataInitializer versionDataInitializer) {
        super(loopRepository);
        this.loopRepository = loopRepository;
        this.versionDataInitializer = versionDataInitializer;
    }

    public Loop createData(Loop loopDTO) {
        return createData(loopDTO, TEST_VERSION_NO);
    }

    public Loop createData(Loop loopDTO, int versionNo) {
        final Version testVersion = versionDataInitializer.createData(versionNo, loopDTO.getDocId());
        return createData(loopDTO, testVersion);
    }

    public Loop createData(Loop loopDTO, Version testVersion) {
        final LoopJPA testLoop = new LoopJPA(loopDTO, testVersion);
        return loopRepository.saveAndFlush(testLoop);
    }

    @Override
    public void cleanRepositories() {
        super.cleanRepositories();
        versionDataInitializer.cleanRepositories();
    }
}
