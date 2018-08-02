package com.newgame.models;

import android.os.Parcel;
import android.os.Parcelable;

public class GameTransactionsModel implements Parcelable {

	public String number;
	public String eachAmount;
	public String time;
	public String date;
	public String totalAmount;
	public String entryId;
	public String gameType;
	public String gameName;
	public int textColor;
	public String userName;
	public String transactionId;
	public String userId;
	public String Created_date;
	public String Updated_Date;

	public int getSubAgentID() {
		return SubAgentID;
	}

	public void setSubAgentID(int subAgentID) {
		SubAgentID = subAgentID;
	}

	public int SubAgentID;
	
	public String getUpdated_Date() {
		return Updated_Date;
	}

	public void setUpdated_Date(String updated_Date) {
		Updated_Date = updated_Date;
	}

	public String getCreated_date() {
		return Created_date;
	}

	public void setCreated_date(String created_date) {
		Created_date = created_date;
	}

    protected GameTransactionsModel(Parcel in) {
        number = in.readString();
        eachAmount = in.readString();
        time = in.readString();
        date = in.readString();
        totalAmount = in.readString();
        entryId = in.readString();
        gameType = in.readString();
        textColor = in.readInt();
        SubAgentID = in.readInt();
    }
    
    public GameTransactionsModel() {
        
    }

    
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getEachAmount() {
		return eachAmount;
	}

	public void setEachAmount(String eachAmount) {
		this.eachAmount = eachAmount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(eachAmount);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(totalAmount);
        dest.writeString(entryId);
        dest.writeString(gameType);
        dest.writeInt(textColor);
    }

    @SuppressWarnings("unused")
    public static final Creator<GameTransactionsModel> CREATOR = new Creator<GameTransactionsModel>() {
        @Override
        public GameTransactionsModel createFromParcel(Parcel in) {
            return new GameTransactionsModel(in);
        }

        @Override
        public GameTransactionsModel[] newArray(int size) {
            return new GameTransactionsModel[size];
        }
    };
}
