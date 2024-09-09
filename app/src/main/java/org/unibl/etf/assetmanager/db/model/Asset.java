package org.unibl.etf.assetmanager.db.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.unibl.etf.assetmanager.util.Constants;

import java.io.Serializable;
import java.util.Date;
@Entity(tableName = Constants.TABLE_NAME_ASSET)
public class Asset implements Serializable {
    @PrimaryKey
    private Long id;
    @NotNull
    private String name;
    private String description;
    private Long barcode;
    private Double price;
    private Date creationDate;
    private String imageURI;
    @NotNull
    private Long asset_locationId;
    private Long asset_employeeId;

    public Asset(Long id, String name, String description, Long barcode, Double price, Date creationDate, String imageURI, Long asset_locationId, Long asset_employeeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.price = price;
        this.creationDate = creationDate;
        this.imageURI = imageURI;
        this.asset_locationId = asset_locationId;
        this.asset_employeeId = asset_employeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public Long getAsset_locationId() {
        return asset_locationId;
    }

    public void setAsset_locationId(Long asset_locationId) {
        this.asset_locationId = asset_locationId;
    }

    public Long getAsset_employeeId() {
        return asset_employeeId;
    }

    public void setAsset_employeeId(Long asset_employeeId) {
        this.asset_employeeId = asset_employeeId;
    }
}
