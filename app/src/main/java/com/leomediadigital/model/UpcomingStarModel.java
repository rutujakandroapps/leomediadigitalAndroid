package com.leomediadigital.model;

import java.io.Serializable;

public class UpcomingStarModel implements Serializable {

    String id,type,name,description,image,nameId,descID, file_name, star_id;

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getDescID() {
        return descID;
    }

    public void setDescID(String descID) {
        this.descID = descID;
    }

    public String getStarID() {
        return star_id;
    }

    public void setStarID(String star_id) {
        this.star_id = star_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }


}
