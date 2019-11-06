package inovus.task.mimimimetr.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contenders")
public class Contender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private long score;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;


}
