package inovus.task.mimimimetr.repository;

import inovus.task.mimimimetr.model.Contender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Repository
public interface ContenderRepository extends JpaRepository<Contender, Long> {

    List<Contender> findTop10ByOrderByScoreDesc();

    @Query(value = "SELECT * FROM contenders WHERE id NOT IN :ids ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
    List<Contender> findTwoRandomContendersNotUsedBefore(@Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM contenders ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
    List<Contender> findTwoRandomContenders();

    @Modifying
    @Query(value = "UPDATE contenders SET score = score + 1 WHERE id = :id", nativeQuery = true)
    void updateContenderIncrementScoreBy1(@Param("id") Long id);

}
