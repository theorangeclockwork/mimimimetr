package inovus.task.mimimimetr.form;

import inovus.task.mimimimetr.model.Contender;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContendersDto {

    private List<Contender> contenderList;

    public ContendersDto() {
        this.contenderList = new ArrayList<>();
    }

    public ContendersDto(List<Contender> contenderList) {
        this.contenderList = contenderList;
    }

    public void addContender(Contender contender) {
        this.contenderList.add(contender);
    }

}
