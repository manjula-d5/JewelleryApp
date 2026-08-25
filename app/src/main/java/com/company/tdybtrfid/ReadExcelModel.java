package com.company.tdybtrfid;

import java.util.Arrays;
import java.util.Objects;

public class ReadExcelModel {
    String tagname;
    String descp;
    String price;
    String pricePerGram;
    String weight;
    String views;
    String extension;
    byte[] data;
    String info;
    String imageUrl;
    private boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePerGram() {
        return pricePerGram;
    }

    public void setPricePerGram(String pricePerGram) {
        this.pricePerGram = pricePerGram;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ReadExcelModel{" +
                "tagname='" + tagname + '\'' +
                ", descp='" + descp + '\'' +
                ", price='" + price + '\'' +
                ", pricePerGram='" + pricePerGram + '\'' +
                ", weight='" + weight + '\'' +
                ", views='" + views + '\'' +
                ", extension='" + extension + '\'' +
                ", data=" + Arrays.toString(data) +
                ", info='" + info + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", expanded=" + expanded +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadExcelModel model = (ReadExcelModel) o;
        return Objects.equals(tagname, model.tagname) && Objects.equals(descp, model.descp) && Objects.equals(price, model.price) && Objects.equals(pricePerGram, model.pricePerGram) && Objects.equals(weight, model.weight) && Objects.equals(views, model.views) && Objects.equals(extension, model.extension) && Arrays.equals(data, model.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tagname, descp, price, pricePerGram, weight, views, extension);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
