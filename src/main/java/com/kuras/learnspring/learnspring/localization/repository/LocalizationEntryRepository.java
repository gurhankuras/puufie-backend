package com.kuras.learnspring.learnspring.localization.repository;

import com.kuras.learnspring.learnspring.localization.entity.LocalizationEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizationEntryRepository extends JpaRepository<LocalizationEntry, Long> {

    // En güncel versiyonu bulmak için:
    @Query("""
        SELECT r
        FROM LocalizationEntry r
        WHERE r.locale = :locale AND r.version = (
            SELECT MAX(r2.version) FROM LocalizationEntry r2 WHERE r2.locale = :locale
        )
    """)
    List<LocalizationEntry> findAllForLatestVersion(@Param("locale") String locale);
}
