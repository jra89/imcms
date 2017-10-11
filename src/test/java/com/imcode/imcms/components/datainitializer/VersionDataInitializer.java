package com.imcode.imcms.components.datainitializer;

import com.imcode.imcms.components.cleaner.RepositoryTestDataCleaner;
import com.imcode.imcms.mapping.jpa.User;
import com.imcode.imcms.mapping.jpa.doc.Version;
import com.imcode.imcms.mapping.jpa.doc.VersionRepository;
import com.imcode.imcms.util.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class VersionDataInitializer extends RepositoryTestDataCleaner {
    private final VersionRepository versionRepository;
    private final UserDataInitializer userDataInitializer;

    private User user;

    public VersionDataInitializer(VersionRepository versionRepository,
                                  UserDataInitializer userDataInitializer) {
        super(versionRepository);
        this.versionRepository = versionRepository;
        this.userDataInitializer = userDataInitializer;
    }

    public Version createData(int versionIndex, int docId) {
        Version testVersion = versionRepository.findByDocIdAndNo(docId, versionIndex);

        if (testVersion != null) {
            return testVersion;
        }

        final int adminUserId = 1;
        user = userDataInitializer.createData(adminUserId);

        testVersion = Value.with(new Version(), version -> {
            version.setNo(versionIndex);
            version.setDocId(docId);
            version.setCreatedBy(user);
            version.setCreatedDt(new Date());
            version.setModifiedBy(user);
            version.setModifiedDt(new Date());
        });
        versionRepository.saveAndFlush(testVersion);

        return testVersion;
    }

    public User getUser() {
        return user;
    }
}
