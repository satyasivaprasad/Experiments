package com.newgame.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AgentInfoResult {

@SerializedName("AgentID")
@Expose
private String AgentID;
@SerializedName("AgentName")
@Expose
private String AgentName;
@SerializedName("TabID")
@Expose
private String TabID;

/**
* 
* @return
* The AgentID
*/
public String getAgentID() {
return AgentID;
}

/**
* 
* @param AgentID
* The AgentID
*/
public void setAgentID(String AgentID) {
this.AgentID = AgentID;
}

/**
* 
* @return
* The AgentName
*/
public String getAgentName() {
return AgentName;
}

/**
* 
* @param AgentName
* The AgentName
*/
public void setAgentName(String AgentName) {
this.AgentName = AgentName;
}

/**
* 
* @return
* The TabID
*/
public String getTabID() {
return TabID;
}

/**
* 
* @param TabID
* The TabID
*/
public void setTabID(String TabID) {
this.TabID = TabID;
}

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}