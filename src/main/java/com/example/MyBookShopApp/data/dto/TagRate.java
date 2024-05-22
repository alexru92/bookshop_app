package com.example.MyBookShopApp.data.dto;

public class TagRate {

    String tagName;
    long popularityRate;

    public TagRate(String tagName, Long popularityRate) {
        this.tagName = tagName;
        this.popularityRate = popularityRate;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getPopularityRate() {
        return popularityRate;
    }

    public void setPopularityRate(Long popularityRate) {
        this.popularityRate = popularityRate;
    }

    public String getFrontEndClass(Long tagsQuantity){
        long popularityRateInPercent = (1000 * popularityRate / tagsQuantity);

        if (popularityRateInPercent <= 17){return "Tag Tag_xs";}
        else if (popularityRateInPercent <= 20){return "Tag Tag_sm";}
        else if (popularityRateInPercent <= 22){return "Tag";}
        else if (popularityRateInPercent <= 26){return "Tag Tag_md";}
        else {return "Tag Tag_lg";}
    }
}
