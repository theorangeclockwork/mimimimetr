package inovus.task.mimimimetr.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

@Data
@Accessors(chain = true)
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

    @Lob
    @Column(name = "imageData", length = 100000)
    private byte[] imageData;

    public String getImageDataAsBase64() {
        try {
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageData));
            return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
