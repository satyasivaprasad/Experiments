package com.newgame.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTransactionsModel {

    @SerializedName("ID")
    @Expose
    private Integer ID;
    @SerializedName("GameID")
    @Expose
    private Integer GameID;
    @SerializedName("GameTypeID")
    @Expose
    private Integer GameTypeID;
    @SerializedName("Amount")
    @Expose
    private Integer Amount;
    @SerializedName("Number")
    @Expose
    private String Number;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("AgentID")
    @Expose
    private Integer AgentID;
    @SerializedName("Created")
    @Expose
    private String Created;
    @SerializedName("UserID")
    @Expose
    private Integer UserID;
    @SerializedName("ModifiedDate")
    @Expose
    private String ModifiedDate;
    @SerializedName("TransactionID")
    @Expose
    private Integer TransactionID;
    @SerializedName("TabID")
    @Expose
    private Integer TabID;
    @SerializedName("GameName")
    @Expose
    private String GameName;
    @SerializedName("GameConfigName")
    @Expose
    private String GameConfigName;
    @SerializedName("TabName")
    @Expose
    private String TabName;
    @SerializedName("AgentName")
    @Expose
    private String AgentName;
    @SerializedName("TransactionTotal")
    @Expose
    private String TransactionTotal;
    @SerializedName("Editby")
    @Expose
    private String Editby;

    @SerializedName("SubAgentID")
    @Expose
    private String SubAgentID;

    @SerializedName("SubAgentName")
    @Expose
    private String SubAgentName;

    public String getEditBy() {
        return Editby;
    }

    public void setEditBy(String editBy) {
        Editby = editBy;
    }

    /**
     * @return The ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param ID The ID
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return The GameID
     */
    public Integer getGameID() {
        return GameID;
    }

    /**
     * @param GameID The GameID
     */
    public void setGameID(Integer GameID) {
        this.GameID = GameID;
    }

    /**
     * @return The GameTypeID
     */
    public Integer getGameTypeID() {
        return GameTypeID;
    }

    /**
     * @param GameTypeID The GameTypeID
     */
    public void setGameTypeID(Integer GameTypeID) {
        this.GameTypeID = GameTypeID;
    }

    /**
     * @return The Amount
     */
    public Integer getAmount() {
        return Amount;
    }

    /**
     * @param Amount The Amount
     */
    public void setAmount(Integer Amount) {
        this.Amount = Amount;
    }

    /**
     * @return The Number
     */
    public String getNumber() {
        return Number;
    }

    /**
     * @param Number The Number
     */
    public void setNumber(String Number) {
        this.Number = Number;
    }

    /**
     * @return The Date
     */
    public String getDate() {
        return Date;
    }

    /**
     * @param Date The Date
     */
    public void setDate(String Date) {
        this.Date = Date;
    }

    /**
     * @return The AgentID
     */
    public Integer getAgentID() {
        return AgentID;
    }

    /**
     * @param AgentID The AgentID
     */
    public void setAgentID(Integer AgentID) {
        this.AgentID = AgentID;
    }

    /**
     * @return The Created
     */
    public String getCreated() {
        return Created;
    }

    /**
     * @param Created The Created
     */
    public void setCreated(String Created) {
        this.Created = Created;
    }

    /**
     * @return The UserID
     */
    public Integer getUserID() {
        return UserID;
    }

    /**
     * @param UserID The UserID
     */
    public void setUserID(Integer UserID) {
        this.UserID = UserID;
    }

    /**
     * @return The ModifiedDate
     */
    public String getModifiedDate() {
        return ModifiedDate;
    }

    /**
     * @param ModifiedDate The ModifiedDate
     */
    public void setModifiedDate(String ModifiedDate) {
        this.ModifiedDate = ModifiedDate;
    }

    /**
     * @return The TransactionID
     */
    public Integer getTransactionID() {
        return TransactionID;
    }

    /**
     * @param TransactionID The TransactionID
     */
    public void setTransactionID(Integer TransactionID) {
        this.TransactionID = TransactionID;
    }

    /**
     * @return The TabID
     */
    public Integer getTabID() {
        return TabID;
    }

    /**
     * @param TabID The TabID
     */
    public void setTabID(Integer TabID) {
        this.TabID = TabID;
    }

    /**
     * @return The GameName
     */
    public String getGameName() {
        return GameName;
    }

    /**
     * @param GameName The GameName
     */
    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    /**
     * @return The GameConfigName
     */
    public String getGameConfigName() {
        return GameConfigName;
    }

    /**
     * @param GameConfigName The GameConfigName
     */
    public void setGameConfigName(String GameConfigName) {
        this.GameConfigName = GameConfigName;
    }

    /**
     * @return The TabName
     */
    public String getTabName() {
        return TabName;
    }

    /**
     * @param TabName The TabName
     */
    public void setTabName(String TabName) {
        this.TabName = TabName;
    }

    /**
     * @return The AgentName
     */
    public String getAgentName() {
        return AgentName;
    }

    /**
     * @param AgentName The AgentName
     */
    public void setAgentName(String AgentName) {
        this.AgentName = AgentName;
    }

    /**
     * @return The TransactionTotal
     */
    public String getTransactionTotal() {
        return TransactionTotal;
    }

    /**
     * @param TransactionTotal The TransactionTotal
     */
    public void setTransactionTotal(String TransactionTotal) {
        this.TransactionTotal = TransactionTotal;
    }

    public String getSubAgentID() {
        return SubAgentID;
    }

    public void setSubAgentID(String subAgentID) {
        SubAgentID = subAgentID;
    }

    public String getSubAgentName() {
        return SubAgentName;
    }

    public void setSubAgentName(String subAgentName) {
        SubAgentName = subAgentName;
    }
}