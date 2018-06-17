package com.newgame.models;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SubGameStatus {

@Expose
private String GameID;
@Expose
private String Name;
@Expose
private String Note;
@Expose
private String OpenStatus;
@Expose
private String BracketStatus;
@Expose
private String CloseStatus;
@Expose
private String OPanaStatus;
@Expose
private String CpanaStatus;
@Expose
private String CurrentTime;

/**
* 
* @return
* The GameID
*/
public String getGameID() {
return GameID;
}

/**
* 
* @param GameID
* The GameID
*/
public void setGameID(String GameID) {
this.GameID = GameID;
}

/**
* 
* @return
* The Name
*/
public String getName() {
return Name;
}

/**
* 
* @param Name
* The Name
*/
public void setName(String Name) {
this.Name = Name;
}

/**
* 
* @return
* The Note
*/
public String getNote() {
return Note;
}

/**
* 
* @param Note
* The Note
*/
public void setNote(String Note) {
this.Note = Note;
}

/**
* 
* @return
* The OpenStatus
*/
public String getOpenStatus() {
return OpenStatus;
}

/**
* 
* @param OpenStatus
* The OpenStatus
*/
public void setOpenStatus(String OpenStatus) {
this.OpenStatus = OpenStatus;
}

/**
* 
* @return
* The BracketStatus
*/
public String getBracketStatus() {
return BracketStatus;
}

/**
* 
* @param BracketStatus
* The BracketStatus
*/
public void setBracketStatus(String BracketStatus) {
this.BracketStatus = BracketStatus;
}

/**
* 
* @return
* The CloseStatus
*/
public String getCloseStatus() {
return CloseStatus;
}

/**
* 
* @param CloseStatus
* The CloseStatus
*/
public void setCloseStatus(String CloseStatus) {
this.CloseStatus = CloseStatus;
}

/**
* 
* @return
* The OPanaStatus
*/
public String getOPanaStatus() {
return OPanaStatus;
}

/**
* 
* @param OPanaStatus
* The OPanaStatus
*/
public void setOPanaStatus(String OPanaStatus) {
this.OPanaStatus = OPanaStatus;
}

/**
* 
* @return
* The CpanaStatus
*/
public String getCpanaStatus() {
return CpanaStatus;
}

/**
* 
* @param CpanaStatus
* The CpanaStatus
*/
public void setCpanaStatus(String CpanaStatus) {
this.CpanaStatus = CpanaStatus;
}

/**
* 
* @return
* The CurrentTime
*/
public String getCurrentTime() {
return CurrentTime;
}

/**
* 
* @param CurrentTime
* The CurrentTime
*/
public void setCurrentTime(String CurrentTime) {
this.CurrentTime = CurrentTime;
}

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}
