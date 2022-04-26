package ie.iemdb.model.DTO;

public class MovieBriefDTO{
    private int id;
    private String name;
    private String summary;
    private Double imdbRate;
    private String coverImgUrl;
    private String releaseDate;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public Double getImdbRate() {
        return imdbRate;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImdbRate(Double imdbRate) {
        this.imdbRate = imdbRate;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
