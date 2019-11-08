package inovus.task.mimimimetr.form;

import inovus.task.mimimimetr.model.Contender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ContendersDto {
    private List<Contender> contenderList;
}
