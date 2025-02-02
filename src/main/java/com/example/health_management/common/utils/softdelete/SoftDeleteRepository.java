package com.example.health_management.common.utils.softdelete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface SoftDeleteRepository<T extends SoftDeletable, ID> extends JpaRepository<T, ID> {

    @Override
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e = :entity")
    void delete(@Param("entity") T entity);

    @Override
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void deleteById(@Param("id") ID id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    List<T> findAllActive();

    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NOT NULL")
    List<T> findAllDeleted();

    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL AND e.id = :id")
    T findByIdActive(@Param("id") ID id);
}
