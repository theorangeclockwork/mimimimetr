package inovus.task.mimimimetr.service;

import inovus.task.mimimimetr.model.Contender;

import java.util.List;
import java.util.Set;

public interface ContenderService {

    List<Contender> getTop10ByScore();

    void save(Contender contender);

    void saveAll(List<Contender> contenders);

    List<Contender> getAll();

    List<Contender> getTwoRandomContendersNotUsedBefore(Set<Long> ids);

    List<Contender> getTwoRandomContenders();

    void updateContenderIncrementScoreBy1(Long id);

}
