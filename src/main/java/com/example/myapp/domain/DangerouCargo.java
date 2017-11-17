package com.example.myapp.domain;


public class DangerouCargo {
    private Integer dangerousId;
    private String cargoName;
    private String safeCardPath;

    public DangerouCargo(){
        super();
    }

    public DangerouCargo(Integer dangerousId, String cargoName, String safeCardPath){
        super();
        this.dangerousId = dangerousId;
        this.cargoName = cargoName;
        this.safeCardPath = safeCardPath;
    }

    public Integer getDangerousId() {
        return dangerousId;
    }

    public void setDangerousId(Integer dangerousId) {
        this.dangerousId = dangerousId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getSafeCardPath() {
        return safeCardPath;
    }

    public void setSafeCardPath(String safeCardPath) {
        this.safeCardPath = safeCardPath;
    }

    public String toString(){
        return "dangerousId:"+this.dangerousId+"cargoName:"+this.cargoName+"safeCardPath:"+this.safeCardPath;
    }
}
