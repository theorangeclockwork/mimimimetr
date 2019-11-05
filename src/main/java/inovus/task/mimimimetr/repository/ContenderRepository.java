package inovus.task.mimimimetr.repository;

import inovus.task.mimimimetr.model.Contender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ContenderRepository extends JpaRepository<Contender, Long> {

    List<Contender> findTop10ByOrderByScoreDesc();

    @Query(value = "SELECT * FROM contender WHERE id NOT IN :ids ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
    List<Contender> findTwoRandomContendersNotUsedBefore(@Param("ids") Set<Long> ids);


}
