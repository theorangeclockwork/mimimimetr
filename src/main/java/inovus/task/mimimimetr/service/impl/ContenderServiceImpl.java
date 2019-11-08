package inovus.task.mimimimetr.service.impl;

import inovus.task.mimimimetr.model.Contender;
import inovus.task.mimimimetr.repository.ContenderRepository;
import inovus.task.mimimimetr.service.ContenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ContenderServiceImpl implements ContenderService {

    private final ContenderRepository contenderRepository;

    @Autowired
    public ContenderServiceImpl(ContenderRepository contenderRepository) {
        this.contenderRepository = contenderRepository;
    }

    @Override
    public List<Contender> getTop10ByScore() {
        return contenderRepository.findTop10ByOrderByScoreDesc();
    }

    @Override
    public void save(Contender contender) {
        contenderRepository.save(contender);
    }

    @Override
    public void saveAll(List<Contender> contenders) {
        for (Contender contender : contenders) {
            contenderRepository.save(contender);
        }
    }

    @Override
    public List<Contender> getAll() {
        return contenderRepository.findAll();
    }

    @Override
    public List<Contender> getTwoRandomContendersNotUsedBefore(Set<Long> ids) {
        return contenderRepository.findTwoRandomContendersNotUsedBefore(ids);
    }

    @Override
    public List<Contender> getTwoRandomContenders() {
        return contenderRepository.findTwoRandomContenders();
    }

    @Override
    public void updateContenderIncrementScoreBy1(Long id) {
        contenderRepository.updateContenderIncrementScoreBy1(id);
    }

}
