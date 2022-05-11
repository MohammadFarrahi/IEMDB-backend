package ie.iemdb.model.DTO;

import java.time.Period;

public class ActorBriefDTO {
    private int id;
    private String name;
    private Integer age;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
