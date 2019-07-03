package com.github.unassignedxd.deathmarkers;

//this class is used to store player information regarding a name and a location.
//used for storage
public class PlayerInfo {

    protected String name;
    protected double xCoord;
    protected double yCoord;
    protected double zCoord;

    public PlayerInfo(String name, double xCoord, double yCoord, double zCoord) {
        this.name = name;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    public String getName() {
        return name;
    }

    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public double getzCoord() {
        return zCoord;
    }

    public String getWriteableFormat() {
        return getName() + "#" + getxCoord() + "#" + getyCoord() + "#" + getzCoord() + "#";
    }

}
